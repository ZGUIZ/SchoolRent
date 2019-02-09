package com.example.amia.schoolrent.Presenter;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Student;


public interface PushIdleContract {
    interface Presenter{
        void uploadFile(Context context, Student student, String url, Handler handler);
        void getAllClassify(Context context,Handler handler);
        void pushIdle(Context context, IdleInfo idleInfo,Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void addPic(String uri);
        void leavePage();
    }
}
