package com.example.amia.schoolrent.Task.TaskImpl;

import android.content.Context;
import android.util.Base64;

import com.example.amia.schoolrent.Bean.KeyValue;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.LoginContract;
import com.example.amia.schoolrent.Presenter.NetCallBack;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.StudentTask;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.JSONUtil;
import com.example.amia.schoolrent.Util.NetUtils;
import com.example.amia.schoolrent.Util.RSAUtil;

import org.json.JSONException;
import org.litepal.LitePal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StudentTaskImpl implements StudentTask {
    @Override
    public void login(Context context, Student student, final LoginContract.CallBack callBack) {
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
                student.setPassword(new String(encodePassword,"utf-8"));
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
                    if(json== null || "".equals(json.trim())){
                        callBack.passwordError();
                    }
                    Student s = null;
                    try {
                        s = (Student) JSONUtil.getObject(Student.class,json);
                        callBack.toListPage(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                        error("json转换异常");
                    }
                }

                @Override
                public void error(String... msg) {
                    callBack.netLinkError();
                }
            });
        } else{
            callBack.netLinkError();
        }
    }
}
