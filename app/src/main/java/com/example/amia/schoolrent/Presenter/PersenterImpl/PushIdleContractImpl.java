package com.example.amia.schoolrent.Presenter.PersenterImpl;

import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.PushIdleContract;
import com.example.amia.schoolrent.Task.IdleTask;

public class PushIdleContractImpl implements PushIdleContract.Presenter {
    private BaseView view;
    private IdleTask task;

    public PushIdleContractImpl(BaseView view, IdleTask task) {
        this.view = view;
        this.task = task;
    }

}
