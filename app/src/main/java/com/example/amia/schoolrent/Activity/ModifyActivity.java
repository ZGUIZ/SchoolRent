package com.example.amia.schoolrent.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.FillStudentInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.ModifyInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.BaseInfoFragement;
import com.example.amia.schoolrent.Fragment.ModifyFragment;
import com.example.amia.schoolrent.Presenter.BaseInfoContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.BaseInfoContractImpl;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.StudentTask;
import com.example.amia.schoolrent.Task.TaskImpl.StudentTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

import static com.example.amia.schoolrent.Task.StudentTask.UPDATE_STUDENT_ERROR;
import static com.example.amia.schoolrent.Task.StudentTask.UPDATE_STUDENT_SUCCESS;

public class ModifyActivity extends AppCompatActivity implements StudentInterface, ModifyInterface {

    private Student student;
    private int flag;
    private TextView title;
    private FillStudentInterface fillStudentInterface;

    private StudentTask studentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        setToolBar();
        init();
    }

    protected void setToolBar(){
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(onClickListener);
    }

    protected void init(){
        title = findViewById(R.id.title);

        Intent intent = getIntent();
        flag = intent.getIntExtra("type",0);
        student = (Student) intent.getSerializableExtra("student");

        studentTask = new StudentTaskImpl();

        switch (flag){
            case R.id.user_name_tv:
                title.setText(R.string.user_show_name);
                modifyBaseInfo();
                break;
            case R.id.real_name_tv:
                title.setText(R.string.real_name);
                modifyBaseInfo();
                break;
            case R.id.password_reset_ll:
                title.setText(R.string.password);
                modifyPassword();
                break;
            case R.id.pay_password_ll:
                title.setText(R.string.pay_password);
                modifyPassword();
                break;
            case R.id.telephone_tv:
                title.setText(R.string.telephone);
                modifyValidateInfo();
                break;
            case R.id.mail_tv:
                title.setText(R.string.mail);
                modifyValidateInfo();
                break;
        }

        findViewById(R.id.finish_tv).setOnClickListener(onClickListener);
    }

    private void modifyBaseInfo(){
        ModifyFragment fragment = (ModifyFragment) getSupportFragmentManager().findFragmentById(R.id.modify_fragment);
        if(fragment == null){
            fragment = ModifyFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.modify_fragment);
        }
        fillStudentInterface = fragment;
    }

    private void modifyPassword(){

    }

    private void modifyValidateInfo(){
    }

    @Override
    public Student getStudent() {
        return student;
    }

    protected void fillValue(){
        Student student = fillStudentInterface.fillStudent();
        studentTask.updateInfo(this,student,handler);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.finish_tv:
                    fillValue();
                    break;
                default:
                    ModifyActivity.this.finish();
                    break;
            }

        }
    };


    @Override
    public int getFlags() {
        return flag;
    }

    private void linkError(){
        Toast.makeText(this,R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_STUDENT_SUCCESS:
                    Toast.makeText(ModifyActivity.this,R.string.update_success,Toast.LENGTH_SHORT).show();
                    ModifyActivity.this.finish();
                    break;
                case UPDATE_STUDENT_ERROR:
                    Toast.makeText(ModifyActivity.this,R.string.user_update_error,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
