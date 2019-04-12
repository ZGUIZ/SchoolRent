package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfoExtend;


public interface ClassifyContract {
    interface Presenter{
        void queryClassify(Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
    }
}
