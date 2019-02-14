package com.example.amia.schoolrent.Presenter.PersenterImpl;


import android.os.Handler;

import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.RetPasswordContract;
import com.example.amia.schoolrent.Task.StudentTask;

public class ResetPasswordContractImpl implements RetPasswordContract.Presenter {
    private BaseView view;
    private StudentTask studentTask;

    public ResetPasswordContractImpl(BaseView view, StudentTask task) {
        this.view = view;
        this.studentTask = task;
    }

    @Override
    public void forgotPassword(Handler handler) {
        studentTask.resetPassword(view.getContext(),handler);
    }

    @Override
    public void forgotPayPssword(Handler handler) {
        studentTask.resetPayPassword(view.getContext(),handler);
    }
}
