package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.Complain;

public interface OtherComplainContract {
    interface Presenter{
        void addComplain(Complain otherComplain, Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
        void setPresenter(Presenter presenter);
    }
}
