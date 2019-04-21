package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.CapitalCash;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.CashContract;
import com.example.amia.schoolrent.Task.ChargeTask;

public class CashContractImpl implements CashContract.Presenter {

    private BaseView view;
    private ChargeTask task;

    public CashContractImpl(BaseView view, ChargeTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void addCash(CapitalCash capitalCash, Handler handler) {
        task.addCapitalCash(view.getContext(),capitalCash,handler);
    }
}
