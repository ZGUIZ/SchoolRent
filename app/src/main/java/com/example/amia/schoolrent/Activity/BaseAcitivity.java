package com.example.amia.schoolrent.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.NetUtils;

public abstract class BaseAcitivity extends AppCompatActivity {
    private long lastBackPressTime;

    protected static Student student;

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
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            //this.finish();
        }
    }

    protected void disConnection(){
        NetUtils.disConnection();
    }

    public Student getStudent(){
        return student;
    }

    public void setStudent(Student student){
        this.student = student;
    }

    /**
     * 退出登录
     */
    public void exitLogin(){
        //删除连接

        //返回注册页面
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
}
