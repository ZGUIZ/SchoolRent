package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.OrderComplian;


public interface ComplainContract {
    interface Presenter{
        void addComplain(OrderComplian orderComplian, Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void addPic(String path);
        void linkError();
        void setPresenter(Presenter presenter);
    }
}
