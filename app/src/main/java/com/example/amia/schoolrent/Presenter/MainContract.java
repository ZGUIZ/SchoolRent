package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.Classify;

import java.util.List;

public interface MainContract {
    interface Presenter{
        /**
         * 获取首页的分类信息
         * @param handler
         */
        void getIndexClassify(Handler handler);
        List<Classify> getCacheClassify();
    }

    interface View extends BaseView<Presenter>{
        void linkError();
    }
}
