package com.example.amia.schoolrent.Task.TaskImpl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.ResponseInfo;
import com.example.amia.schoolrent.Bean.Result;
import com.example.amia.schoolrent.Presenter.NetCallBack;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.RefuseTask;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.NetUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.example.amia.schoolrent.Task.IdleTask.ERRORWITHMESSAGE;

public class RefuseTaskImpl implements RefuseTask {
    @Override
    public void addRefuse(Context context, ResponseInfo responseInfo,final Handler handler) {
        try {
            responseInfo.setResponseInfo(URLEncoder.encode(responseInfo.getResponseInfo(),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = ActivityUtil.getString(context, R.string.host)+ActivityUtil.getString(context,R.string.add_response_info);

        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("responseInfo",responseInfo);
        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()) {
                        msg.what = PUSH_REFUSE_SUCCESS;
                    } else {
                        msg.what = PUSH_REFUSE_ERROR;
                        msg.obj = result.getMsg();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = PUSH_REFUSE_ERROR;
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

    @Override
    public void getReufseList(Context context, IdleInfo idleInfo, final Handler handler) {
        String url = ActivityUtil.getString(context, R.string.host)+ActivityUtil.getString(context,R.string.get_response_info);

        Map<String,Object> keyValueMap = new HashMap<>();
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setInfoId(idleInfo.getInfoId());
        keyValueMap.put("responseInfo",responseInfo);
        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getObjectWithList(json,ResponseInfo.class);
                    if(result.getResult()) {
                        msg.what = LOAD_REFUSE_SUCCESS;
                    } else {
                        msg.what = LOAD_REFUSE_ERROR;
                        msg.obj = result.getMsg();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = LOAD_REFUSE_ERROR;
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
