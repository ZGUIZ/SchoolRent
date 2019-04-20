package com.example.amia.schoolrent.Activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.ForgotPassInterface;
import com.example.amia.schoolrent.Bean.PassWord;
import com.example.amia.schoolrent.Fragment.ForgotSendCodeFragment;
import com.example.amia.schoolrent.Fragment.ForgotSetPassFragment;
import com.example.amia.schoolrent.Presenter.ForgotPassContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.ForgotPassContractImpl;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.StudentTask;
import com.example.amia.schoolrent.Task.TaskImpl.StudentTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class ForgotPassActivity extends AppCompatActivity implements ForgotPassInterface {

    protected StudentTask studentTask;
    private String mail;
    private PassWord passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        studentTask = new StudentTaskImpl();
        init();
    }

    private void init(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.forgot_fragment);
        if(fragment == null){
            setSendCodeFragment();
        } else{
            ActivityUtil.replaceFragment(getSupportFragmentManager(),fragment,R.id.forgot_fragment);
        }
    }

    /**
     * 验证码认证成功
     * @param passWord
     */
    @Override
    public void codeValidateSuccess(PassWord passWord) {
        this.passWord = passWord;
        Fragment fragment = ForgotSetPassFragment.newInstance();
        ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.forgot_fragment);

        ForgotSetPassFragment setPassFragment = (ForgotSetPassFragment) fragment;
        //设置Presenter
        ForgotPassContract.Presenter presenter = new ForgotPassContractImpl(setPassFragment, studentTask);
        setPassFragment.setPresenter(presenter);
    }

    @Override
    public String getMail() {
        if(passWord == null) {
            return null;
        }
        return passWord.getMail();
    }

    @Override
    public PassWord getPassWord() {
        return passWord;
    }

    /**
     * 设置发送验证码页面
     */
    @Override
    public void setSendCodeFragment() {
        Fragment fragment = ForgotSendCodeFragment.newInstance();
        ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.forgot_fragment);

        ForgotSendCodeFragment sendCodeFragment = (ForgotSendCodeFragment) fragment;
        //设置Presenter
        ForgotPassContract.Presenter presenter = new ForgotPassContractImpl(sendCodeFragment, studentTask);
        sendCodeFragment.setPresenter(presenter);
    }
}
