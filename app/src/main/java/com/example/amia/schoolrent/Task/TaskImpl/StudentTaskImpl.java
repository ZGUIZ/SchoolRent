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
import com.example.amia.schoolrent.Util.JSONUtil;
import com.example.amia.schoolrent.Util.NetUtils;
import com.example.amia.schoolrent.Util.RSAUtil;


import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.ERROR_WITH_MESSAGE;
import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.REGISTER_ERROR;
import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.REGISTER_SUCCESS;
import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.SEND_SUCCESS;
import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.VALIDATE_ERROR;
import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.VALIDATE_SUCCESS;
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
            //RSAUtil.restorePublicKey(bytes);
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

    @Override
    public void sendValidateMail(Context context, String address, final Handler handler) {
        //拼接URL
        StringBuffer sb = new StringBuffer(ActivityUtil.getString(context,R.string.host));
        sb.append(ActivityUtil.getString(context,R.string.send_mail_validate)).append(address);

        NetUtils.get(sb.toString(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    boolean result = jsonObject.getBoolean("result");
                    if(result){
                        msg.what = SEND_SUCCESS;
                        msg.obj = jsonObject.getString("msg");
                    } else {
                        msg.what = ERROR_WITH_MESSAGE;
                        msg.obj = jsonObject.getString("msg");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void error(String... msg) {
                Message message = handler.obtainMessage();
                message.what = ERROR_WITH_MESSAGE;
                message.obj = msg;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void validateMail(Context context, KeyValue keyValue, final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.mail_validate);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("keyValue",keyValue);
        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()){
                        msg.what = VALIDATE_SUCCESS;
                    } else{
                        msg.what = VALIDATE_ERROR;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void error(String... msg) {
                Message message = handler.obtainMessage();
                message.what = ERROR_WITH_MESSAGE;
                message.obj = msg;
            }
        });
    }

    @Override
    public void register(Context context, Student student, final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.student_register);
        List<KeyValue> keyValues = LitePal.where("key = ?","publicKey").find(KeyValue.class);
        KeyValue keyValue = keyValues.get(0);
        byte[] bytes = Base64.decode(keyValue.getValue(), Base64.DEFAULT);
        //还原公钥
        //RSAUtil.restorePublicKey(bytes);
        PublicKey key = RSAUtil.restorePublicKey(bytes);
        byte[] encodePassword = null;
        try {
            //加密密码
            encodePassword = RSAUtil.RSAEncode(key,student.getPassword().getBytes("utf-8"));
            student.setPassword(Base64.encodeToString(encodePassword,Base64.DEFAULT));
            //加密确认密码
            encodePassword = RSAUtil.RSAEncode(key,student.getConfirmPassword().getBytes("utf-8"));
            student.setConfirmPassword(Base64.encodeToString(encodePassword,Base64.DEFAULT));

            //用户名编码
            student.setUserName(URLEncoder.encode(student.getUserName(),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("student",student);
        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,Student.class);
                    if(result.getResult()) {
                        msg.what = REGISTER_SUCCESS;
                        msg.obj = result.getData();
                    } else {
                        msg.what = REGISTER_ERROR;
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
                message.what = ERROR_WITH_MESSAGE;
                message.obj = msg;
            }
        });
    }
}
