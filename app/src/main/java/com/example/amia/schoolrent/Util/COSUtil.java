package com.example.amia.schoolrent.Util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.amia.schoolrent.Bean.Student;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class COSUtil {
    public static final int PUT_PROGRESS = 2000;
    public static final int RESULT_SUCCESS = 2001;
    public static final int RESULT_ERROR = 2002;

    private static String bucket = "schoolrent-1253946493";

    private static String appid = "1253946493";
    private static String secretId = "AKIDYzHC8uDDHvbsyHwK7oKo9JvMa6WlEgMr";
    private static String secretKey = "SuC0c4LQK4g7C6pSuBHBuNekfQ0PceS5";
    private static String region = "ap-guangzhou";
    private static String packageName = "/idle_pic/";

    private CosXmlServiceConfig serviceConfig;
    private CosXmlService cosXmlService;
    private TransferConfig transferConfig;
    private TransferManager transferManager;

    private static Map<String,COSXMLUploadTask> taskMap = new HashMap<>();

    public COSUtil(Context context) {
        serviceConfig = new CosXmlServiceConfig.Builder().setAppidAndRegion(appid,region).setDebuggable(true).builder();
        QCloudCredentialProvider credentialProvider = new ShortTimeCredentialProvider(secretId, secretKey, 300);
        cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);
        transferConfig = new TransferConfig.Builder().build();
        transferManager = new TransferManager(cosXmlService,transferConfig);
    }

    public String uploadFile(Student student,String srcPath, final Handler handler, final String id){
        String fileName = packageName + student.getUserId() + (new Date()).getTime();
        uploadFile(fileName,srcPath,handler,id);
        return fileName;
    }

    public void uploadFile(String cosPath, String srcPath, final Handler handler, final String id){
        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket,cosPath,srcPath,null);
        taskMap.put(id,cosxmlUploadTask);
        //设置上传进度回调
        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            private Progress progress = new Progress();
            @Override
            public void onProgress(long complete, long target) {
                Message msg = handler.obtainMessage();
                progress.progress = complete;
                progress.max = target;
                msg.what = PUT_PROGRESS;
                msg.obj = msg;
                handler.sendMessage(msg);
            }
        });

        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                taskMap.remove(id);
                Message msg = handler.obtainMessage();
                msg.what = RESULT_SUCCESS;
                msg.obj = result.printResult();
                handler.sendMessage(msg);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                taskMap.remove(id);
                Message msg = handler.obtainMessage();
                msg.what = RESULT_ERROR;
                msg.obj = exception == null ? serviceException.getMessage() : exception.toString();
                handler.sendMessage(msg);
            }
        });
    }

    public static void stop(String id){
        if(taskMap.containsKey(id)){
            COSXMLUploadTask task = taskMap.get(id);
            task.cancel();
            taskMap.remove(id);
        }
    }

    class Progress{
        long progress;
        long max;
    }
}
