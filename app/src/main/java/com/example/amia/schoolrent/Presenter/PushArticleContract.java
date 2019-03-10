package com.example.amia.schoolrent.Presenter;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.RentNeedsExtend;
import com.example.amia.schoolrent.Bean.ResponseInfo;


public interface PushArticleContract {
    interface Presenter{
        void pushArticle(RentNeeds needs, Handler handler);
        void delArticle(RentNeeds needs,Handler handler);
        void addRefuse(ResponseInfo responseInfo,Handler handler);
        void queryArticleList(RentNeedsExtend rentNeedsExtend,Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void leavePage();
    }
}
