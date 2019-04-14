package com.example.amia.schoolrent.Activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.MainFragement;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.StudentTask;
import com.example.amia.schoolrent.Task.TaskImpl.StudentTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.NetUtils;
import com.example.amia.schoolrent.Util.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        //初始化推送
        initJPush();
    }

    protected void init(){
        //写入sessionId
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(this);
        Map<String,String> map = new HashMap<>();
        map.put("sessionId", NetUtils.getSessionId());
        sharedPreferencesUtil.write("SessionId",map);

        student = (Student) getIntent().getSerializableExtra("student");

        MainFragement fragment = (MainFragement) getSupportFragmentManager().findFragmentById(R.id.main_frame);
        if(fragment == null){
            fragment = MainFragement.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.main_frame);
        }
    }

    protected void initJPush(){
        JPushInterface.init(this);
        JPushInterface.setAlias(this,0,student.getUserId());

        StudentTask studentTask = new StudentTaskImpl();
        studentTask.pushMessage(this);
    }
}
