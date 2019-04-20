package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.ModifyPhoneContract;
import com.example.amia.schoolrent.Task.StudentTask;

public class ModifyPhoneContractImpl implements ModifyPhoneContract.Presenter {

    private BaseView view;
    private StudentTask task;

    public ModifyPhoneContractImpl(BaseView view, StudentTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void sendCode(String phone, Handler handler) {
        task.sendSMS(view.getContext(),phone,handler);
    }
}
