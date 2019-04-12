package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.ClassifyContract;
import com.example.amia.schoolrent.Task.IdleTask;


public class ClassifyContractImpl implements ClassifyContract.Presenter {

    private BaseView view;
    private IdleTask task;

    public ClassifyContractImpl(BaseView view, IdleTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void queryClassify(Handler handler) {
        task.getAllClassify(view.getContext(),handler);
    }
}
