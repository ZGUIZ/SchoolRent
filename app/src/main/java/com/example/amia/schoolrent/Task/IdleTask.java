package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Student;

import java.util.List;

public interface IdleTask extends BaseTask{
    int ERRORWITHMESSAGE=-2;
    int ERROR = -1;
    int INDEX_CLASSIFY = 1;
    int ALL_CLASSIFY = 2;
    int CLASSIFY_ICON = 3;

    /**
     * 获取首页分类
     * @param context
     * @param handler
     */
    void getIndexClassify(Context context,Handler handler);

    /**
     * 从缓存中获取分类
     * @return
     */
    List<Classify> getIndexClassifyFromCache();

    /**
     * 获取所有分类
     * @param context
     * @param handler
     */
    void getAllClassify(Context context,Handler handler);

    /**
     * 上传图片
     * @param context
     * @param student
     * @param srcPath
     * @param handler
     * @return task的id
     */
    String uploadImage(Context context,Student student, String srcPath, Handler handler);
    void stopUpload(String id);
    /**
     * 获取商品列表
     * @param list
     * @param index
     * @return
     */
    void getListInfo(List<IdleInfo> list,int index,Handler handler);
}
