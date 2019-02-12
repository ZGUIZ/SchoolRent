package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.ResponseInfo;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.IdleInfoContract;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.RefuseTask;

public class IdleInfoContractImpl implements IdleInfoContract.Presenter {
    private BaseView view;
    private RefuseTask refuseTask;

    public IdleInfoContractImpl(BaseView view, RefuseTask refuseTask) {
        this.view = view;
        this.refuseTask = refuseTask;
    }

    @Override
    public void addRefuse(Context context, ResponseInfo responseInfo, Handler handler) {
        refuseTask.addRefuse(context,responseInfo,handler);
    }

    @Override
    public void getReufseList(Context context, IdleInfo idleInfo, Handler handler) {
        refuseTask.getReufseList(context,idleInfo,handler);
    }
}
