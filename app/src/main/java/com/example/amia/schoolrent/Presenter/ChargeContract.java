package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.Charge;

public interface ChargeContract {
    interface Presenter{
        void addCharge(Charge capitalCash, Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void addPic(String path);
        void linkError();
    }
}
