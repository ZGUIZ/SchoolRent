package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.KeyValue;
import com.example.amia.schoolrent.Bean.PassWord;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.ForgotPassContract;
import com.example.amia.schoolrent.Task.StudentTask;


public class ForgotPassContractImpl implements ForgotPassContract.Presenter {

    private BaseView view;
    private StudentTask task;

    public ForgotPassContractImpl(BaseView view, StudentTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void sendMail( String address, Handler handler) {
        task.sendForgotCode(view.getContext(),address,handler);
    }

    @Override
    public void validateCode(KeyValue keyValue, Handler handler) {
        task.valdateForgot(view.getContext(),keyValue,handler);
    }

    @Override
    public void modifyPassword(PassWord passWord, Handler handler) {
        task.forgotPassword(view.getContext(),passWord,handler);
    }
}
