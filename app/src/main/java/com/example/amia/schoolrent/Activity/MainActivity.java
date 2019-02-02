package com.example.amia.schoolrent.Activity;

import android.os.Bundle;

import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.MainFragement;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class MainActivity extends BaseAcitivity {

    protected Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    protected void init(){
        student = (Student) getIntent().getSerializableExtra("student");

        MainFragement fragment = (MainFragement) getSupportFragmentManager().findFragmentById(R.id.main_frame);
        if(fragment == null){
            fragment = MainFragement.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.main_frame);
        }

    }

    public Student getStudent(){
        return student;
    }
}
