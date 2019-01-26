package com.example.amia.schoolrent.Fragment;

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
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.MainActivity;
import com.example.amia.schoolrent.Activity.RegisterActivity;
import com.example.amia.schoolrent.Bean.Province;
import com.example.amia.schoolrent.Bean.School;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.LoginContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.ProgressView;

import java.util.List;

import static com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl.CITY;
import static com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl.ERROR;
import static com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl.PROVINCE;
import static com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl.SCHOOL;

public class LoginFragment extends Fragment implements LoginContract.View {

    private static LoginFragment loginFragment;
    private static View view;
    private ListView listView;
    private EditText schoolText;
    private BottomSheetDialog dialog;  //学校选择弹出窗
    private ProgressView progressView; //进度条
    private View selectView; //学校选择的View
    private LoginContract.Presenter presenter;

    //所选的学校
    private School school;

    public static LoginFragment newInstance(){
        if(loginFragment == null){
            loginFragment = new LoginFragment();
        }
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
        //设置选择学校EditText点击事件
        schoolText=view.findViewById(R.id.school_input);
        schoolText.setKeyListener(null);
        schoolText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog != null) {
                    dialog.cancel();
                }
                dialog = new BottomSheetDialog(getActivity());
                selectView = getLayoutInflater().inflate(R.layout.school_selector_layout,null);
                progressView = selectView.findViewById(R.id.login_progress_view);
                dialog.setContentView(selectView);
                dialog.show();
                progressView.setVisibility(View.VISIBLE);
                presenter.getProvince(handler);
            }
        });

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

    public void toListPage(Student student) {
        Intent intent = new Intent(getActivity(),MainActivity.class);
        intent.putExtra("user",student);
        startActivity(intent);
        getActivity().finish();
    }

    public void passwordError() {
        Toast.makeText(getActivity(),ActivityUtil.getString(getActivity(),R.string.password_error),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Context getContext(){
        return getActivity();
    }


    public void setProvinceList(final List<Province> provinceList) {
        if(provinceList== null || provinceList.size() == 0){
            linkError();
            return;
        }

        listView = selectView.findViewById(R.id.school_list);

        ArrayAdapter<String> adapter;
        if(listView != null){
            //将其转换为数组
            String[] array = new String[provinceList.size()];
            for(int i = 0;i<provinceList.size();i++){
                array[i] = provinceList.get(i).getName();
            }
            adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,array);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    progressView.setVisibility(View.VISIBLE);
                    presenter.getCity(provinceList.get(i),handler);
                }
            });
        }

        //解决NestedScrollView和ListView滑动事件冲突
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                NestedScrollView scrollView = selectView.findViewById(R.id.school_nv);
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


    }

    public void setCityList(final List<Province> cityList) {
        if (cityList == null ||cityList.size() == 0){
            linkError();
            return;
        }
        ArrayAdapter<String> adapter;
        if(listView != null){
            //将其转换为数组
            String[] array = new String[cityList.size()];
            for(int i = 0;i<cityList.size();i++){
                array[i] = cityList.get(i).getName();
            }
            adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,array);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    progressView.findViewById(R.id.login_progress_view).setVisibility(View.VISIBLE);
                    presenter.getSchool(cityList.get(i),handler);
                }
            });
        }
    }

    public void setSchoolList(final List<School> schools) {
        ArrayAdapter<String> adapter;
        if(listView != null){
            //将其转换为数组
            String[] array = new String[schools.size()];
            for(int i = 0;i<schools.size();i++){
                array[i] = schools.get(i).getSchoolName();
            }
            adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,array);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   school = schools.get(i);
                   schoolText.setText(school.getSchoolName());
                   dialog.cancel();
                }
            });
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
        if(school == null || school.getSchoolId() == null ||"".equals(school.getSchoolId())){
            Toast.makeText(getActivity(),ActivityUtil.getString(getActivity(),R.string.null_school),Toast.LENGTH_SHORT).show();
            return;
        }
        view.findViewById(R.id.progress_view).setVisibility(View.VISIBLE);
        Student student = new Student();
        student.setSchoolId(school.getSchoolId());
        EditText account = view.findViewById(R.id.account);
        EditText password = view.findViewById(R.id.password);
        student.setStudentId(account.getText().toString());
        student.setPassword(password.getText().toString());
        presenter.login(getActivity(),student,handler);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressView.setVisibility(View.GONE);
            switch (msg.what){
                case ERROR:
                    linkError();
                    break;
                case PROVINCE:
                    setProvinceList((List<Province>) msg.obj);
                    break;
                case CITY:
                    setCityList((List<Province>) msg.obj);
                    break;
                case SCHOOL:
                    setSchoolList((List<School>) msg.obj);
                    break;
            }
        }
    };
}
