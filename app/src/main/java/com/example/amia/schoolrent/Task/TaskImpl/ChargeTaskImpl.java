package com.example.amia.schoolrent.Task.TaskImpl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.amia.schoolrent.Bean.CapitalCash;
import com.example.amia.schoolrent.Bean.Charge;
import com.example.amia.schoolrent.Bean.Result;
import com.example.amia.schoolrent.Presenter.NetCallBack;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.ChargeTask;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.NetUtils;

import java.util.HashMap;
import java.util.Map;

import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Task.SchoolTask.ERRORWITHMESSAGE;

public class ChargeTaskImpl implements ChargeTask {

    @Override
    public void addCharge(Context context, Charge charge, final Handler handler) {
        String url = ActivityUtil.getString(context, R.string.host)+ActivityUtil.getString(context,R.string.add_charge);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("charge",charge);
        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()) {
                        msg.what = ADD_CHARGE;
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

    @Override
    public void addCapitalCash(Context context, CapitalCash capitalCash, final Handler handler) {
        String url = ActivityUtil.getString(context, R.string.host)+ActivityUtil.getString(context,R.string.add_capitalCash);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("capitalCash",capitalCash);
        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()) {
                        msg.what = ADD_CAPITALCASH;
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
