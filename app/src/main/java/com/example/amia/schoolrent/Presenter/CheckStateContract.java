package com.example.amia.schoolrent.Presenter;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.CheckStatementExtend;

public interface CheckStateContract {
    interface Presenter{
        void queryMyCheck(CheckStatementExtend extend,Handler handler);
        void getMyCapital(Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
    }
}
