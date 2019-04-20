package com.example.amia.schoolrent.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.ModifyMailContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.KeyboardUtil;
import com.example.amia.schoolrent.Util.MailUtil;

import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.ERROR_WITH_MESSAGE;
import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.SEND_SUCCESS;
import static com.example.amia.schoolrent.Task.IdleTask.ERROR;

public class ModifyMailFragment extends Fragment implements ModifyMailContract.View {
    protected static final int BTN_TIME_FLAG = 1000;

    private View view;

    private Student student;

    private EditText mailEt;
    private EditText codeEt;
    private Button sendBtn;

    private ModifyMailContract.Presenter presenter;

    public static ModifyMailFragment newInstance(){
        ModifyMailFragment modifyFragment = new ModifyMailFragment();
        return modifyFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_modify_mail_layout,container,false);
        init();
        return view;
    }

    private void init(){
        sendBtn = view.findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode();
            }
        });
        student = new Student();

        mailEt = view.findViewById(R.id.text);
        codeEt = view.findViewById(R.id.code);
    }

    private void sendCode(){
        KeyboardUtil keyboardUtil = KeyboardUtil.getInstance();
        keyboardUtil.hideKeyBoard(getActivity(),mailEt);
        keyboardUtil.hideKeyBoard(getActivity(),codeEt);

        String address = mailEt.getText().toString().trim();
        if("".equals(address)){
            Snack(R.string.mail_null);
            return;
        }

        //邮箱非法
        if(!MailUtil.isMail(address)){
            Snack(R.string.mail_format_error);
            return;
        }

        presenter.sendCode(address,handler);
    }

    public void Snack(String msg){
        if("".equals(msg)){
            Snackbar.make(view,R.string.link_error,Snackbar.LENGTH_SHORT).show();
            return;
        }
        Snackbar.make(view,msg,Toast.LENGTH_SHORT).show();
    }

    public void Snack(int msg){
        Snackbar.make(view,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    /**
     * 发送邮件成功
     */
    protected void sendSuccess(){
        Snackbar.make(view, R.string.send_mail_success, Snackbar.LENGTH_SHORT).show();
        sendBtn.setClickable(false);
        //30秒后可重新发送邮件
        new Thread(new Runnable() {
            private int lastTime = 30;
            @Override
            public void run() {
                while(lastTime>=0) {
                    try {
                        Message msg = handler.obtainMessage();
                        msg.what = BTN_TIME_FLAG;
                        msg.obj = lastTime;
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                        lastTime--;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    protected void setBtnTime(int time){
        if(time == 0){
            sendBtn.setClickable(true);
            sendBtn.setTextColor(Color.rgb(0,0,0));
            sendBtn.setText(R.string.send_valid_code);
            return;
        }
        sendBtn.setTextColor(Color.rgb(120,120,120));
        sendBtn.setText(time+"秒");
    }

    @Override
    public void linkError() {
        Toast.makeText(getContext(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public Student getFill() {
        KeyboardUtil keyboardUtil = KeyboardUtil.getInstance();
        keyboardUtil.hideKeyBoard(getActivity(),mailEt);
        keyboardUtil.hideKeyBoard(getActivity(),codeEt);

        String address = mailEt.getText().toString().trim();
        if("".equals(address)){
            Snack(R.string.mail_null);
            return null;
        }

        //邮箱非法
        if(!MailUtil.isMail(address)){
            Snack(R.string.mail_format_error);
            return null;
        }
        String code = codeEt.getText().toString().trim();
        if("".equals(code)){
            Snack(R.string.valid_code);
            return null;
        }

        student.setEmail(address);
        student.setCode(code);
        return student;
    }

    /**
     * 验证成功
     */
    protected void validateSuccess(){
        //Toast.makeText(getContext(),"验证成功！",Toast.LENGTH_LONG).show();
        Snack(R.string.behavior_success);
        view.findViewById(R.id.progress_view).setVisibility(View.GONE);
    }

    protected void validateError(){
        Snackbar.make(view,R.string.mail_validate_error,Snackbar.LENGTH_SHORT).show();
        view.findViewById(R.id.progress_view).setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(ModifyMailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SEND_SUCCESS:
                    sendSuccess();
                    break;
                case ERROR:
                case ERROR_WITH_MESSAGE:
                    try {
                        Snack(((String[]) msg.obj)[0]);
                    }catch (ClassCastException e){
                        e.printStackTrace();
                        try {
                            Snack((String) msg.obj);
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    break;
                case BTN_TIME_FLAG:
                    setBtnTime((Integer) msg.obj);
                    break;
            }
        }
    };
}
