package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.BaseInfoContract;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Task.StudentTask;


public class BaseInfoContractImpl implements BaseInfoContract.Presenter {

    private BaseView view;
    private StudentTask studentTask;

    public BaseInfoContractImpl(BaseView view, StudentTask studentTask) {
        this.view = view;
        this.studentTask = studentTask;
    }

    @Override
    public void loadBaseInfo(Context context, Student student, Handler handler) {
        studentTask.getBaseInfo(context,student,handler);
    }

    @Override
    public void updateStudentInfo(Context context, Student student, Handler handler) {
        studentTask.updateInfo(context,student,handler);
    }

    @Override
    public void exit() {
        studentTask.exit(view.getContext());
    }
}
