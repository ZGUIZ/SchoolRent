package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

public interface RetPasswordContract {
    interface Presenter{
        void forgotPassword(Handler handler);
        void forgotPayPssword(Handler handler);
    }

    interface View extends BaseView<Presenter>{
    }
}
