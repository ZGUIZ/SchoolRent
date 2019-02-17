package com.example.amia.schoolrent.Presenter;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.IdleInfoExtend;

public interface MyPushContract {
    interface Presenter{
       void loadMyPush(IdleInfoExtend idleInfoExtend, Handler handler);
       void closeIdle(IdleInfo idleInfo,Handler handler);
       void cancelRent(IdleInfo idleInfo,Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
    }
}
