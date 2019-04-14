package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.Complain;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.OtherComplainContract;
import com.example.amia.schoolrent.Task.ComplainTask;

public class OtherComplainContractImpl implements OtherComplainContract.Presenter {

    private BaseView view;
    private ComplainTask task;

    public OtherComplainContractImpl(BaseView view, ComplainTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void addComplain(Complain otherComplain, Handler handler) {
        task.addComplain(view.getContext(),otherComplain,handler);
    }
}
