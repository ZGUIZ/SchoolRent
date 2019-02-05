package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.Bean.IdleInfo;

import java.util.List;

public interface IdleTask extends BaseTask{
    int ERRORWITHMESSAGE=-2;
    int ERROR = -1;
    int INDEX_CLASSIFY = 1;
    int ALL_CLASSIFY = 2;
    int CLASSIFY_ICON = 3;

    void getIndexClassify(Context context,Handler handler);
    List<Classify> getIndexClassifyFromCache();
    void getAllClassify(Context context,Handler handler);
    /**
     * 获取商品列表
     * @param list
     * @param index
     * @return
     */
    void getListInfo(List<IdleInfo> list,int index,Handler handler);
}
