package com.example.amia.schoolrent.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.MainActivity;
import com.example.amia.schoolrent.Activity.RegisterActivity;

import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.LoginContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.SharedPreferencesUtil;

import java.util.Map;

import static com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl.ERROR;
import static com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl.LOGINSUCCESS;
import static com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl.PASSWORDERROR;

public class LoginFragment extends Fragment implements LoginContract.View {

    private static View view;
    private LoginContract.Presenter presenter;


    public static LoginFragment newInstance(){
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.default_icon);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),src);

        roundedBitmapDrawable.setCornerRadius(src.getWidth()/2);
        roundedBitmapDrawable.setAntiAlias(true);
        ImageView imageView = view.findViewById(R.id.user_image);
        imageView.setImageDrawable(roundedBitmapDrawable);

        //注册按钮设置监听事件
        Button button = view.findViewById(R.id.register_btn);
        button.setOnClickListener(clickListener);
        //登录按钮设置监听事件
        Button loginButton = view.findViewById(R.id.login_btn);
        loginButton.setOnClickListener(clickListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    public void passwordError() {
        Toast.makeText(getActivity(),ActivityUtil.getString(getActivity(),R.string.password_error),Toast.LENGTH_SHORT).show();
        view.findViewById(R.id.progress_view).setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Context getContext(){
        return getActivity();
    }

    protected void loginSuccess(Object object){
        try {
            Student student = (Student) object;
            Activity activity = getActivity();
            Intent intent = new Intent(activity,MainActivity.class);
            intent.putExtra("student",student);
            startActivity(intent);
            activity.finish();
        } catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void linkError() {
        Toast.makeText(getActivity(),ActivityUtil.getString(getActivity(),R.string.link_error),Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.register_btn:
                    Intent intent = new Intent(getActivity(),RegisterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.login_btn:
                    login();
                    break;
            }
        }
    };

    /**
     * 用户登录
     */
    private void login(){
        view.findViewById(R.id.progress_view).setVisibility(View.VISIBLE);
        Student student = new Student();
        EditText account = view.findViewById(R.id.account);
        EditText password = view.findViewById(R.id.password);
        student.setUserName(account.getText().toString());
        student.setPassword(password.getText().toString());
        presenter.login(getActivity(),student,handler);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ERROR:
                    linkError();
                    break;
                case PASSWORDERROR:
                    passwordError();
                    break;
                case LOGINSUCCESS:
                    loginSuccess(msg.obj);
                    break;
            }
        }
    };
}
