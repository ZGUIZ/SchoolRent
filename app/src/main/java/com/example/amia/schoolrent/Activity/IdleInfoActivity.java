package com.example.amia.schoolrent.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.amia.schoolrent.Activity.ActivityInterface.IdleInfoInterface;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Fragment.IdleInfoFragment;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class IdleInfoActivity extends AppCompatActivity implements IdleInfoInterface {

    private IdleInfo idleInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle_info);
        init();
    }

    protected void init(){
        Intent intent = getIntent();
        idleInfo = (IdleInfo) intent.getSerializableExtra("idleInfo");

        IdleInfoFragment fragment = (IdleInfoFragment) getSupportFragmentManager().findFragmentById(R.id.idle_info_fragment);
        if(fragment == null){
            fragment = IdleInfoFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.idle_info_fragment);
        }

        //设置Presenter
        /*SchoolTask schoolTask = new SchoolTaskImpl();
        StudentTask studentTask = new StudentTaskImpl();
        LoginContract.Presenter presenter = new LoginContractImpl(fragment,schoolTask,studentTask);
        fragment.setPresenter(presenter);*/
    }

    @Override
    public IdleInfo getIdleInfo() {
        return idleInfo;
    }
}
