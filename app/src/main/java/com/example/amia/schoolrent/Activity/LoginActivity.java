package com.example.amia.schoolrent.Activity;

import android.os.Bundle;

import com.example.amia.schoolrent.Fragment.LoginFragment;
import com.example.amia.schoolrent.Presenter.LoginContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.LoginContractImpl;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.SchoolTask;
import com.example.amia.schoolrent.Task.StudentTask;
import com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl;
import com.example.amia.schoolrent.Task.TaskImpl.StudentTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class LoginActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadFragment();
    }

    protected void loadFragment(){
        LoginFragment fragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.login_fragment);
        if(fragment == null){
            fragment = LoginFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.login_fragment);
        }

        //设置Presenter
        SchoolTask schoolTask = new SchoolTaskImpl();
        StudentTask studentTask = new StudentTaskImpl();
        LoginContract.Presenter presenter = new LoginContractImpl(fragment,schoolTask,studentTask);
        fragment.setPresenter(presenter);

    }
}
