package com.example.amia.schoolrent.Presenter;

import android.os.Message;

public interface SplashContract {
    interface Presenter{
        void onStart(SplashContract.CallBack callBack) throws Exception;
    }

    interface View extends BaseView<Presenter>{
        void setPresenter(SplashContract.Presenter presenter,StudentContract.Presenter studentPresenter);
    }

    interface CallBack{
        void setStatus(Message msg);
    }
}
