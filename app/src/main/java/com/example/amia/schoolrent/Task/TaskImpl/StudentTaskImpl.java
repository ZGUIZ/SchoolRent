package com.example.amia.schoolrent.Task.TaskImpl;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import com.example.amia.schoolrent.Bean.AuthPicture;
import com.example.amia.schoolrent.Bean.Capital;
import com.example.amia.schoolrent.Bean.KeyValue;
import com.example.amia.schoolrent.Bean.PassWord;
import com.example.amia.schoolrent.Bean.Result;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.NetCallBack;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.StudentTask;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.JSONUtil;
import com.example.amia.schoolrent.Util.NetUtils;
import com.example.amia.schoolrent.Util.RSAUtil;
import com.example.amia.schoolrent.Util.SharedPreferencesUtil;


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
                student.setUserName(URLEncoder.encode(student.getUserName(),"utf-8"));

                //保存账号密码
                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
                Map<String,String> map = new HashMap<>();
                map.put("userName", student.getUserName());
                map.put("password",student.getPassword());
                sharedPreferencesUtil.write("loginInfo",map);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            loginAfterEncode(context,student,handler);
        } else{
            Message msg = handler.obtainMessage();
            msg.what = ERROR;
            handler.sendMessage(msg);
        }
    }

    public void pushMessage(Context context){
        NetUtils.get(ActivityUtil.getString(context, R.string.host) + ActivityUtil.getString(context, R.string.mine_message), new NetCallBack() {
            @Override
            public void finish(String json) {
            }
            @Override
            public void error(String... msg) {
            }
        });
    }

    @Override
    public void sendForgotCode(Context context, String address, final Handler handler) {
        //拼接URL
        String url = ActivityUtil.getString(context,R.string.host) + ActivityUtil.getString(context,R.string.send_forgot_message);

        Student student = new Student();
        student.setEmail(address);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("student",student);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    try {
                        Result result = Result.getJSONObject(json,null);
                        if(result.getResult()) {
                            msg.what = SEND_SUCCESS;
                        } else {
                            msg.what = ERROR;
                            msg.obj = result.getMsg();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        handler.sendMessage(msg);
                    }
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
    public void valdateForgot(Context context, KeyValue keyValue, final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.validate_forgot);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("keyValue",keyValue);
        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,PassWord.class);
                    if(result.getResult()){
                        msg.what = VALIDATE_SUCCESS;
                        msg.obj = result.getData();
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
    public void forgotPassword(Context context, PassWord passWord, final Handler handler) {
        List<KeyValue> keyValues = LitePal.where("key = ?","publicKey").find(KeyValue.class);
        if(keyValues.size()>0) {
            KeyValue keyValue = keyValues.get(0);
            byte[] bytes = Base64.decode(keyValue.getValue(), Base64.DEFAULT);
            //还原公钥
            //RSAUtil.restorePublicKey(bytes);
            PublicKey key = RSAUtil.restorePublicKey(bytes);
            byte[] encodePassword = null;
            try {
                encodePassword = RSAUtil.RSAEncode(key, passWord.getNewPassword().getBytes("utf-8"));
                passWord.setNewPassword(Base64.encodeToString(encodePassword, Base64.DEFAULT));
                encodePassword = RSAUtil.RSAEncode(key, passWord.getConfirmPaswword().getBytes("utf-8"));
                passWord.setConfirmPaswword(Base64.encodeToString(encodePassword, Base64.DEFAULT));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.forgot_password_url);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("passWord",passWord);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getObjectWithList(json,null);
                    if(result.getResult()) {
                        msg.what = MODIFY_PASSWORD;
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
    public void updateMail(Context context, Student student,final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.update_mail);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("student",student);
        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()){
                        msg.what = UPDATE_MAIL;
                        msg.obj = result.getData();
                    } else{
                        msg.what = ERROR;
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
    public void sendSMS(Context context, String telephone, final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.send_sms)+telephone;
        NetUtils.get(url, new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json, null);
                    if(result.getResult()) {
                        msg.what = SEND_SMS;
                        msg.obj = result.getData();
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
    public void updateTelephone(Context context, Student student,final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.update_phone);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("student",student);
        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()){
                        msg.what = UPDATE_PHONE;
                        msg.obj = result.getData();
                    } else{
                        msg.what = ERROR;
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

    protected void loginAfterEncode(Context context,Student student,final Handler handler){
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

            byte[] payPassword = RSAUtil.RSAEncode(key,student.getPayPassword().getBytes("utf-8"));
            byte[] confirmPassword = RSAUtil.RSAEncode(key,student.getConfirmPayPassword().getBytes("utf-8"));
            student.setPayPassword(Base64.encodeToString(payPassword,Base64.DEFAULT));
            student.setConfirmPayPassword(Base64.encodeToString(confirmPassword,Base64.DEFAULT));

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

    @Override
    public void getCurrentUser(Context context, final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.get_current_user);
        NetUtils.get(url, new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getJSONObject(json, Student.class);
                    if (r.getResult()) {
                        Student student = (Student) r.getData();
                        msg.what = CURRENT_USER_SUCCESS;
                        msg.obj = student;
                    } else {
                        msg.what = CURRENT_USER_ERROR;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = CURRENT_USER_ERROR;
                } finally {
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void error(String... msg) {
                Message message = handler.obtainMessage();
                message.what = CURRENT_USER_ERROR;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void updateInfo(Context context, Student student, final Handler handler) {
        try{
            String userName = student.getUserName();
            if(!TextUtils.isEmpty(userName)){
                student.setUserName(URLEncoder.encode(userName,"utf-8"));
            }
            String realName = student.getRealName();
            if(!TextUtils.isEmpty(realName)){
                student.setRealName(URLEncoder.encode(realName,"utf-8"));
            }
            String sex =student.getSex();
            if (!TextUtils.isEmpty(sex)) {
                student.setSex(URLEncoder.encode(sex,"utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.student_update);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("student",student);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()) {
                        msg.what = UPDATE_STUDENT_SUCCESS;
                    } else {
                        msg.what = UPDATE_STUDENT_ERROR;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = UPDATE_STUDENT_ERROR;
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
    public void getBaseInfo(Context context, Student student, final Handler handler) {
        Student s = new Student();
        s.setUserId(student.getUserId());
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.get_base_info);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("student",s);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,Student.class);
                    if(result.getResult()) {
                        msg.what = BASE_INFO_SUCCESS;
                        msg.obj = result.getData();
                    } else {
                       msg.what = BASE_INFO_ERROR;
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
                message.what = ERRORWITHMESSAGE;
                message.obj = msg;
            }
        });
    }

    @Override
    public void exit(Context context) {
        NetUtils.setIsClearSession(true);
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.exit_login);
        NetUtils.get(url, new NetCallBack() {
            @Override
            public void finish(String json) {
            }

            @Override
            public void error(String... msg) {
            }
        });
    }

    @Override
    public void addAuthPicture(Context context, AuthPicture authPicture, final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.add_auth_pic);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("authPicture",authPicture);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()) {
                        msg.what = UPDATE_STUDENT_SUCCESS;
                        msg.obj = result.getData();
                    } else {
                        msg.what = UPDATE_STUDENT_ERROR;
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
                message.what = ERRORWITHMESSAGE;
                message.obj = msg;
            }
        });
    }

    @Override
    public void changePassword(Context context, PassWord passWord,final Handler handler) {
        List<KeyValue> keyValues = LitePal.where("key = ?","publicKey").find(KeyValue.class);
        if(keyValues.size()>0) {
            KeyValue keyValue = keyValues.get(0);
            byte[] bytes = Base64.decode(keyValue.getValue(), Base64.DEFAULT);
            //还原公钥
            //RSAUtil.restorePublicKey(bytes);
            PublicKey key = RSAUtil.restorePublicKey(bytes);
            byte[] encodePassword = null;
            try {
                encodePassword = RSAUtil.RSAEncode(key, passWord.getOldPassword().getBytes("utf-8"));
                passWord.setOldPassword(Base64.encodeToString(encodePassword, Base64.DEFAULT));
                encodePassword = RSAUtil.RSAEncode(key,passWord.getNewPassword().getBytes("utf-8"));
                passWord.setNewPassword(Base64.encodeToString(encodePassword, Base64.DEFAULT));
                encodePassword = RSAUtil.RSAEncode(key,passWord.getConfirmPaswword().getBytes("utf-8"));
                passWord.setConfirmPaswword(Base64.encodeToString(encodePassword, Base64.DEFAULT));
            } catch (Exception e) {
                Toast.makeText(context,R.string.link_error,Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String url = ActivityUtil.getString(context,R.string.host) + ActivityUtil.getString(context,R.string.chang_password);

        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("passWord",passWord);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()) {
                        msg.what = UPDATE_STUDENT_SUCCESS;
                    } else {
                        msg.what = UPDATE_PASSWORD_ERROR;
                    }
                    msg.obj = result.getMsg();
                } catch (Exception e) {
                    e.printStackTrace();
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
    public void changePayPassword(Context context, PassWord passWord, final Handler handler) {

        List<KeyValue> keyValues = LitePal.where("key = ?","publicKey").find(KeyValue.class);
        if(keyValues.size()>0) {
            KeyValue keyValue = keyValues.get(0);
            byte[] bytes = Base64.decode(keyValue.getValue(), Base64.DEFAULT);
            //还原公钥
            //RSAUtil.restorePublicKey(bytes);
            PublicKey key = RSAUtil.restorePublicKey(bytes);
            byte[] encodePassword = null;
            try {
                encodePassword = RSAUtil.RSAEncode(key, passWord.getOldPassword().getBytes("utf-8"));
                passWord.setOldPassword(Base64.encodeToString(encodePassword, Base64.DEFAULT));
                encodePassword = RSAUtil.RSAEncode(key,passWord.getNewPassword().getBytes("utf-8"));
                passWord.setNewPassword(Base64.encodeToString(encodePassword, Base64.DEFAULT));
                encodePassword = RSAUtil.RSAEncode(key,passWord.getConfirmPaswword().getBytes("utf-8"));
                passWord.setConfirmPaswword(Base64.encodeToString(encodePassword, Base64.DEFAULT));
            } catch (Exception e) {
                Toast.makeText(context,R.string.link_error,Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String url = ActivityUtil.getString(context,R.string.host) + ActivityUtil.getString(context,R.string.chang_pay_password);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("passWord",passWord);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()) {
                        msg.what = UPDATE_STUDENT_SUCCESS;
                    } else {
                        msg.what = UPDATE_PAYPASSWORD_ERROR;
                    }
                    msg.obj = result.getMsg();
                } catch (Exception e) {
                    e.printStackTrace();
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
    public void resetPayPassword(Context context,final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.reset_pay_password);
        NetUtils.get(url,new NetCallBack(){
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()) {
                        msg.what = RESET_SUECCESS;
                    } else {
                        msg.what = RESET_ERROR;
                    }
                    msg.obj = result.getMsg();
                } catch (Exception e) {
                    e.printStackTrace();
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
    public void resetPassword(Context context,final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.reset_password);
        NetUtils.get(url,new NetCallBack(){
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()) {
                        msg.what = RESET_SUECCESS;
                    } else {
                        msg.what = RESET_ERROR;
                    }
                    msg.obj = result.getMsg();
                } catch (Exception e) {
                    e.printStackTrace();
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
    public void getMyCapital(Context context, final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.get_capital);
        NetUtils.get(url,new NetCallBack(){
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json, Capital.class);
                    if(result.getResult()) {
                        msg.what = GET_CAPITAL;
                        msg.obj = result.getData();
                    } else {
                        msg.what = ERROR;
                        msg.obj = result.getMsg();
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
                message.what = ERRORWITHMESSAGE;
                message.obj = msg;
            }
        });
    }

}
