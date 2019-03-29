package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfoExtend;
import com.example.amia.schoolrent.Bean.Student;

public interface UserIdleContract {
    interface Presenter{
        void getUserIdle(IdleInfoExtend extend,Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void setStudent(Student student);
        void linkError();
    }
}
