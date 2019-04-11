package com.example.amia.schoolrent.Presenter;


import android.os.Handler;
import android.os.Message;

public interface MyMessageContract {
    interface Presenter{
        void getMyMessage(Handler handler);
        void requestPush(Handler handler);
        void delMessage(com.example.amia.schoolrent.Bean.Message message,Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
        void setPresenter(Presenter presenter);
    }
}
