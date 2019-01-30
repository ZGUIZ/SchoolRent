package com.example.amia.schoolrent.Activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.amia.schoolrent.Activity.ActivityInterface.RegisterInterface;
import com.example.amia.schoolrent.Bean.School;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.MailFragment;
import com.example.amia.schoolrent.Fragment.RegisterBaseFragment;
import com.example.amia.schoolrent.Presenter.LoginContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.LoginContractImpl;
import com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl;
import com.example.amia.schoolrent.Presenter.StudentContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.SchoolTask;
import com.example.amia.schoolrent.Task.StudentTask;
import com.example.amia.schoolrent.Task.TaskImpl.SchoolTaskImpl;
import com.example.amia.schoolrent.Task.TaskImpl.StudentTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class RegisterActivity extends AppCompatActivity implements RegisterInterface {

    protected Student student =new Student();

    protected StudentTask studentTask;
    protected SchoolTask schoolTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        schoolTask = new SchoolTaskImpl();
        studentTask = new StudentTaskImpl();
        //loadMailFragment();
        loadBaseFragment();
    }

    public void loadMailFragment(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.register_fragment);
        if(fragment == null){
            fragment = MailFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.register_fragment);
        } else{
            fragment = MailFragment.newInstance();
            ActivityUtil.replaceFragment(getSupportFragmentManager(),fragment,R.id.register_fragment);
        }
        MailFragment mailFragment = (MailFragment) fragment;
        //设置Presenter
        StudentContract.Presenter presenter = new StudentContractImpl(mailFragment,studentTask);
        mailFragment.setPresenter(presenter);
    }

    protected void loadBaseFragment(){
        RegisterBaseFragment fragment = RegisterBaseFragment.newInstance();
        ActivityUtil.replaceFragment(getSupportFragmentManager(),fragment,R.id.register_fragment);
        StudentContract.Presenter presenter = new StudentContractImpl(fragment,studentTask);
        LoginContract.Presenter loginPresenter = new LoginContractImpl(fragment,schoolTask,studentTask);
        fragment.setPresenter(presenter,loginPresenter);
    }

    @Override
    public void setEMail(String eMail){
        student.setEmail(eMail);
        //设置界面为填写基本信息的Fragment
        loadBaseFragment();
    }

    @Override
    public void setSchool(School school){
        student.setSchoolId(school.getSchoolId());
    }

    @Override
    public void setUserName(String userName) {
        student.setUserName(userName);
    }

    @Override
    public void setPassword(String password) {
        student.setPassword(password);
    }

    @Override
    public Student getStudent() {
        return student;
    }

    @Override
    public String getEMail() {
        return student.getEmail();
    }

    public void setConfirmPassword(String confirmPassword) {
        student.setConfirmPassword(confirmPassword);;
    }

    public void setConfirmPayPassword(String confirmPayPassword) {
        student.setConfirmPassword(confirmPayPassword);
    }
}
