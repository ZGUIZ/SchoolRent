package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.Province;

public interface SchoolTask extends BaseTask{
    int ERRORWITHMESSAGE=-2;
    int ERROR = -1;
    int PROVINCE = 0;
    int CITY = 1;
    int SCHOOL = 2;
    int PASSWORDERROR = 3;
    int LOGINSUCCESS = 4;

    void getProvince(Context context, Handler handler);
    void getCity(Context context,Province province,Handler handler) ;
    void getSchool(Context context,Province province,Handler handler);
}
