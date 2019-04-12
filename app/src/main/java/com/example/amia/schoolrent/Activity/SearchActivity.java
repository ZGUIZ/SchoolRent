package com.example.amia.schoolrent.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.SearchFragment;
import com.example.amia.schoolrent.Presenter.PersenterImpl.SearchContractImpl;
import com.example.amia.schoolrent.Presenter.SearchContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.TaskImpl.IdleTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class SearchActivity extends AppCompatActivity implements StudentInterface {

    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    private void init(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("classifyId");
        student = (Student) intent.getSerializableExtra("student");

        SearchFragment fragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment);
        if(fragment == null){
            fragment = SearchFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.search_fragment);
        }

        //如果参数有类别信息，则填入
        if(id != null && !"".equals(id)){
            fragment.setClassifyId(id);
        }

        //设置Presenter
        IdleTask idleTask = new IdleTaskImpl();
        SearchContract.Presenter presenter = new SearchContractImpl(fragment,idleTask);
        fragment.setPresenter(presenter);
    }

    @Override
    public Student getStudent() {
        return student;
    }
}
