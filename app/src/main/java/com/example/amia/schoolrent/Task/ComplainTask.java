package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.Complain;


public interface ComplainTask extends BaseTask{
    int PUSH_SUCCESS = 1001;
    void addComplain(Context context, Complain complain, Handler handler);
}
