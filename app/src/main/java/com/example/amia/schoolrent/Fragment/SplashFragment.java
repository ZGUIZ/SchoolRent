package com.example.amia.schoolrent.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.LoginActivity;
import com.example.amia.schoolrent.Activity.MainActivity;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.SplashContract;
import com.example.amia.schoolrent.Presenter.StudentContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.NetUtils;
import com.example.amia.schoolrent.Util.SharedPreferencesUtil;
import com.example.amia.schoolrent.View.LogoView;

import org.json.JSONException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.example.amia.schoolrent.Task.StudentTask.CURRENT_USER_ERROR;
import static com.example.amia.schoolrent.Task.StudentTask.CURRENT_USER_SUCCESS;

public class SplashFragment extends Fragment implements SplashContract.View,SplashContract.CallBack {

    private View view;

    private SplashContract.Presenter presenter;
    private StudentContract.Presenter studentPresenter;

    public static SplashFragment newInstance(){
        SplashFragment splashFragment=new SplashFragment();
        return splashFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_splash,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadAnimation();

        studentPresenter.getCurrentUser(handler);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
        studentPresenter = null;
    }

    //加载动画
    private void loadAnimation(){
        LogoView logoView = view.findViewById(R.id.logo);
        Animation inAnimation= AnimationUtils.loadAnimation(getActivity(),R.anim.push_bottom_in);
        logoView.startAnimation(inAnimation);
    }

    @Override
    public void setStatus(Message msg) {
        Activity activity =getActivity();
        Intent intent = null;
        int status = msg.what;
        switch (status){
            case -1:
                Toast.makeText(activity,R.string.link_error,Toast.LENGTH_SHORT).show();
                return;
            case 0:
                //打开登陆页面
                intent = new Intent(activity,LoginActivity.class);
                break;
            case 1:
                //打开列表页面
                intent = new Intent(activity,MainActivity.class);
                intent.putExtra("student", (Student) msg.obj);
                startActivity(intent);
                break;
        }
        startActivity(intent);
        activity.finish();
    }

    protected void loadPublicKey(){
        try {
            presenter.onStart(this);
        } catch (Exception e){
            Toast.makeText(getContext(),ActivityUtil.getString(getContext(),R.string.link_error),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    protected void loadMainAcitvity(Object o){
        try{
            Student student = (Student) o;
            Intent intent = new Intent(getActivity(),MainActivity.class);
            intent.putExtra("student",student);
            startActivity(intent);
            getActivity().finish();
        } catch (ClassCastException e){
            e.printStackTrace();
            loadPublicKey();
        }

    }

    @Override
    public Context getContext(){
        return getActivity();
    }

    @Deprecated
    @Override
    public void setPresenter(SplashContract.Presenter presenter){
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter,StudentContract.Presenter studentPresenter) {
        this.presenter = presenter;
        this.studentPresenter = studentPresenter;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CURRENT_USER_SUCCESS:
                    loadMainAcitvity(msg.obj);
                    break;
                case CURRENT_USER_ERROR:
                    loadPublicKey();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
