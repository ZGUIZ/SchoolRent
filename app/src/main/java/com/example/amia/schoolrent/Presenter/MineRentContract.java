package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.RentExtend;

public interface MineRentContract {
    interface Presenter{
        void getMyRent(RentExtend rentExtend,Handler handler,int flag);
        void cancleRent(Rent rent, Handler handler);
        void startRent(Rent rent,Handler handler);
        void cancelRent(Rent rent,Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
    }
}
