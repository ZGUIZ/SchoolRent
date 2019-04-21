package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.Charge;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.ChargeContract;
import com.example.amia.schoolrent.Task.ChargeTask;

public class ChargeContractImpl implements ChargeContract.Presenter {

    private BaseView view;
    private ChargeTask task;

    public ChargeContractImpl(BaseView view, ChargeTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void addCharge(Charge capitalCash, Handler handler) {
        task.addCharge(view.getContext(),capitalCash,handler);
    }
}
