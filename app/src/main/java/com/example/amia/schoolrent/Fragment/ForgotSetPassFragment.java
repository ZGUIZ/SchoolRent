package com.example.amia.schoolrent.Fragment;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.ForgotPassInterface;
import com.example.amia.schoolrent.Activity.ForgotPassActivity;
import com.example.amia.schoolrent.Bean.PassWord;
import com.example.amia.schoolrent.Presenter.ForgotPassContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.KeyboardUtil;

import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Task.StudentTask.MODIFY_PASSWORD;

public class ForgotSetPassFragment extends Fragment implements ForgotPassContract.View {

    private View view;
    private EditText passwordEt;
    private EditText confirmEt;

    private ForgotPassContract.Presenter presenter;

    private PassWord passWord;

    public static ForgotSetPassFragment newInstance(){
        ForgotSetPassFragment setPassFragment=new ForgotSetPassFragment();
        return setPassFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forgot_pass_layout,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init(){
        //获得服务器返回的参数
        passWord = ((ForgotPassInterface) getActivity()).getPassWord();

        TextView next = view.findViewById(R.id.next_tv);
        next.setText(R.string.finish);
        //next.setOnClickListener(onClickListener);

        view.findViewById(R.id.next_layout).setOnClickListener(onClickListener);
        view.findViewById(R.id.back_layout).setOnClickListener(onClickListener);

        passwordEt = view.findViewById(R.id.password);
        confirmEt = view.findViewById(R.id.password_confirm);
    }

    protected void Snack(String msg){
        if("".equals(msg)){
            Snackbar.make(view,R.string.link_error,Snackbar.LENGTH_SHORT).show();
            return;
        }
        Snackbar.make(view,msg,Toast.LENGTH_SHORT).show();
    }

    protected void Snack(int msg){
        if("".equals(msg)){
            Snackbar.make(view,R.string.link_error,Snackbar.LENGTH_SHORT).show();
            return;
        }
        Snackbar.make(view,msg,Toast.LENGTH_SHORT).show();
    }

    private void updatePass(){
        KeyboardUtil keyboardUtil = KeyboardUtil.getInstance();
        keyboardUtil.hideKeyBoard(getContext(),passwordEt);
        keyboardUtil.hideKeyBoard(getContext(),confirmEt);

        String pass = passwordEt.getText().toString();
        String confirm = confirmEt.getText().toString();

        if(pass.equals("")){
           Snack(R.string.password_null);
           return;
        }

        if(!pass.equals(confirm)){
            Snack(R.string.password_not_match);
            return;
        }

        passWord.setNewPassword(pass);
        passWord.setConfirmPaswword(confirm);

        view.findViewById(R.id.progress_view).setVisibility(View.VISIBLE);

        presenter.modifyPassword(passWord,handler);
    }

    protected void updateSuccess(){
        Toast.makeText(getContext(),R.string.update_success,Toast.LENGTH_SHORT).show();
        view.findViewById(R.id.progress_view).setVisibility(View.GONE);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;
        presenter = null;
    }

    @Override
    public void linkError() {
        Toast.makeText(getContext(), R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(ForgotPassContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void back(){
        ((ForgotPassActivity)getActivity()).setSendCodeFragment();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.next_layout:
                    updatePass();
                    break;
                case R.id.back_layout:
                    back();
                    break;
            }
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            view.findViewById(R.id.progress_view).setVisibility(View.GONE);
            super.handleMessage(msg);
            switch (msg.what){
                case MODIFY_PASSWORD:
                    updateSuccess();
                    break;
                case ERROR:
                    try{
                        Snack((String)msg.obj);
                    } catch (Exception e){
                        e.printStackTrace();
                        linkError();
                    }
                    break;
            }
        }
    };
}
