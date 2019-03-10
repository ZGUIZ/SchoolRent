package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.RentNeedsExtend;

public interface RentNeedsTask {

    int ADD_ARTICLE = 801;
    int DEL_ARTICLE = 802;
    int ARTICLE_LIST = 803;

    void pushArticle(Context context,RentNeeds needs, Handler handler);
    void delArticle(Context context,RentNeeds needs,Handler handler);
    void queryArticle(Context context, RentNeedsExtend needsExtend,Handler handler);
}
