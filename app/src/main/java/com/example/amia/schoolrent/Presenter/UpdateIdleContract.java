package com.example.amia.schoolrent.Presenter;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.Bean.IdleInfo;

public interface UpdateIdleContract {
    interface Presenter{
       void updateIdleInfo(IdleInfo idleInfo,Handler handler);
       void getClassifyName(Classify classify, Handler handler);
       void getAllClassify(Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
        void setPresenter(Presenter loginPresenter);
        void addPic(String path);
    }
}
