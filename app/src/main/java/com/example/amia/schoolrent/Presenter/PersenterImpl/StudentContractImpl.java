package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.KeyValue;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.StudentContract;
import com.example.amia.schoolrent.Task.StudentTask;

public class StudentContractImpl implements StudentContract.Presenter {
    public static final int ERROR_WITH_MESSAGE=-2;
    public static final int ERROR = -1;
    public static final int SEND_SUCCESS = 0;
    public static final int VALIDATE_SUCCESS = 1;
    public static final int VALIDATE_ERROR = 2;
    public static final int REGISTER_SUCCESS = 3;
    public static final int REGISTER_ERROR = 4;

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

    @Override
    public void register(Student student, Handler handler) {
        task.register(view.getContext(),student,handler);
    }
}
