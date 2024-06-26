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

import com.example.amia.schoolrent.Activity.ActivityInterface.RentNeedsInterface;
import com.example.amia.schoolrent.Bean.Complain;
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
import com.example.amia.schoolrent.Task.StudentTask;
import com.example.amia.schoolrent.Task.TaskImpl.IdleTaskImpl;
import com.example.amia.schoolrent.Task.TaskImpl.RefuseTaskImpl;
import com.example.amia.schoolrent.Task.TaskImpl.StudentTaskImpl;
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
        StudentTask studentTask = new StudentTaskImpl();
        RentNeedsContract.Presenter presenter = new RentNeedsContractImpl(fragment,refuseTask,studentTask);
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
        intent.putExtra("rentNeeds",rentNeeds);
        Complain complain = new Complain();
        //设置为商品投诉类型
        complain.setComplainType(2);
        intent.putExtra("complain",complain);
        startActivity(intent);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
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
