package com.example.amia.schoolrent.Presenter;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfoExtend;

public interface SearchContract {
    interface Presenter{
        /**
         * 获取商品的信息
         * @param handler
         */
        void getIdleByPages(IdleInfoExtend idleInfo, Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void setClassifyId(String id);
        void linkError();
    }
}
