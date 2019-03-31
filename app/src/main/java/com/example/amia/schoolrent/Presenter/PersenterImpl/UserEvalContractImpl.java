package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.EvalExtend;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.UserEvalContract;
import com.example.amia.schoolrent.Task.IdleTask;

public class UserEvalContractImpl implements UserEvalContract.Presenter {

    private BaseView view;
    private IdleTask task;

    public UserEvalContractImpl(BaseView view, IdleTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void getUserEval(EvalExtend extend, Handler handler) {
        task.getEval(view.getContext(),extend,handler);
    }
}
