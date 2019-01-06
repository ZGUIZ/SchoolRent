package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.Province;

public interface SchoolTask {
    void getProvince(Context context, Handler handler);
    void getCity(Context context,Province province,Handler handler) ;
    void getSchool(Context context,Province province,Handler handler);
}
