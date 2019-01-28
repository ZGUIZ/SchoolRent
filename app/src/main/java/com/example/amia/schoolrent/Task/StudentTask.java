package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.example.amia.schoolrent.Bean.KeyValue;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.LoginContract;

public interface StudentTask {
    /**
     * 登录
     * @param context
     * @param student
     * @param handler
     */
    void login(Context context, Student student, Handler handler);

    /**
     * 发送验证邮件
     * @param address
     * @param handler
     */
    void sendValidateMail(Context context,String address,Handler handler);

    /**
     * 验证邮件
     * @param keyValue
     * @param handler
     */
    void validateMail(Context context,KeyValue keyValue,Handler handler);
}
