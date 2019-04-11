package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.Message;

import java.util.List;

public interface MessageTask  extends BaseTask {
    int ERROR = -1;
    int MESSAGE = 801;
    int DEL_SUCCESS = 802;

    void queryMineMessage(Context context, Handler handler);

    void requestPush(Context context,Handler handler);

    void delMessage(Message message,Handler handler);
}
