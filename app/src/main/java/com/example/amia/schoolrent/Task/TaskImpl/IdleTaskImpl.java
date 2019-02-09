package com.example.amia.schoolrent.Task.TaskImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.MapKeyValue;
import com.example.amia.schoolrent.Bean.Result;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.NetCallBack;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.COSUtil;
import com.example.amia.schoolrent.Util.NetImageCallback;
import com.example.amia.schoolrent.Util.NetUtils;

import org.litepal.LitePal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdleTaskImpl implements IdleTask {

    private Date updateTime;

    @Override
    public void getIndexClassify(final Context context,final Handler handler) {
        updateTime = new Date();
        NetUtils.get(ActivityUtil.getString(context, R.string.host) + ActivityUtil.getString(context, R.string.classify_index), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json,Classify.class);
                    if(r.getResult()){
                        List<Classify> results = (List<Classify>) r.getData();
                        msg.what = INDEX_CLASSIFY;
                        msg.obj = results;
                        LitePal.deleteAll(Classify.class);
                        for(int i=0;i<results.size();i++){
                            Classify classify = results.get(i);
                            //加载图片信息
                            loadIcon(classify.getClassifyId(),classify.getImageUrl(),handler);
                            //保存到数据库
                            classify.save();
                        }
                    } else {
                        msg.what = ERROR;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = ERROR;
                }finally {
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void error(String... msg) {
                Message message = handler.obtainMessage();
                message.what = ERRORWITHMESSAGE;
                message.obj = msg;
            }
        });
    }

    public void loadIcon(final String id,String url, final Handler handler){
        //下载图片
        if(url != null  && !"".equals(url)) {
            NetUtils.getImage(url, new NetImageCallback() {
                @Override
                public void finish(Bitmap bitmap) {
                    Message message = handler.obtainMessage();
                    message.what = CLASSIFY_ICON;

                    //封装对象
                    MapKeyValue<Bitmap> keyValue = new MapKeyValue<>();
                    keyValue.setId(id);
                    keyValue.setData(bitmap);

                    message.obj = keyValue;
                    handler.sendMessage(message);
                }

                @Override
                public void error() {
                }
            });
        }
    }

    @Override
    public List<Classify> getIndexClassifyFromCache() {
        List<Classify> classifies = LitePal.findAll(Classify.class);
        return classifies;
    }


    @Override
    public void getAllClassify(Context context,final Handler handler) {
        NetUtils.get(ActivityUtil.getString(context, R.string.host) + ActivityUtil.getString(context, R.string.classify_all), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json,Classify.class);
                    if(r.getResult()){
                        List<Classify> results = (List<Classify>) r.getData();
                        msg.what = CLASSIFY_ALL;
                        msg.obj = results;
                    } else {
                        msg.what = ERROR;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = ERROR;
                }finally {
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void error(String... msg) {
                Message message = handler.obtainMessage();
                message.what = ERRORWITHMESSAGE;
                message.obj = msg;
            }
        });
    }

    @Override
    public String uploadImage(Context context,Student student, String srcPath, Handler handler) {
        Date now = new Date();
        COSUtil cosUtil = new COSUtil(context);
        String id = "/"+student.getUserId() +"/"+now.getTime();
        String cosUrl = ActivityUtil.getString(context,R.string.idle_pic_package) + id;
        cosUtil.uploadFile(cosUrl,srcPath,handler, id);
        return id;
    }

    @Override
    public void stopUpload(String id) {
        COSUtil.stop(id);
    }

    @Override
    public void getListInfo(List<IdleInfo> list, int index,Handler handler) {

    }

    @Override
    public void pushIdle(Context context, IdleInfo idleInfo, final Handler handler) {

        try {
            idleInfo.setTitle(URLEncoder.encode(idleInfo.getTitle(),"utf-8"));
            idleInfo.setIdelInfo(URLEncoder.encode(idleInfo.getIdelInfo(),"utf-8"));
            if(idleInfo.getAddress()!=null){
                idleInfo.setAddress(URLEncoder.encode(idleInfo.getAddress(),"utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.add_idle);

        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("idleInfo",idleInfo);
        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,IdleInfo.class);
                    if(result.getResult()) {
                        msg.what = PUSH_SUCCESS;
                        msg.obj = result.getData();
                    } else {
                        msg.what = PUSH_ERROR;
                        msg.obj = result.getMsg();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = ERROR;
                } finally {
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void error(String... msg) {
                Message message = handler.obtainMessage();
                message.what = ERRORWITHMESSAGE;
                message.obj = msg;
            }
        });
    }
}
