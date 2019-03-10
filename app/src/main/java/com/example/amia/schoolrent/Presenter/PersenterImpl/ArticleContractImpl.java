package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.RentNeedsExtend;
import com.example.amia.schoolrent.Presenter.ArticleContract;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Task.RefuseTask;
import com.example.amia.schoolrent.Task.RentNeedsTask;

public class ArticleContractImpl implements ArticleContract.Presenter {

    private BaseView view;
    private RentNeedsTask task;
    private RefuseTask refuseTask;

    public ArticleContractImpl(BaseView view, RentNeedsTask task,RefuseTask refuseTask) {
        this.view = view;
        this.task = task;
        this.refuseTask = refuseTask;
    }

    @Override
    public void queryArticleList(RentNeedsExtend rentNeedsExtend, Handler handler) {
        task.queryArticle(view.getContext(),rentNeedsExtend,handler);
    }
}
