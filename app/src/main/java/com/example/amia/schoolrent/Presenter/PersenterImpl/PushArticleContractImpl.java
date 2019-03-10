package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.ResponseInfo;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.PushArticleContract;
import com.example.amia.schoolrent.Task.RefuseTask;
import com.example.amia.schoolrent.Task.RentNeedsTask;

public class PushArticleContractImpl implements PushArticleContract.Presenter {

    private BaseView view;
    private RentNeedsTask rentNeedsTask;
    private RefuseTask refuseTask;

    public PushArticleContractImpl(BaseView view, RentNeedsTask rentNeedsTask,RefuseTask refuseTask) {
        this.view = view;
        this.rentNeedsTask = rentNeedsTask;
        this.refuseTask = refuseTask;
    }

    @Override
    public void pushArticle(RentNeeds needs, Handler handler) {
        rentNeedsTask.pushArticle(view.getContext(),needs,handler);
    }

    @Override
    public void delArticle(RentNeeds needs, Handler handler) {
        rentNeedsTask.delArticle(view.getContext(),needs,handler);
    }

    @Override
    public void addRefuse(ResponseInfo responseInfo, Handler handler) {
        refuseTask.addRefuse(view.getContext(),responseInfo,handler);
    }
}
