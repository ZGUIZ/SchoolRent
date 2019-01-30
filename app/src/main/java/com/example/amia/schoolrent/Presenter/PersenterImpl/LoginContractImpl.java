package com.example.amia.schoolrent.Presenter.PersenterImpl;



import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.Province;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.LoginContract;
import com.example.amia.schoolrent.Presenter.StudentContract;
import com.example.amia.schoolrent.Task.SchoolTask;
import com.example.amia.schoolrent.Task.StudentTask;


public class LoginContractImpl implements LoginContract.Presenter {
    private BaseView view;
    private SchoolTask task;
    private StudentTask studentTask;

    public LoginContractImpl(LoginContract.View view, SchoolTask task,StudentTask studentTask) {
        this.view = view;
        this.task = task;
        this.studentTask = studentTask;
    }

    public LoginContractImpl(StudentContract.View view, SchoolTask task, StudentTask studentTask) {
        this.view = view;
        this.task = task;
        this.studentTask = studentTask;
    }

    @Override
    public void getProvince(Handler handler) {
        task.getProvince(view.getContext(),handler);
    }

    @Override
    public void getCity(Province province,Handler handler) {
        task.getCity(view.getContext(),province,handler);
    }

    @Override
    public void getSchool(Province province, Handler handler) {
        task.getSchool(view.getContext(),province,handler);
        //callBack.setSchoolList(schools);
    }

    @Override
    public void login(Context context,Student student,Handler handler) {
        studentTask.login(context,student,handler);
    }
}
