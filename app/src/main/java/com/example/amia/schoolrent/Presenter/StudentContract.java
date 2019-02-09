package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.KeyValue;
import com.example.amia.schoolrent.Bean.Student;

public interface StudentContract {
    interface Presenter{
        /**
         * 发送验证邮件
         * @param address
         * @param handler
         */
        void sendRegisterMail(String address, Handler handler);

        /**
         * 对比验证信息
         * @param keyValue
         * @param handler
         */
        void validateMail(KeyValue keyValue,Handler handler);

        /**
         * 学生注册
         * @param student
         * @param handler
         */
        void register(Student student,Handler handler);

        void getCurrentUser(Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
        void setPresenter(StudentContract.Presenter presenter,LoginContract.Presenter loginPresenter);
    }
}
