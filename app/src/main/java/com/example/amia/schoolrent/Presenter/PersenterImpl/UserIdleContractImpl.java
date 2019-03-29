package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfoExtend;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.UserIdleContract;
import com.example.amia.schoolrent.Task.IdleTask;

public class UserIdleContractImpl implements UserIdleContract.Presenter {

    private BaseView view;
    private IdleTask task;

    public UserIdleContractImpl(BaseView view, IdleTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void getUserIdle(IdleInfoExtend extend, Handler handler) {
        task.getUserPush(view.getContext(),extend,handler);
    }
}
