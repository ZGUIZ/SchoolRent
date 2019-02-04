package com.example.amia.schoolrent.Task.TaskImpl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;

import com.example.amia.schoolrent.Bean.KeyValue;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.KeyTask;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.NetUtils;
import com.example.amia.schoolrent.Util.RSAUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.security.PublicKey;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KeyTaskImpl implements KeyTask {

    @Override
    public PublicKey getKey(Context context) throws IOException, JSONException {
        String result=NetUtils.requestDataFromNet(ActivityUtil.getString(context,R.string.host)+
            ActivityUtil.getString(context,R.string.getKey));
        JSONObject object = new JSONObject(result);
        String key= (String) object.get("publicKey");
        byte[] bytes = Base64.decode(key, Base64.DEFAULT);
        PublicKey publicKey = RSAUtil.restorePublicKey(bytes);

        return publicKey;
    }

    @Override
    public void isChangeKey(final Context context, final Handler handler) throws Exception {
        service.submit(new Runnable() {
            @Override
            public void run() {
                String publicKeyStr = null;
                try {
                    publicKeyStr = NetUtils.requestDataFromNet(ActivityUtil.getString(context, R.string.host) +
                            ActivityUtil.getString(context, R.string.getKey));
                    if(publicKeyStr == null){
                        linkError(handler);
                    }
                    JSONObject object = new JSONObject(publicKeyStr);
                    String key= (String) object.get("publicKey");
                    byte[] bytes = Base64.decode(key, Base64.DEFAULT);
                    //还原密钥以验证密钥是否有效
                    RSAUtil.restorePublicKey(bytes);
                    //检查与当前密钥是否一致
                    List<KeyValue> keyValues = LitePal.where("key = ?","publicKey").find(KeyValue.class);
                    Message message = handler.obtainMessage();
                    if(keyValues.size()>0){
                        KeyValue keyValue = keyValues.get(0);
                        if(keyValue.getValue().equals(key)){
                            /*isChange = false;*/
                            Student student = getCurrentAccount();
                            message.what = student == null ? 0:1;
                            message.obj = student;
                        } else {
                            keyValue.setValue(key);
                            keyValue.save();
                            message.what = 0;
                        }
                    } else{
                        //安装后首次启动则保存公钥
                        KeyValue keyValue =new KeyValue("publicKey",publicKeyStr);
                        keyValue.save();
                        message.what = 0;
                    }
                    handler.handleMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    linkError(handler);
                }
            }
        });
    }

    private void linkError(Handler handler){
        Message message = Message.obtain();
        message.what = -1;
        message.obj = null;
        handler.sendMessage(message);
    }

    private Student getCurrentAccount(){
        //查询当前登录账号
        List<Student> students = LitePal.where("beanStatus = ?","10000").find(Student.class);
        if(students!=null && students.size() == 1){
            return students.get(0);
        } else {
            return null;
        }
    }
}
