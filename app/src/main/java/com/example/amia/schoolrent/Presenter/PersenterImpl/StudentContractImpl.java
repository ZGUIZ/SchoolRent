package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.KeyValue;
import com.example.amia.schoolrent.Presenter.StudentContract;
import com.example.amia.schoolrent.Task.StudentTask;

public class StudentContractImpl implements StudentContract.Presenter {
    public static final int ERROR_WITH_MESSAGE=-2;
    public static final int ERROR = -1;
    public static final int SEND_SUCCESS = 0;

    protected StudentContract.View view;
    protected StudentTask task;

    public StudentContractImpl(StudentContract.View view, StudentTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void sendRegisterMail(String address, Handler handler) {
        task.sendValidateMail(view.getContext(),address,handler);
    }

    @Override
    public void validateMail(KeyValue keyValue, Handler handler) {
        task.validateMail(view.getContext(),keyValue,handler);
    }
}
