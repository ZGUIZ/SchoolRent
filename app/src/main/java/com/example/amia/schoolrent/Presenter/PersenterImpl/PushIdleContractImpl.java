package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.PushIdleContract;
import com.example.amia.schoolrent.Task.IdleTask;

public class PushIdleContractImpl implements PushIdleContract.Presenter {
    private BaseView view;
    private IdleTask task;

    public PushIdleContractImpl(BaseView view, IdleTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void uploadFile(Context context, Student student, String url, Handler handler) {
        task.uploadImage(context,student,url,handler);
    }

    @Override
    public void getAllClassify(Context context, Handler handler) {
        task.getAllClassify(context,handler);
    }
}
