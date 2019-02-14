package com.example.amia.schoolrent.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.BaseInfoFragement;
import com.example.amia.schoolrent.Presenter.BaseInfoContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.BaseInfoContractImpl;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.StudentTask;
import com.example.amia.schoolrent.Task.TaskImpl.StudentTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class BaseInfoActivity extends AppCompatActivity implements StudentInterface {

    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info);
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
        Intent intent = getIntent();
        student = (Student) intent.getSerializableExtra("student");

        BaseInfoFragement fragment = (BaseInfoFragement) getSupportFragmentManager().findFragmentById(R.id.base_info_fragment);
        if(fragment == null){
            fragment = BaseInfoFragement.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.base_info_fragment);
        }

        //设置Presenter
        StudentTask studentTask = new StudentTaskImpl();
        BaseInfoContract.Presenter presenter = new BaseInfoContractImpl(fragment,studentTask);
        fragment.setPresenter(presenter);
    }

    @Override
    public Student getStudent() {
        return student;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BaseInfoActivity.this.finish();
        }
    };
}
