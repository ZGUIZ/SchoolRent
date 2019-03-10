package com.example.amia.schoolrent.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.amia.schoolrent.Activity.BaseAcitivity;
import com.example.amia.schoolrent.Activity.MainActivity;
import com.example.amia.schoolrent.Activity.PushActivity;
import com.example.amia.schoolrent.Activity.PushArticleActivity;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.ListenerAdapter.AnimationListenerAdapter;
import com.example.amia.schoolrent.Presenter.MainContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.MainContractImpl;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.TaskImpl.IdleTaskImpl;
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
    private Fragment indexFragment;

    private RelativeLayout pushLayout;
    private RelativeLayout idleBtn;
    private RelativeLayout articleBtn;
    private LinearLayout pushBorder;

    //被选中的Fragment
    private Fragment selected;

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
        loadSelect();

        ToolBarButton indexButton = view.findViewById(R.id.index_btn);
        toolBarButtons.add(indexButton);
        indexButton.setOnClickListener(clickListener,clearButtonStatus);

        ToolBarButton newestButton = view.findViewById(R.id.newest);
        toolBarButtons.add(newestButton);
        newestButton.setOnClickListener(clickListener,clearButtonStatus);

        ToolBarButton pushButton = view.findViewById(R.id.push);
        toolBarButtons.add(pushButton);
        pushButton.setOnClickListener(clickListener);

        ToolBarButton messageButton = view.findViewById(R.id.message);
        toolBarButtons.add(messageButton);
        messageButton.setOnClickListener(clickListener,clearButtonStatus);

        ToolBarButton mineButton = view.findViewById(R.id.mine);
        toolBarButtons.add(mineButton);
        mineButton.setOnClickListener(clickListener,clearButtonStatus);

        pushLayout = view.findViewById(R.id.push_type_ll);
        idleBtn = view.findViewById(R.id.idle_btn);
        articleBtn = view.findViewById(R.id.article_btn);
        pushLayout.setOnClickListener(clickListener);
        idleBtn.setOnClickListener(clickListener);
        articleBtn.setOnClickListener(clickListener);
        pushBorder = view.findViewById(R.id.push_border);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;

        toolBarButtons.clear();
        //presenter = null;
    }

    protected void loadSelect(){
        if(selected == null){
            loadIndex();
            return;
        }
        ActivityUtil.replaceFragment(getActivity().getSupportFragmentManager(),selected,R.id.main_frame_layout);
    }

    /**
     * 加载首页
     */
    protected void loadIndex(){
        if(indexFragment == null){
            indexFragment = IndexFragment.newInstance();
        }
        selected = indexFragment;

        ActivityUtil.replaceFragment(getActivity().getSupportFragmentManager(),indexFragment,R.id.main_frame_layout);

        IdleTask task = new IdleTaskImpl();
        IndexFragment fragment = ((IndexFragment)indexFragment);
        MainContract.Presenter presenter = new MainContractImpl(fragment,task);
        fragment.setPresenter(presenter);
    }

    /**
     * 加载“我的”
     */
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
        selected = mineFragment;
        ActivityUtil.replaceFragment(activity.getSupportFragmentManager(),mineFragment,R.id.main_frame_layout);
    }

    private void loadPushActivity(){
        MainActivity mainActivity = (MainActivity) getActivity();
        Intent intent = new Intent(mainActivity, PushActivity.class);
        intent.putExtra("student",mainActivity.getStudent());
        startActivity(intent);
        hidePushLayout();
    }

    private void loadPushArticleActivity(){
        MainActivity mainActivity = (MainActivity) getActivity();
        Intent intent = new Intent(mainActivity, PushArticleActivity.class);
        intent.putExtra("student",mainActivity.getStudent());
        startActivity(intent);
        hidePushLayout();
    }

    private void showPushLayout(){
        if(pushLayout.getVisibility() ==View.VISIBLE){
            hidePushLayout();
        } else {
            pushLayout.setVisibility(View.VISIBLE);
            Animation inAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.push_bottom_in_short);
            pushBorder.startAnimation(inAnimation);
        }
    }

    private void hidePushLayout(){
        Animation outAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.push_buttom_out);
        outAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                pushLayout.setVisibility(View.GONE);
            }
        });
        pushBorder.startAnimation(outAnimation);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.mine:
                    loadMine();
                    break;
                case R.id.index_btn:
                    loadIndex();
                    break;
                case R.id.push:
                    showPushLayout();
                    break;
                case R.id.push_type_ll:
                    hidePushLayout();
                    break;
                case R.id.idle_btn:
                    loadPushActivity();
                    break;
                case R.id.article_btn:
                    loadPushArticleActivity();
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
