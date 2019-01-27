package com.example.amia.schoolrent.Task.TaskImpl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;

import com.example.amia.schoolrent.Bean.KeyValue;
import com.example.amia.schoolrent.Bean.Result;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.NetCallBack;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.StudentTask;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.NetUtils;
import com.example.amia.schoolrent.Util.RSAUtil;

import org.litepal.LitePal;

import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl.ERROR;
import static com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl.ERRORWITHMESSAGE;
import static com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl.LOGINSUCCESS;
import static com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl.PASSWORDERROR;


public class StudentTaskImpl implements StudentTask {
    @Override
    public void login(Context context, Student student, final Handler handler) {
        String password=student.getPassword();
        List<KeyValue> keyValues = LitePal.where("key = ?","publicKey").find(KeyValue.class);
        if(keyValues.size()>0){
            KeyValue keyValue = keyValues.get(0);
            byte[] bytes = Base64.decode(keyValue.getValue(), Base64.DEFAULT);
            //还原公钥
            RSAUtil.restorePublicKey(bytes);
            PublicKey key = RSAUtil.restorePublicKey(bytes);
            byte[] encodePassword = null;
            try {
                encodePassword = RSAUtil.RSAEncode(key,password.getBytes("utf-8"));
                student.setPassword(Base64.encodeToString(encodePassword,Base64.DEFAULT));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Map<String,Object> param = new HashMap<>();
            param.put("student",student);
            String url=ActivityUtil.getString(context, R.string.host)+ActivityUtil.getString(context,R.string.login_web);
            //登录请求
            NetUtils.doPost(url, param, new HashMap<String, String>(), new NetCallBack() {
                @Override
                public void finish(String json) {
                    Message msg = handler.obtainMessage();
                    if(json== null || "".equals(json.trim())){
                        //callBack.passwordError();
                        msg.what = PASSWORDERROR;
                        handler.sendMessage(msg);
                        return;
                    }
                    try {
                        Result result = Result.getJSONObject(json.trim(),Student.class);
                        if(result.getResult()){
                            msg.what = LOGINSUCCESS;
                            msg.obj = result.getData();
                        } else {
                            msg.what = PASSWORDERROR;
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
                    handler.sendMessage(message);
                }
            });
        } else{
            Message msg = handler.obtainMessage();
            msg.what = ERROR;
            handler.sendMessage(msg);
        }
    }
}
