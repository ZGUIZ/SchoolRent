package com.example.amia.schoolrent.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.MailFragment;
import com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl;
import com.example.amia.schoolrent.Presenter.StudentContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.StudentTask;
import com.example.amia.schoolrent.Task.TaskImpl.StudentTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class RegisterActivity extends AppCompatActivity {

    protected Student student =new Student();

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
        StudentTask studentTask = new StudentTaskImpl();
        StudentContract.Presenter presenter = new StudentContractImpl(fragment,studentTask);
        fragment.setPresenter(presenter);
    }

    public void setEMail(String eMail){
        student.setEmail(eMail);
    }
}
