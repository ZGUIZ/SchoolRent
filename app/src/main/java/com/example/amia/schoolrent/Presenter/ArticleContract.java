package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.RentNeedsExtend;


public interface ArticleContract {
    interface Presenter{
        void queryArticleList(RentNeedsExtend rentNeedsExtend, Handler handler);
    }

    interface View extends BaseView<Presenter>{

    }
}
