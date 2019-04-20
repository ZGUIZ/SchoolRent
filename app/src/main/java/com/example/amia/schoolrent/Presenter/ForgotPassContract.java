package com.example.amia.schoolrent.Presenter;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.KeyValue;
import com.example.amia.schoolrent.Bean.PassWord;

public interface ForgotPassContract {
    interface Presenter{
        void sendMail(String address, Handler handler);
        void validateCode( KeyValue keyValue,Handler handler);
        void modifyPassword(PassWord passWord,Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
        void setPresenter(Presenter presenter);
    }
}
