package com.example.amia.schoolrent.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.amia.schoolrent.Fragment.MineAgreeFragement;
import com.example.amia.schoolrent.Fragment.MyRentFragmentAdapter;
import com.example.amia.schoolrent.Fragment.MySendRequestFragment;
import com.example.amia.schoolrent.Presenter.MineRentContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.MineRentContractImpl;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.TaskImpl.IdleTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

public class MyRentActivity extends AppCompatActivity {

    protected List<Fragment> fragmentList;
    protected List<String> titleList;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rent);
        init();
    }

    private void init(){
        setToolBar();

        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();

        IdleTask idleTask = new IdleTaskImpl();
        MySendRequestFragment myRequest = MySendRequestFragment.newInstance();
        MineRentContract.Presenter presenter = new MineRentContractImpl(myRequest,idleTask);
        myRequest.setPresenter(presenter);
        String requestTitle = ActivityUtil.getString(this,R.string.requested);
        fragmentList.add(myRequest);
        titleList.add(requestTitle);

        MineAgreeFragement agreeFragement = MineAgreeFragement.newInstance();
        String agreeTitle = ActivityUtil.getString(this,R.string.agree_request);
        fragmentList.add(agreeFragement);
        titleList.add(agreeTitle);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(new MyRentFragmentAdapter(getSupportFragmentManager(),this,fragmentList,titleList));
        tabLayout.setupWithViewPager(viewPager);
    }

    protected void setToolBar(){
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(onClickListener);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.mine_rent);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MyRentActivity.this.finish();
        }
    };
}
