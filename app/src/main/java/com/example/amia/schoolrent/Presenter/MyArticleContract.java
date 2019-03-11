package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.RentNeedsExtend;

public interface MyArticleContract {
    interface Presenter{
        void queryMyArticle(RentNeedsExtend rentNeedsExtend, Handler handler);
        void del(RentNeeds rentNeeds,Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
    }
}
