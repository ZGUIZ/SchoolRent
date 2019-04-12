package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfoExtend;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.SearchContract;
import com.example.amia.schoolrent.Task.IdleTask;

public class SearchContractImpl implements SearchContract.Presenter {

    private BaseView view;
    private IdleTask task;

    public SearchContractImpl(BaseView view, IdleTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void getIdleByPages(IdleInfoExtend idleInfo, Handler handler) {
        task.getListInfo(view.getContext(),idleInfo,handler);
    }
}
