package com.example.amia.schoolrent.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.MyArticleFragment;
import com.example.amia.schoolrent.Presenter.MyArticleContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.MyArticleContractImpl;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.RentNeedsTask;
import com.example.amia.schoolrent.Task.TaskImpl.RentNeedsTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class MyNeedActivity extends AppCompatActivity implements StudentInterface {

    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_need);
        init();
    }

    private void init(){
        Intent intent = getIntent();
        student = (Student) intent.getSerializableExtra("student");

        setToolBar();
        setFragment();
    }

    protected void setToolBar(){
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(onClickListener);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.mine_need);
    }

    protected void setFragment(){
        MyArticleFragment fragment = (MyArticleFragment) getSupportFragmentManager().findFragmentById(R.id.my_need_fragment);
        if(fragment == null){
            fragment =MyArticleFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.my_need_fragment);
        }
        RentNeedsTask task = new RentNeedsTaskImpl();
        MyArticleContract.Presenter presenter = new MyArticleContractImpl(task,fragment);
        fragment.setPresenter(presenter);
    }

    @Override
    public Student getStudent() {
        return student;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MyNeedActivity.this.finish();
        }
    };
}
