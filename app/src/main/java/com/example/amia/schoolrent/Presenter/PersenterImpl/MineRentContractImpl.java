package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.RentExtend;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.MineRentContract;
import com.example.amia.schoolrent.Task.IdleTask;

public class MineRentContractImpl implements MineRentContract.Presenter {

    private BaseView view;
    private IdleTask task;

    public MineRentContractImpl(BaseView view, IdleTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void getMyRent(RentExtend rentExtend, Handler handler,int flag) {
        task.loadMineRent(view.getContext(),rentExtend,handler,flag);
    }

    @Override
    public void cancleRent(Rent rent, Handler handler) {
        IdleInfo idleInfo = new IdleInfo();
        idleInfo.setInfoId(rent.getIdelId());
        task.cancelRent(view.getContext(),idleInfo,handler);
    }

    @Override
    public void startRent(Rent rent, Handler handler) {
        task.startRent(view.getContext(),rent,handler);
    }

    @Override
    public void cancelRent(Rent rent, Handler handler) {
        task.cancelRent(view.getContext(),rent,handler);
    }

    @Override
    public void findById(String id, Handler handler) {
        task.findIdleById(view.getContext(),id,handler);
    }
}
