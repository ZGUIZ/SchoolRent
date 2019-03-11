package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.RentNeedsExtend;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.MyArticleContract;
import com.example.amia.schoolrent.Task.RentNeedsTask;

public class MyArticleContractImpl implements MyArticleContract.Presenter {

    private RentNeedsTask task;
    private BaseView view;

    public MyArticleContractImpl(RentNeedsTask task, BaseView view) {
        this.task = task;
        this.view = view;
    }

    @Override
    public void queryMyArticle(RentNeedsExtend rentNeedsExtend, Handler handler) {
        task.queryMyArticle(view.getContext(),rentNeedsExtend,handler);
    }

    @Override
    public void del(RentNeeds rentNeeds, Handler handler) {
        task.delArticle(view.getContext(),rentNeeds,handler);
    }
}
