package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.ResponseInfo;
import com.example.amia.schoolrent.Bean.SecondResponseInfo;

public interface RefuseTask {
    int PUSH_REFUSE_SUCCESS = 600;
    int PUSH_REFUSE_ERROR = 601;
    int LOAD_REFUSE_SUCCESS = 602;
    int LOAD_REFUSE_ERROR = 603;

    void addRefuse(Context context, ResponseInfo responseInfo, Handler handler);
    void addSecondRefuse(Context context, SecondResponseInfo secondResponseInfo, Handler handler);
    void getReufseList(Context context, IdleInfo idleInfo,Handler handler);
}
