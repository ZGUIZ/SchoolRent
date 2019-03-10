package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.RentNeeds;

public interface RentNeedsTask {

    int ADD_ARTICLE = 801;
    int DLE_ARTICLE = 802;

    void pushArticle(Context context,RentNeeds needs, Handler handler);
    void delArticle(Context context,RentNeeds needs,Handler handler);
}
