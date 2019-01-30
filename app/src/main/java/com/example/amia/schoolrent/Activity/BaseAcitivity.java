package com.example.amia.schoolrent.Activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.NetUtils;

public abstract class BaseAcitivity extends AppCompatActivity {
    private long lastBackPressTime;

    protected static boolean isLoading;
    @Override
    public void onBackPressed() {
        if(isLoading){
           disConnection();
            return;
        }
        //双击返回退出
        long secondBackPressTime=System.currentTimeMillis();
        if(secondBackPressTime-lastBackPressTime>=1500){
            Toast.makeText(this,R.string.double_exit,Toast.LENGTH_SHORT).show();
            lastBackPressTime=secondBackPressTime;
        } else{
            this.finish();
        }
    }

    protected void disConnection(){
        NetUtils.disConnection();
    }
}
