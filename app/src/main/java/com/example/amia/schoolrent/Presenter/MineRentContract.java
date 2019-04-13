package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.Eval;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.RentExtend;

import java.io.UnsupportedEncodingException;

public interface MineRentContract {
    interface Presenter{
        void getMyRent(RentExtend rentExtend,Handler handler,int flag);
        void cancleRent(Rent rent, Handler handler);
        void startRent(Rent rent,Handler handler);
        void cancelRent(Rent rent,Handler handler);
        void findById(String id,Handler handler);
        void delRent(Rent rent,Handler handler);
        void eval(Eval eval, Handler handler) throws UnsupportedEncodingException;
        void addDestroy(IdleInfo idleInfo,Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
    }
}
