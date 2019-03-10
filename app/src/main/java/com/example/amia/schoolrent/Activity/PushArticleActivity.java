package com.example.amia.schoolrent.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.PushArticleFragment;
import com.example.amia.schoolrent.Fragment.PushIdleFragment;
import com.example.amia.schoolrent.Presenter.PersenterImpl.PushArticleContractImpl;
import com.example.amia.schoolrent.Presenter.PersenterImpl.PushIdleContractImpl;
import com.example.amia.schoolrent.Presenter.PushArticleContract;
import com.example.amia.schoolrent.Presenter.PushIdleContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.RefuseTask;
import com.example.amia.schoolrent.Task.RentNeedsTask;
import com.example.amia.schoolrent.Task.TaskImpl.IdleTaskImpl;
import com.example.amia.schoolrent.Task.TaskImpl.RefuseTaskImpl;
import com.example.amia.schoolrent.Task.TaskImpl.RentNeedsTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class PushArticleActivity extends AppCompatActivity implements StudentInterface {

    protected IdleInfo idleInfo;
    protected Student student;

    private PushArticleFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_article);
        init();
    }

    protected void init(){
        setToolBar();

        Intent intent = getIntent();
        student = (Student) intent.getSerializableExtra("student");

        idleInfo = new IdleInfo();
        idleInfo.setUserId(student.getUserId());
        loadFragment();
    }

    protected void setToolBar(){
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(onClickListener);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.push_idle);
    }

    private void loadFragment(){
        fragment = (PushArticleFragment) getSupportFragmentManager().findFragmentById(R.id.push_fragment);
        if(fragment == null){
            fragment = PushArticleFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.push_fragment);
        }

        //设置Presenter
        RentNeedsTask rentNeedsTask = new RentNeedsTaskImpl();
        RefuseTask refuseTask = new RefuseTaskImpl();
        PushArticleContract.Presenter presenter = new PushArticleContractImpl(fragment,rentNeedsTask,refuseTask);
        fragment.setPresenter(presenter);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PushArticleActivity.this.finish();
        }
    };

    @Override
    public Student getStudent() {
        return student;
    }
}
