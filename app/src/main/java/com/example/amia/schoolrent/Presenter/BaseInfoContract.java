package com.example.amia.schoolrent.Presenter;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.Student;


public interface BaseInfoContract {
    interface Presenter{
        void loadBaseInfo(Context context, Student student, Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
        void setPresenter(BaseInfoContract.Presenter presenter);
    }
}
