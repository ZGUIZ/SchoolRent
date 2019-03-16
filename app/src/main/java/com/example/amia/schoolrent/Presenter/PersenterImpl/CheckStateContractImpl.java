package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.CheckStatementExtend;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.CheckStateContract;
import com.example.amia.schoolrent.Task.CheckStatementTask;
import com.example.amia.schoolrent.Task.StudentTask;

public class CheckStateContractImpl implements CheckStateContract.Presenter {

    private BaseView view;
    private CheckStatementTask task;
    private StudentTask studentTask;

    public CheckStateContractImpl(BaseView view, CheckStatementTask task,StudentTask studentTask) {
        this.view = view;
        this.task = task;
        this.studentTask = studentTask;
    }

    @Override
    public void queryMyCheck(CheckStatementExtend extend, Handler handler) {
        task.getCheckStatement(view.getContext(),extend,handler);
    }

    @Override
    public void getMyCapital(Handler handler) {
        studentTask.getMyCapital(view.getContext(),handler);
    }
}
