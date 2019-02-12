package com.example.amia.schoolrent.Presenter;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.ResponseInfo;

public interface IdleInfoContract {
    interface Presenter{
        void addRefuse(Context context, ResponseInfo responseInfo, Handler handler);
        void getReufseList(Context context, IdleInfo idleInfo, Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
        void setPresenter(IdleInfoContract.Presenter presenter);
    }
}
