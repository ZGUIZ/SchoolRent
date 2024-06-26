package com.example.amia.schoolrent.Activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.amia.schoolrent.Fragment.CheckStateFragment;
import com.example.amia.schoolrent.Presenter.CheckStateContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.CheckStateContractImpl;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.CheckStatementTask;
import com.example.amia.schoolrent.Task.StudentTask;
import com.example.amia.schoolrent.Task.TaskImpl.CheckStatementTaskImpl;
import com.example.amia.schoolrent.Task.TaskImpl.StudentTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class CheckStatActivity extends AppCompatActivity {

    private CheckStateFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_stat);
        setToolBar();
        init();
    }

    private void init(){
        fragment = (CheckStateFragment) getSupportFragmentManager().findFragmentById(R.id.check_fragment);
        if(fragment == null){
            fragment = CheckStateFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.check_fragment);
        }

        //设置Presenter
        StudentTask studentTask = new StudentTaskImpl();
        CheckStatementTask checkStatementTask = new CheckStatementTaskImpl();
        CheckStateContract.Presenter presenter = new CheckStateContractImpl(fragment,checkStatementTask,studentTask);
        fragment.setPresenter(presenter);
    }

    protected void setToolBar(){
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(onClickListener);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.mine_check);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                default:
                    finish();
            }
        }
    };
}
