package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.EvalExtend;
import com.example.amia.schoolrent.Bean.Student;

public interface UserEvalContract {
    interface Presenter{
        void getUserEval(EvalExtend extend, Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void setStudent(Student student);
        void linkError();
    }
}
