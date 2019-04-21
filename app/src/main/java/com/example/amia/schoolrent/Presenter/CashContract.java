package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.CapitalCash;


public interface CashContract {
    interface Presenter{
        void addCash(CapitalCash capitalCash, Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
    }
}
