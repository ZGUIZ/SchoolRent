package com.example.amia.schoolrent.Presenter;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.ResponseInfo;
import com.example.amia.schoolrent.Bean.SecondResponseInfo;

public interface IdleInfoContract {
    interface Presenter{
        void addRefuse(Context context, ResponseInfo responseInfo, Handler handler);
        void addSecondRefuse(Context context, SecondResponseInfo secondResponseInfo, Handler handler);
        void getReufseList(Context context, IdleInfo idleInfo, Handler handler);
        void getRelation(IdleInfo idleInfo,Handler handler);
        void addRent(Rent rent, Handler handler);
        void getRentList(IdleInfo idleInfo,Handler handler);
        void agreeRent(Rent rent,Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
        void setPresenter(IdleInfoContract.Presenter presenter);
    }
}
