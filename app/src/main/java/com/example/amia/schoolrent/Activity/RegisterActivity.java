package com.example.amia.schoolrent.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.MailFragment;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class RegisterActivity extends AppCompatActivity {

    protected Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadFragment();
    }

    protected void loadFragment(){
        MailFragment fragment = (MailFragment) getSupportFragmentManager().findFragmentById(R.id.register_fragment);
        if(fragment == null){
            fragment = MailFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.register_fragment);
        }

        //设置Presenter
      /* SchoolTask schoolTask = new SchoolTaskImpl();
        StudentTask studentTask = new StudentTaskImpl();
        LoginContract.Presenter presenter = new LoginContractImpl(fragment,schoolTask,studentTask);
        fragment.setPresenter(presenter);*/

    }
}
