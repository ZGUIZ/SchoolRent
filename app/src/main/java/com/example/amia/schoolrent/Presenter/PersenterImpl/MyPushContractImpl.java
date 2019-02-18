package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfo;
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

    @Override
    public void closeIdle(IdleInfo idleInfo, Handler handler) {
        task.closeIdle(view.getContext(),idleInfo,handler);
    }

    @Override
    public void cancelRent(IdleInfo idleInfo, Handler handler) {
        task.cancelRent(view.getContext(),idleInfo,handler);
    }

    @Override
    public void delIdle(IdleInfo idleInfo, Handler handler) {
        task.delIdleInfo(view.getContext(),idleInfo,handler);
    }
}
