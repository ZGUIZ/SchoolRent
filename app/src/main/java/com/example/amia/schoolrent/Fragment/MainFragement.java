package com.example.amia.schoolrent.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.amia.schoolrent.Activity.BaseAcitivity;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.View.ClearButtonStatus;
import com.example.amia.schoolrent.View.ToolBarButton;

import java.util.ArrayList;
import java.util.List;

public class MainFragement extends Fragment {

    protected View view;
    protected List<ToolBarButton> toolBarButtons = new ArrayList<>();

    private FrameLayout frameLayout;
    private Fragment mineFragment;

    public static MainFragement newInstance(){
        MainFragement mainFragement=new MainFragement();
        return mainFragement;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_layout,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    protected void init(){
        frameLayout = view.findViewById(R.id.main_frame_layout);

        ToolBarButton indexButton = view.findViewById(R.id.index_btn);
        toolBarButtons.add(indexButton);
        indexButton.setOnClickListener(clickListener,clearButtonStatus);

        ToolBarButton newestButton = view.findViewById(R.id.newest);
        toolBarButtons.add(newestButton);
        newestButton.setOnClickListener(clickListener,clearButtonStatus);

        ToolBarButton pushButton = view.findViewById(R.id.push);
        toolBarButtons.add(pushButton);
        pushButton.setOnClickListener(clickListener,clearButtonStatus);

        ToolBarButton messageButton = view.findViewById(R.id.message);
        toolBarButtons.add(messageButton);
        messageButton.setOnClickListener(clickListener,clearButtonStatus);

        ToolBarButton mineButton = view.findViewById(R.id.mine);
        toolBarButtons.add(mineButton);
        mineButton.setOnClickListener(clickListener,clearButtonStatus);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;

        toolBarButtons.clear();
        //presenter = null;
    }

    protected void loadMine(){
        BaseAcitivity activity = (BaseAcitivity) getActivity();
        //如果没有学生资料，则直接断开连接
        Student student = activity.getStudent();
        if(student == null){
            activity.exitLogin();
            return;
        }

        if(mineFragment == null){
            mineFragment = MineFragment.newInstance();
        }
        ActivityUtil.replaceFragment(activity.getSupportFragmentManager(),mineFragment,R.id.main_frame_layout);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.mine:
                    loadMine();
                    break;
            }
        }
    };

    ClearButtonStatus clearButtonStatus = new ClearButtonStatus() {
        @Override
        public void clearButtonStatus() {
            for(ToolBarButton toolBarButton: toolBarButtons){
                toolBarButton.setClicked(false);
            }
        }
    };
}
