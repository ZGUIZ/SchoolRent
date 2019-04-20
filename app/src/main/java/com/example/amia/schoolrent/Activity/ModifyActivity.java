package com.example.amia.schoolrent.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.FillStudentInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.ModifyInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.ModifyPasswordInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Bean.PassWord;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.ModifyFragment;
import com.example.amia.schoolrent.Fragment.ModifyMailFragment;
import com.example.amia.schoolrent.Fragment.ModifyPhoneFragment;
import com.example.amia.schoolrent.Fragment.ResetPasswordFragment;
import com.example.amia.schoolrent.Presenter.ModifyMailContract;
import com.example.amia.schoolrent.Presenter.ModifyPhoneContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.ModifyMailContractImpl;
import com.example.amia.schoolrent.Presenter.PersenterImpl.ModifyPhoneContractImpl;
import com.example.amia.schoolrent.Presenter.PersenterImpl.ResetPasswordContractImpl;
import com.example.amia.schoolrent.Presenter.RetPasswordContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.StudentTask;
import com.example.amia.schoolrent.Task.TaskImpl.StudentTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Task.StudentTask.SEND_SMS;
import static com.example.amia.schoolrent.Task.StudentTask.UPDATE_MAIL;
import static com.example.amia.schoolrent.Task.StudentTask.UPDATE_PASSWORD_ERROR;
import static com.example.amia.schoolrent.Task.StudentTask.UPDATE_PAYPASSWORD_ERROR;
import static com.example.amia.schoolrent.Task.StudentTask.UPDATE_PHONE;
import static com.example.amia.schoolrent.Task.StudentTask.UPDATE_STUDENT_ERROR;
import static com.example.amia.schoolrent.Task.StudentTask.UPDATE_STUDENT_SUCCESS;

public class ModifyActivity extends AppCompatActivity implements StudentInterface, ModifyInterface {

    private Student student;
    private int flag;
    private TextView title;
    private FillStudentInterface fillStudentInterface;
    private ModifyPasswordInterface modifyPasswordInterface;
    private ModifyMailContract.View modifyMailView;
    private ModifyPhoneContract.View modifyPhoneView;

    private RelativeLayout progressBar;

    private StudentTask studentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        setToolBar();
        init();
    }

    protected void setToolBar(){
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(onClickListener);
    }

    protected void init(){
        title = findViewById(R.id.title);
        progressBar = findViewById(R.id.progress_view);

        Intent intent = getIntent();
        flag = intent.getIntExtra("type",0);
        student = (Student) intent.getSerializableExtra("student");

        studentTask = new StudentTaskImpl();

        switch (flag){
            case R.id.user_name_tv:
                title.setText(R.string.user_show_name);
                modifyBaseInfo();
                break;
            case R.id.real_name_tv:
                title.setText(R.string.real_name);
                modifyBaseInfo();
                break;
            case R.id.password_reset_ll:
                title.setText(R.string.password);
                modifyPassword();
                break;
            case R.id.pay_password_reset_ll:
                title.setText(R.string.pay_password);
                modifyPassword();
                break;
            case R.id.telephone_tv:
                title.setText(R.string.telephone);
                modifyPhoneInfo();
                break;
            case R.id.mail_tv:
                title.setText(R.string.mail);
                modifyValidateInfo();
                break;
            case R.id.student_id_tv:
                title.setText(R.string.student_id);
                modifyBaseInfo();
                break;
        }

        findViewById(R.id.finish_tv).setOnClickListener(onClickListener);
    }

    /**
     * 更改基本信息
     */
    private void modifyBaseInfo(){
        ModifyFragment fragment = (ModifyFragment) getSupportFragmentManager().findFragmentById(R.id.modify_fragment);
        if(fragment == null){
            fragment = ModifyFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.modify_fragment);
        }
        fillStudentInterface = fragment;
    }

    /**
     * 更改密码
     */
    private void modifyPassword(){
        ResetPasswordFragment fragment = (ResetPasswordFragment) getSupportFragmentManager().findFragmentById(R.id.modify_fragment);
        if(fragment == null){
            fragment = ResetPasswordFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.modify_fragment);
        }

        RetPasswordContract.Presenter presenter = new ResetPasswordContractImpl(fragment,studentTask);
        fragment.setPresenter(presenter);
        modifyPasswordInterface = fragment;
    }

    private void modifyValidateInfo(){
        ModifyMailFragment fragment = ModifyMailFragment.newInstance();
        ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.modify_fragment);
        modifyMailView = fragment;
        ModifyMailContract.Presenter presenter = new ModifyMailContractImpl(fragment,studentTask);
        fragment.setPresenter(presenter);
    }

    private void modifyPhoneInfo(){
        ModifyPhoneFragment fragment = ModifyPhoneFragment.newInstance();
        ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.modify_fragment);
        modifyPhoneView = fragment;
        ModifyPhoneContract.Presenter presenter = new ModifyPhoneContractImpl(fragment,studentTask);
        fragment.setPresenter(presenter);
    }

    @Override
    public Student getStudent() {
        return student;
    }

    protected void fillValue(){
        Student student = fillStudentInterface.fillStudent();
        if(student == null){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        studentTask.updateInfo(this,student,handler);
    }

    protected void changePassword(){
        PassWord passWord = modifyPasswordInterface.getPassword();
        if(passWord == null){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        studentTask.changePassword(this,passWord,handler);
    }

    protected void changePayPassword(){
        PassWord passWord = modifyPasswordInterface.getPassword();
        if(passWord == null){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        studentTask.changePayPassword(this,passWord,handler);
    }

    protected void modifyMail(){
        Student s = modifyMailView.getFill();
        if(s == null){
            return;
        }

        s.setUserId(student.getUserId());
        studentTask.updateMail(this,s,handler);
    }

    protected void modifyPhone(){
        Student s = modifyPhoneView.getFill();
        if(s == null){
            return;
        }

        s.setUserId(student.getUserId());
        studentTask.updateTelephone(this,s,handler);
    }


    protected void selectFillResult(){
        switch (flag){
            case R.id.user_name_tv:
            case R.id.real_name_tv:
            case R.id.student_id_tv:
                fillValue();
                break;
            case R.id.password_reset_ll:
                changePassword();
                break;
            case R.id.pay_password_reset_ll:
                changePayPassword();
                break;
            case R.id.telephone_tv:
                modifyPhone();
                break;
            case R.id.mail_tv:
                modifyMail();
                break;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.finish_tv:
                    selectFillResult();
                    break;
                default:
                    ModifyActivity.this.finish();
                    break;
            }

        }
    };


    @Override
    public int getFlags() {
        return flag;
    }

    private void linkError(){
        Toast.makeText(this,R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    protected void updatePasswordError(Object o){
        if(o!=null && o instanceof String){
            Toast.makeText(this,(String)o,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,R.string.password_update_error,Toast.LENGTH_SHORT).show();
        }
    }

    protected void updatePayPasswordError(Object o){
        if(o!=null && o instanceof String){
            Toast.makeText(this,(String)o,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,R.string.pay_password_update_error,Toast.LENGTH_SHORT).show();
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressBar.setVisibility(View.GONE);
            switch (msg.what){
                case UPDATE_PHONE:
                case UPDATE_STUDENT_SUCCESS:
                    Toast.makeText(ModifyActivity.this,R.string.update_success,Toast.LENGTH_SHORT).show();
                    ModifyActivity.this.finish();
                    break;
                case UPDATE_STUDENT_ERROR:
                    Toast.makeText(ModifyActivity.this,R.string.user_update_error,Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_PASSWORD_ERROR:
                    updatePasswordError(msg.obj);
                    break;
                case UPDATE_PAYPASSWORD_ERROR:
                    updatePayPasswordError(msg.obj);
                    break;
                case UPDATE_MAIL:
                    Toast.makeText(ModifyActivity.this,R.string.update_success,Toast.LENGTH_SHORT).show();
                    ModifyActivity.this.finish();
                    break;
                case ERROR:
                    try{
                        if(modifyMailView!=null) {
                            modifyMailView.Snack((String) msg.obj);
                        }
                        if(modifyPhoneView != null){
                            modifyPhoneView.Snack((String)msg.obj);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        linkError();
                    }
                    break;
            }
        }
    };
}
