package com.example.amia.schoolrent.Presenter;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfo;

public interface UpdateIdleContract {
    interface Presenter{
       void updateIdleInfo(IdleInfo idleInfo,Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
        void setPresenter(Presenter loginPresenter);
    }
}
