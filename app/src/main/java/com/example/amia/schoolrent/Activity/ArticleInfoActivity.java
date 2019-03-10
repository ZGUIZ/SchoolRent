package com.example.amia.schoolrent.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.amia.schoolrent.Activity.ActivityInterface.RentNeedsInterface;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.IdleInfoFragment;
import com.example.amia.schoolrent.Fragment.RentNeedsInfoFragment;
import com.example.amia.schoolrent.Presenter.IdleInfoContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.IdleInfoContractImpl;
import com.example.amia.schoolrent.Presenter.PersenterImpl.RentNeedsContractImpl;
import com.example.amia.schoolrent.Presenter.RentNeedsContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.RefuseTask;
import com.example.amia.schoolrent.Task.TaskImpl.IdleTaskImpl;
import com.example.amia.schoolrent.Task.TaskImpl.RefuseTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class ArticleInfoActivity extends AppCompatActivity implements RentNeedsInterface {

    protected RentNeeds rentNeeds;
    protected Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_info);

        init();
    }

    protected void init(){
        Intent intent = getIntent();
        rentNeeds = (RentNeeds) intent.getSerializableExtra("rentNeeds");
        student = (Student) intent.getSerializableExtra("student");

        RentNeedsInfoFragment fragment = (RentNeedsInfoFragment) getSupportFragmentManager().findFragmentById(R.id.idle_info_fragment);
        if(fragment == null){
            fragment = RentNeedsInfoFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.idle_info_fragment);
        }

        //设置Presenter
        RefuseTask refuseTask = new RefuseTaskImpl();
        RentNeedsContract.Presenter presenter = new RentNeedsContractImpl(fragment,refuseTask);
        fragment.setPresenter(presenter);

        setToolBar();

        TextView textView = findViewById(R.id.title);
        textView.setText(rentNeeds.getTitle());
    }

    protected void setToolBar(){
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.complain_ib:

                    break;
                default:
                    finish();
            }
        }
    };

    @Override
    public RentNeeds getRentNeeds() {
        return rentNeeds;
    }

    @Override
    public Student getStudent() {
        return student;
    }
}
