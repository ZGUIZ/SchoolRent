package com.example.amia.schoolrent.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.amia.schoolrent.Fragment.UpdateIdleFragment;
import com.example.amia.schoolrent.Presenter.PersenterImpl.UpdateIdleContractImpl;
import com.example.amia.schoolrent.Presenter.UpdateIdleContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.TaskImpl.IdleTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

import static com.example.amia.schoolrent.Activity.PushActivity.ACCESS_NETWORK_STATE_FLAG;
import static com.example.amia.schoolrent.Activity.PushActivity.ACCESS_WIFI_STATE_FLAG;

public class UpdateIdleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_idle);
        init();
    }

    private void init(){
        setToolBar();
        getPermission();

        UpdateIdleFragment fragment = (UpdateIdleFragment) getSupportFragmentManager().findFragmentById(R.id.update_fragment);
        if(fragment == null){
            fragment = UpdateIdleFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.update_fragment);
        }

        //设置Presenter
        IdleTask idleTask = new IdleTaskImpl();
        UpdateIdleContract.Presenter presenter = new UpdateIdleContractImpl(fragment,idleTask);
        fragment.setPresenter(presenter);
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

    protected void setToolBar(){
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(onClickListener);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.alter_idle);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            UpdateIdleActivity.this.finish();
        }
    };
}
