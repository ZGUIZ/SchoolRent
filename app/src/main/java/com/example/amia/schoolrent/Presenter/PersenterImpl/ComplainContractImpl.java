package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.OrderComplian;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.ComplainContract;
import com.example.amia.schoolrent.Task.IdleTask;


public class ComplainContractImpl implements ComplainContract.Presenter {

    private BaseView view;
    private IdleTask task;

    public ComplainContractImpl(BaseView view, IdleTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void addComplain(OrderComplian orderComplian, Handler handler) {
        task.addComplain(view.getContext(),orderComplian,handler);
    }
}
