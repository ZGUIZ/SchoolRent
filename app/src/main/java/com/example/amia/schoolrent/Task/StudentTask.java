package com.example.amia.schoolrent.Task;

import android.content.Context;

import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.LoginContract;

public interface StudentTask {
    void login(Context context,Student student, LoginContract.CallBack callBack);
}
