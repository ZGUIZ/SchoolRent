package com.example.amia.schoolrent.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.AllClassifyFragment;
import com.example.amia.schoolrent.Presenter.ClassifyContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.ClassifyContractImpl;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.TaskImpl.IdleTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class AllClassifyActivity extends AppCompatActivity implements StudentInterface {

    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_classify);

        setToolBar();
        init();
    }

    private void init(){
        Intent intent = getIntent();
        student = (Student) intent.getSerializableExtra("student");

        AllClassifyFragment fragment = (AllClassifyFragment) getSupportFragmentManager().findFragmentById(R.id.classify_fragment);
        if(fragment == null){
            fragment = AllClassifyFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.classify_fragment);
        }

        //设置Presenter
        IdleTask idleTask = new IdleTaskImpl();
        ClassifyContract.Presenter presenter = new ClassifyContractImpl(fragment,idleTask);
        fragment.setPresenter(presenter);
    }

    protected void setToolBar(){
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(onClickListener);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.all_classify_title);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AllClassifyActivity.this.finish();
        }
    };

    @Override
    public Student getStudent() {
        return student;
    }
}
