package com.example.amia.schoolrent.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.ModifyInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.ModifyPasswordInterface;
import com.example.amia.schoolrent.Bean.PassWord;
import com.example.amia.schoolrent.Presenter.RetPasswordContract;
import com.example.amia.schoolrent.R;

import static com.example.amia.schoolrent.Task.StudentTask.RESET_ERROR;
import static com.example.amia.schoolrent.Task.StudentTask.RESET_SUECCESS;

public class ResetPasswordFragment extends Fragment implements ModifyPasswordInterface, RetPasswordContract.View {
    private View view;

    private RetPasswordContract.Presenter presenter;

    public static ResetPasswordFragment newInstance(){
        ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
        return resetPasswordFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.rest_password_layout,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init(){
        TextView resetView = view.findViewById(R.id.forgot_tv);
        final ModifyInterface modifyInterface = (ModifyInterface) getActivity();
        resetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(getContext());
                normalDialog.setTitle(R.string.forgot_title);
                normalDialog.setMessage(R.string.forgot_context);
                normalDialog.setPositiveButton(R.string.sure,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (modifyInterface.getFlags()){
                                    case R.id.password_reset_ll:
                                        presenter.forgotPassword(handler);
                                        break;
                                    case R.id.pay_password_reset_ll:
                                        presenter.forgotPayPssword(handler);
                                        break;
                                }
                            }
                        });
                normalDialog.setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                // 显示
                normalDialog.show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    @Override
    public PassWord getPassword() {

        EditText oldpasswordET = view.findViewById(R.id.old_password_et);
        String oldPassword = oldpasswordET.getText().toString().trim();
        if(TextUtils.isEmpty(oldPassword)){
            Toast.makeText(getActivity(),R.string.old_password_null,Toast.LENGTH_SHORT).show();
            return null;
        }
        EditText newPasswordET = view.findViewById(R.id.new_password_et);
        String newPassword = newPasswordET.getText().toString().trim();
        if(TextUtils.isEmpty(newPassword)){
            Toast.makeText(getActivity(),R.string.password_null,Toast.LENGTH_SHORT).show();
            return null;
        }
        EditText confirmPasswordET = view.findViewById(R.id.confirm_password_et);
        String confirmPassword = confirmPasswordET.getText().toString().trim();
        if(!newPassword.equals(confirmPassword)){
            Toast.makeText(getActivity(),R.string.password_not_match,Toast.LENGTH_SHORT).show();
            return null;
        }

        PassWord passWord = new PassWord();
        passWord.setOldPassword(oldPassword);
        passWord.setNewPassword(newPassword);
        passWord.setConfirmPaswword(confirmPassword);
        return passWord;
    }

    @Override
    public void setPresenter(RetPasswordContract.Presenter presenter) {
        this.presenter = presenter;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case RESET_SUECCESS:
                    Toast.makeText(getActivity(),(String)msg.obj,Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    break;
                case RESET_ERROR:
                    Toast.makeText(getActivity(),(String)msg.obj,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
