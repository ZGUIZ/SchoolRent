package com.example.amia.schoolrent.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.MyPushFragment;
import com.example.amia.schoolrent.Presenter.MyPushContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.MyPushContractImpl;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.TaskImpl.IdleTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class MyPushActivity extends AppCompatActivity implements StudentInterface {

    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_push);
        init();
    }

    private void init(){
        setToolBar();

        student = (Student) getIntent().getSerializableExtra("student");

        MyPushFragment fragment = (MyPushFragment) getSupportFragmentManager().findFragmentById(R.id.my_push_fragment);
        if(fragment == null){
            fragment = MyPushFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.my_push_fragment);
        }

        IdleTask idleTask = new IdleTaskImpl();
        MyPushContract.Presenter presenter = new MyPushContractImpl(fragment,idleTask);
        fragment.setPresenter(presenter);
    }

    protected void setToolBar(){
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(onClickListener);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.my_push_title);
    }

    @Override
    public Student getStudent() {
        return student;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MyPushActivity.this.finish();
        }
    };
}
