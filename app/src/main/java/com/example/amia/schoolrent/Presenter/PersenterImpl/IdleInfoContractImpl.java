package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.ResponseInfo;
import com.example.amia.schoolrent.Bean.SecondResponseInfo;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.IdleInfoContract;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.RefuseTask;

public class IdleInfoContractImpl implements IdleInfoContract.Presenter {
    private BaseView view;
    private RefuseTask refuseTask;
    private IdleTask idleTask;

    public IdleInfoContractImpl(BaseView view, RefuseTask refuseTask,IdleTask idleTask) {
        this.view = view;
        this.refuseTask = refuseTask;
        this.idleTask = idleTask;
    }

    @Override
    public void addRefuse(Context context, ResponseInfo responseInfo, Handler handler) {
        refuseTask.addRefuse(context,responseInfo,handler);
    }

    @Override
    public void addSecondRefuse(Context context, SecondResponseInfo secondResponseInfo, Handler handler) {
        refuseTask.addSecondRefuse(context,secondResponseInfo,handler);
    }

    @Override
    public void getReufseList(Context context, IdleInfo idleInfo, Handler handler) {
        refuseTask.getReufseList(context,idleInfo,handler);
    }

    @Override
    public void getRelation(IdleInfo idleInfo, Handler handler) {
        idleTask.getRelation(view.getContext(),idleInfo,handler);
    }

    @Override
    public void addRent(Rent rent, Handler handler) {
        idleTask.addRent(view.getContext(),rent,handler);
    }

    @Override
    public void getRentList(IdleInfo idleInfo, Handler handler) {
        idleTask.getIdleRentList(view.getContext(),idleInfo,handler);
    }

    @Override
    public void agreeRent(Rent rent, Handler handler) {
        idleTask.agree(view.getContext(),rent,handler);
    }

    @Override
    public void disagreeRent(Rent rent, Handler handler) {
        idleTask.disagreeRent(view.getContext(),rent,handler);
    }
}
