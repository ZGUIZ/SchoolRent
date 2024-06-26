package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.example.amia.schoolrent.Bean.AuthPicture;
import com.example.amia.schoolrent.Bean.KeyValue;
import com.example.amia.schoolrent.Bean.PassWord;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.LoginContract;

public interface StudentTask extends BaseTask{
    int CURRENT_USER_ERROR = 200;
    int CURRENT_USER_SUCCESS = 201;
    int BASE_INFO_SUCCESS = 203;
    int BASE_INFO_ERROR = 204;
    int UPDATE_STUDENT_SUCCESS =205;
    int UPDATE_STUDENT_ERROR =206;
    int UPDATE_PASSWORD_ERROR = 207;
    int UPDATE_PAYPASSWORD_ERROR = 208;
    int RESET_SUECCESS = 209;
    int RESET_ERROR = 210;
    int GET_CAPITAL = 211;
    int MODIFY_PASSWORD = 212;
    int UPDATE_MAIL = 213;
    int UPDATE_PHONE = 214;
    int SEND_SMS = 215;

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

    void addAuthPicture(Context context, AuthPicture authPicture,Handler handler);

    void changePassword(Context context, PassWord passWord,Handler handler);

    void changePayPassword(Context context,PassWord passWord,Handler handler);

    void resetPayPassword(Context context,Handler handler);
    void resetPassword(Context context,Handler handler);
    void getMyCapital(Context context,Handler handler);

    /**
     * 获取推送信息
     */
    void pushMessage(Context context);

    /**
     * 忘记密码验证邮箱
     */
    void sendForgotCode(Context context,String address,Handler handler);
    void valdateForgot(Context context,KeyValue keyValue,Handler handler);
    void forgotPassword(Context context,PassWord passWord,Handler handler);

    /**
     * 更改邮箱
     */
    void updateMail(Context context,Student student,Handler handler);

    void sendSMS(Context context,String telephone,Handler handler);
    void updateTelephone(Context context,Student student,Handler handler);
}
