package com.example.amia.schoolrent.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.RegisterInterface;
import com.example.amia.schoolrent.Activity.MainActivity;
import com.example.amia.schoolrent.Bean.Province;
import com.example.amia.schoolrent.Bean.School;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.LoginContract;
import com.example.amia.schoolrent.Presenter.StudentContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.KeyboardUtil;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.ProgressView;

import java.util.List;

import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.REGISTER_ERROR;
import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.REGISTER_SUCCESS;
import static com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl.CITY;
import static com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl.ERROR;
import static com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl.PROVINCE;
import static com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl.SCHOOL;

public class RegisterBaseFragment extends Fragment implements StudentContract.View {

    protected View view;

    private ListView listView;
    private EditText schoolText;
    private BottomSheetDialog dialog;  //学校选择弹出窗
    private ProgressView progressView; //进度条
    private View selectView; //学校选择的View

    protected StudentContract.Presenter presenter;
    protected LoginContract.Presenter loginPresenter;

    public RegisterBaseFragment() {
    }

    public static RegisterBaseFragment newInstance() {
        return new RegisterBaseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_base, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    protected void init(){
        //设置下一步按钮为完成
        TextView next = view.findViewById(R.id.next_tv);
        next.setText(R.string.finish);
        view.findViewById(R.id.next_layout).setOnClickListener(onClickListener);

        view.findViewById(R.id.back_layout).setOnClickListener(onClickListener);

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
                loginPresenter.getProvince(handler);
            }
        });
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
                    loginPresenter.getCity(provinceList.get(i),handler);
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
                    loginPresenter.getSchool(cityList.get(i),handler);
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
                     School school = schools.get(i);
                    schoolText.setText(school.getSchoolName());
                    ((RegisterInterface)getActivity()).setSchool(school);
                    dialog.cancel();
                }
            });
        }
    }

    void finishRegister(){
        showProgressBar();

        //获取填写的信息
        RegisterInterface registerInterface = (RegisterInterface) getActivity();
        EditText editText = view.findViewById(R.id.user_name);

        KeyboardUtil keyboardUtil = KeyboardUtil.getInstance();
        keyboardUtil.hideKeyBoard(getActivity(),editText);

        String userName = editText.getText().toString().trim();
        EditText pass = view.findViewById(R.id.password);
        String password = pass.getText().toString().trim();
        EditText passConfirm = view.findViewById(R.id.password_confirm);
        String passwordConfirm = passConfirm.getText().toString().trim();
        if("".equals(userName)){
            Snackbar.make(view,R.string.user_name_null,Snackbar.LENGTH_SHORT).show();
            return;
        }
        if("".equals(password)){
            Snackbar.make(view,R.string.password_null,Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(passwordConfirm)){
            Snackbar.make(view,R.string.password_not_match,Snackbar.LENGTH_SHORT).show();
            return;
        }

        registerInterface.setUserName(userName);
        registerInterface.setPassword(password);
        registerInterface.setConfirmPassword(password);
        presenter.register(registerInterface.getStudent(),handler);
    }

    protected void registerError(Object msg){
        hideProgressBar();
        try {
            String message = (String)msg;
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
            Snackbar.make(view, R.string.link_error, Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * 注册成功，跳转到列表页面
     * @param student
     */
    protected void registerSuccess(Student student){
        hideProgressBar();

        if(student == null){
            registerError(ActivityUtil.getString(getActivity(),R.string.password_error));
            return;
        }

        Intent intent = new Intent(getActivity(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //清空栈
        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("student",student);
        startActivity(intent);

        getActivity().finish();
    }

    /**
     * 显示进度条
     */
    protected void showProgressBar(){
        view.findViewById(R.id.progress_rl).setVisibility(View.VISIBLE);
        view.findViewById(R.id.progress_view).setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏进度条
     */
    protected void hideProgressBar(){
        view.findViewById(R.id.progress_view).setVisibility(View.GONE);
        view.findViewById(R.id.progress_rl).setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
        loginPresenter = null;
    }

    /**
     * 网络连接异常
     */
    @Override
    public void linkError() {
        Toast.makeText(getContext(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(StudentContract.Presenter presenter,LoginContract.Presenter loginPresenter) {
        this.presenter = presenter;
        this.loginPresenter = loginPresenter;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.back_layout:
                    ((RegisterInterface)getActivity()).loadMailFragment();
                    break;
                case R.id.next_layout:
                    finishRegister();
                    break;
            }
        }
    };

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
                case REGISTER_SUCCESS:
                    registerSuccess((Student) msg.obj);
                    break;
                case REGISTER_ERROR:
                    registerError(msg.obj);
                    break;
            }
        }
    };

    @Deprecated
    public void setPresenter(StudentContract.Presenter presenter) {
    }
}
