package com.example.amia.schoolrent.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.PushIdleFragment;
import com.example.amia.schoolrent.Presenter.PersenterImpl.PushIdleContractImpl;
import com.example.amia.schoolrent.Presenter.PushIdleContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.TaskImpl.IdleTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class PushActivity extends AppCompatActivity{

    protected IdleInfo idleInfo;
    protected Student student;

    protected static final int ACCESS_WIFI_STATE_FLAG = 0;
    protected static final int ACCESS_NETWORK_STATE_FLAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        getPermission();
        init();
        loadFragment();
    }

    /**
     * 获取需要的权限
     */
    protected void getPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_WIFI_STATE},ACCESS_WIFI_STATE_FLAG);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_NETWORK_STATE},ACCESS_NETWORK_STATE_FLAG);
        }
    }

    protected void init(){
        Intent intent = getIntent();
        student = (Student) intent.getSerializableExtra("student");

        idleInfo = new IdleInfo();
        idleInfo.setUserId(student.getUserId());
    }

    private void loadFragment(){
        PushIdleFragment fragment = (PushIdleFragment) getSupportFragmentManager().findFragmentById(R.id.push_fragment);
        if(fragment == null){
            fragment = PushIdleFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.push_fragment);
        }

        //设置Presenter
        IdleTask keyTask = new IdleTaskImpl();
        PushIdleContract.Presenter presenter = new PushIdleContractImpl(fragment,keyTask);
        fragment.setPresenter(presenter);
    }
}
