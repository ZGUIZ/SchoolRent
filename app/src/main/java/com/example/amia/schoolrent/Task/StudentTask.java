package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.example.amia.schoolrent.Bean.KeyValue;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.LoginContract;

public interface StudentTask extends BaseTask{
    int CURRENT_USER_ERROR = 200;
    int CURRENT_USER_SUCCESS = 201;
    int BASE_INFO_SUCCESS = 203;
    int BASE_INFO_ERROR = 204;
    int UPDATE_STUDENT_SUCCESS =205;
    int UPDATE_STUDENT_ERROR =206;

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

    /**
     * 账号注册
     * @param context
     * @param student
     * @param handler
     */
    void register(Context context,Student student,Handler handler);

    void getCurrentUser(Context context,Handler handler);

    void updateInfo(Context context,Student student,Handler handler);

    void getBaseInfo(Context context,Student student,Handler handler);

    void exit(Context context);
}
