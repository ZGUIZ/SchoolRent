package com.example.amia.schoolrent.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.SplashFragment;
import com.example.amia.schoolrent.Presenter.PersenterImpl.SplashContractImpl;
import com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl;
import com.example.amia.schoolrent.Presenter.SplashContract;
import com.example.amia.schoolrent.Presenter.StudentContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.KeyTask;
import com.example.amia.schoolrent.Task.StudentTask;
import com.example.amia.schoolrent.Task.TaskImpl.KeyTaskImpl;
import com.example.amia.schoolrent.Task.TaskImpl.StudentTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.SharedPreferencesUtil;

public class SplashActivity extends AppCompatActivity {

    //网络权限回执号
    private static final int PERMISSION_REQUEST_INTERNET = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
        loadFragment();
    }

    private void init(){
        //网络权限
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.INTERNET)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},PERMISSION_REQUEST_INTERNET);
        }

        //申请内存读写权限
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                ||ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_EXTERNAL_STORAGE);
        }

        //读取sessionId
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(this);
        sharedPreferencesUtil.read("SessionId",new String[]{"sessionId"});
    }

    private void loadFragment(){
        SplashFragment fragment = (SplashFragment) getSupportFragmentManager().findFragmentById(R.id.splash_fragment);
        if(fragment == null){
            fragment = SplashFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.splash_fragment);
        }

        //设置Presenter
        KeyTask keyTask = new KeyTaskImpl();
        StudentTask studentTask = new StudentTaskImpl();
        SplashContract.Presenter presenter = new SplashContractImpl(fragment,keyTask);
        StudentContract.Presenter studentPresenter = new StudentContractImpl(fragment,studentTask);
        fragment.setPresenter(presenter,studentPresenter);
    }

}
