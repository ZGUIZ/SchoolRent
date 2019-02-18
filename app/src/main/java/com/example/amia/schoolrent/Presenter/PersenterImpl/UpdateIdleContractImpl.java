package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.UpdateIdleContract;
import com.example.amia.schoolrent.Task.IdleTask;

public class UpdateIdleContractImpl implements UpdateIdleContract.Presenter {

    private BaseView view;
    private IdleTask idleTask;

    public UpdateIdleContractImpl(BaseView view, IdleTask idleTask) {
        this.view = view;
        this.idleTask = idleTask;
    }

    @Override
    public void updateIdleInfo(IdleInfo idleInfo, Handler handler) {
        idleTask.updateIdleInfo(view.getContext(),idleInfo,handler);
    }
}
