package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.IdleInfoExtend;

public interface MyPushContract {
    interface Presenter{
       void loadMyPush(IdleInfoExtend idleInfoExtend, Handler handler);
       void closeIdle(IdleInfo idleInfo,Handler handler);
       void cancelRent(IdleInfo idleInfo,Handler handler);
       void delIdle(IdleInfo idleInfo,Handler handler);
       void startRent(IdleInfo rent, Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
    }
}
