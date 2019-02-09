package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.IdleInfoExtend;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.MainContract;
import com.example.amia.schoolrent.Task.IdleTask;

import java.util.List;

public class MainContractImpl implements MainContract.Presenter {
    private BaseView view;
    private IdleTask task;

    public MainContractImpl(BaseView view, IdleTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void getIndexClassify(Handler handler) {
        task.getIndexClassify(view.getContext(),handler);
    }

    @Override
    public List<Classify> getCacheClassify() {
        return task.getIndexClassifyFromCache();
    }

    @Override
    public void getIdleByPages(IdleInfoExtend idleInfo, Handler handler) {
        task.getListInfo(view.getContext(),idleInfo,handler);
    }

    @Override
    public String uploadImage(Context context, Student student, String srcPath, Handler handler) {
        return task.uploadImage(context,student,srcPath,handler);
    }
}
