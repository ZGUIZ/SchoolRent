package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.CheckStatementExtend;

public interface CheckStatementTask {

    int CHECK_LIST = 1001;
    void getCheckStatement(Context context, CheckStatementExtend extend, Handler handler);
}
