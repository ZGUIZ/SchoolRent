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

import com.example.amia.schoolrent.Activity.ActivityInterface.RegisterInterface;
import com.example.amia.schoolrent.Bean.KeyValue;
import com.example.amia.schoolrent.Presenter.LoginContract;
import com.example.amia.schoolrent.Presenter.StudentContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.KeyboardUtil;
import com.example.amia.schoolrent.Util.MailUtil;

import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.ERROR_WITH_MESSAGE;
import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.SEND_SUCCESS;
import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.VALIDATE_ERROR;
import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.VALIDATE_SUCCESS;

public class MailFragment extends Fragment implements StudentContract.View {
    protected static final int BTN_TIME_FLAG = 1000;

    private View view;

    protected Button sendBtn;

    protected StudentContract.Presenter presenter;

    protected String email;

    public static MailFragment newInstance(){
        MailFragment mailFragment=new MailFragment();
        return mailFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mail,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;
        presenter = null;
    }

    protected void init(){
        sendBtn = view.findViewById(R.id.send_mail_btn);
        view.findViewById(R.id.back_layout).setOnClickListener(onClickListener);
        sendBtn.setOnClickListener(onClickListener);
        view.findViewById(R.id.next_layout).setOnClickListener(onClickListener);

        String mail = ((RegisterInterface)getActivity()).getEMail();
        if(mail!=null && !"".equals(mail)){
            EditText editText = view.findViewById(R.id.mailAddress);
            editText.setText(mail);
        }
    }

    /**
     * 发送邮件
     */
    protected void sendMail(){
        EditText editText = view.findViewById(R.id.mailAddress);
        KeyboardUtil keyboardUtil = KeyboardUtil.getInstance();
        keyboardUtil.hideKeyBoard(getActivity(),editText);
        String address = editText.getText().toString().trim();
        //空邮箱不发送邮件
        if(address == null || "".equals(address)){
            Snackbar.make(view, R.string.mail_null, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(!MailUtil.isMail(address)){
            Snackbar.make(view, R.string.mail_format_error, Snackbar.LENGTH_SHORT).show();
            return;
        }
        presenter.sendRegisterMail(address,handler);
        email = address;
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

    /**
     * 服务器请求验证
     */
    protected void sendValidate(){
        EditText mailET = view.findViewById(R.id.mailAddress);
        String address = mailET.getText().toString().trim();
        //空邮箱
        if(address == null || "".equals(address)){
            Snackbar.make(view, R.string.mail_null, Snackbar.LENGTH_SHORT).show();
            return;
        }

        //空验证码
        EditText editText = view.findViewById(R.id.validate_code_et);
        String code = editText.getText().toString().trim();
        if(code == null || "".equals(code)){
            Snackbar.make(view,R.string.valid_code,Snackbar.LENGTH_SHORT).show();
            return;
        }
        //邮箱非法
        if(!MailUtil.isMail(address)){
            Snackbar.make(view, R.string.mail_format_error, Snackbar.LENGTH_SHORT).show();
            return;
        }

        view.findViewById(R.id.progress_view).setVisibility(View.VISIBLE);
        KeyValue keyValue = new KeyValue(address,code);
        presenter.validateMail(keyValue,handler);
    }

    protected void Snack(String msg){
        if("".equals(msg)){
            Snackbar.make(view,R.string.link_error,Snackbar.LENGTH_SHORT).show();
            return;
        }
        Snackbar.make(view,msg,Toast.LENGTH_SHORT).show();
    }

    protected View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.back_layout:
                    getActivity().finish();
                    break;
                case R.id.send_mail_btn:
                    sendMail();
                    break;
                case R.id.next_layout:
                    sendValidate();
                    break;
            }
        }
    };

    /**
     * 网络连接异常
     */
    @Override
    public void linkError() {
        Toast.makeText(getContext(),R.string.link_error,Toast.LENGTH_SHORT).show();
        view.findViewById(R.id.progress_view).setVisibility(View.GONE);
    }

    @Deprecated
    public void setPresenter(StudentContract.Presenter presenter, LoginContract.Presenter loginPresenter) {
    }

    /**
     * 验证成功
     */
    protected void validateSuccess(){
        //Toast.makeText(getContext(),"验证成功！",Toast.LENGTH_LONG).show();
        RegisterInterface registerInterface = (RegisterInterface)getActivity();
        registerInterface.setEMail(email);
        view.findViewById(R.id.progress_view).setVisibility(View.GONE);
    }

    protected void validateError(){
        Snackbar.make(view,R.string.mail_validate_error,Snackbar.LENGTH_SHORT).show();
        view.findViewById(R.id.progress_view).setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(StudentContract.Presenter presenter) {
        this.presenter = presenter;
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

    protected Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SEND_SUCCESS:
                    sendSuccess();
                    break;
                case ERROR_WITH_MESSAGE:
                    try {
                        Snack(((String[]) msg.obj)[0]);
                    }catch (ClassCastException e){
                        e.printStackTrace();
                    }
                    break;
                case BTN_TIME_FLAG:
                    setBtnTime((Integer) msg.obj);
                    break;
                case VALIDATE_SUCCESS:
                    validateSuccess();
                    break;
                case VALIDATE_ERROR:
                    validateError();
                    break;
            }

            super.handleMessage(msg);
        }
    };
}
