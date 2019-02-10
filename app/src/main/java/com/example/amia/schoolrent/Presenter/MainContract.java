package com.example.amia.schoolrent.Presenter;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.IdleInfoExtend;
import com.example.amia.schoolrent.Bean.Student;

import java.util.List;

public interface MainContract {
    interface Presenter{
        /**
         * 获取首页的分类信息
         * @param handler
         */
        void getIndexClassify(IdleInfoExtend idleInfoExtend,Handler handler);

        /**
         * 读取缓存的分类
         * @return
         */
        List<Classify> getCacheClassify();

        void getIdleByPages(IdleInfoExtend idleInfo, Handler handler);

        String uploadImage(Context context, Student student, String srcPath,Handler handler);

        void loadIamge(String id,String url,Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
    }
}
