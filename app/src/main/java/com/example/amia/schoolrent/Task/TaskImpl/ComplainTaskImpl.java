package com.example.amia.schoolrent.Task.TaskImpl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.amia.schoolrent.Bean.Complain;
import com.example.amia.schoolrent.Bean.Result;
import com.example.amia.schoolrent.Presenter.NetCallBack;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.ComplainTask;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.NetUtils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Task.SchoolTask.ERRORWITHMESSAGE;


public class ComplainTaskImpl implements ComplainTask {
    @Override
    public void addComplain(Context context, Complain complain, final Handler handler) {
        try {
            complain.setMsg(URLEncoder.encode(complain.getMsg(), "utf-8"));
        } catch (Exception e){
            e.printStackTrace();
        }

        String url = ActivityUtil.getString(context, R.string.host)+ActivityUtil.getString(context,R.string.add_other_complain);

        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("complain",complain);
        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()) {
                        msg.what = PUSH_SUCCESS;
                    } else {
                        msg.what = ERROR;
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
