package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.ModifyMailContract;
import com.example.amia.schoolrent.Task.StudentTask;

public class ModifyMailContractImpl implements ModifyMailContract.Presenter {

    private BaseView view;
    private StudentTask task;

    public ModifyMailContractImpl(BaseView view, StudentTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void sendCode(String mail, Handler handler) {
        task.sendValidateMail(view.getContext(),mail,handler);
    }
}
