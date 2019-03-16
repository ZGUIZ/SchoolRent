package com.example.amia.schoolrent.Task.TaskImpl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.amia.schoolrent.Bean.CheckStatement;
import com.example.amia.schoolrent.Bean.CheckStatementExtend;
import com.example.amia.schoolrent.Bean.Result;
import com.example.amia.schoolrent.Presenter.NetCallBack;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.CheckStatementTask;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.NetUtils;

import java.util.HashMap;
import java.util.Map;

import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Task.SchoolTask.ERRORWITHMESSAGE;

public class CheckStatementTaskImpl implements CheckStatementTask {
    @Override
    public void getCheckStatement(Context context, CheckStatementExtend extend, final Handler handler) {
        String url = ActivityUtil.getString(context, R.string.host)+ActivityUtil.getString(context,R.string.get_check);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("check",extend);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack()  {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json, CheckStatement.class);
                    if(r.getResult()){
                        msg.what = CHECK_LIST;
                        msg.obj = r.getData();
                    } else {
                        msg.what = ERROR;
                        msg.obj = r.getMsg();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = ERROR;
                    msg.obj = e.getMessage();
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
}
