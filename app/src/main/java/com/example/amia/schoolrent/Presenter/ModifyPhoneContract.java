package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.Student;

public interface ModifyPhoneContract {
    interface Presenter{
        void sendCode(String phone, Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
        Student getFill();
        void Snack(String msg);
        void Snack(int msg);
    }
}
