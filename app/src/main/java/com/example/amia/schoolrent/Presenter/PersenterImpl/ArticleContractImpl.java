package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.RentNeedsExtend;
import com.example.amia.schoolrent.Presenter.ArticleContract;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Task.RentNeedsTask;

public class ArticleContractImpl implements ArticleContract.Presenter {

    private BaseView view;
    private RentNeedsTask task;

    public ArticleContractImpl(BaseView view, RentNeedsTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void queryArticleList(RentNeedsExtend rentNeedsExtend, Handler handler) {
        task.queryArticle(view.getContext(),rentNeedsExtend,handler);
    }
}
