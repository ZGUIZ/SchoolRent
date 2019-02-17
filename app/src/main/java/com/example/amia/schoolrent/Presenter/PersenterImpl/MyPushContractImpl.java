package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfoExtend;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.MyPushContract;
import com.example.amia.schoolrent.Task.IdleTask;

public class MyPushContractImpl implements MyPushContract.Presenter {
    private BaseView view;
    private IdleTask task;

    public MyPushContractImpl(BaseView view, IdleTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void loadMyPush(IdleInfoExtend idleInfoExtend, Handler handler) {
        task.getMyPushList(view.getContext(),idleInfoExtend,handler);
    }
}
