package com.example.amia.schoolrent.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.amia.schoolrent.Activity.ActivityInterface.IdleInfoInterface;
import com.example.amia.schoolrent.Bean.Complain;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.IdleInfoFragment;
import com.example.amia.schoolrent.Presenter.IdleInfoContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.IdleInfoContractImpl;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.RefuseTask;
import com.example.amia.schoolrent.Task.StudentTask;
import com.example.amia.schoolrent.Task.TaskImpl.IdleTaskImpl;
import com.example.amia.schoolrent.Task.TaskImpl.RefuseTaskImpl;
import com.example.amia.schoolrent.Task.TaskImpl.StudentTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class IdleInfoActivity extends AppCompatActivity implements IdleInfoInterface {

    private IdleInfo idleInfo;
    protected Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle_info);
        init();
    }

    protected void init(){
        Intent intent = getIntent();
        idleInfo = (IdleInfo) intent.getSerializableExtra("idleInfo");
        student = (Student) intent.getSerializableExtra("student");

        IdleInfoFragment fragment = (IdleInfoFragment) getSupportFragmentManager().findFragmentById(R.id.idle_info_fragment);
        if(fragment == null){
            fragment = IdleInfoFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.idle_info_fragment);
        }

        //设置Presenter
        RefuseTask refuseTask = new RefuseTaskImpl();
        IdleTask idleTask = new IdleTaskImpl();
        StudentTask studentTask = new StudentTaskImpl();
        IdleInfoContract.Presenter presenter = new IdleInfoContractImpl(fragment,refuseTask,idleTask,studentTask);
        fragment.setPresenter(presenter);

        setToolBar();

        TextView textView = findViewById(R.id.title);
        textView.setText(idleInfo.getTitle());
    }

    protected void setToolBar(){
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(onClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.info_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.complain_menu:
                toComplainActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toComplainActivity(){
        Intent intent = new Intent(this,ComplainActivity.class);
        intent.putExtra("idleInfo",idleInfo);
        Complain complain = new Complain();
        //设置为商品投诉类型
        complain.setComplainType(1);
        intent.putExtra("complain",complain);
        startActivity(intent);
    }

    @Override
    public IdleInfo getIdleInfo() {
        return idleInfo;
    }

    public Student getStudent() {
        return student;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
}
