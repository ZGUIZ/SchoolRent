package com.example.amia.schoolrent.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.ModifyPasswordInterface;
import com.example.amia.schoolrent.Bean.PassWord;
import com.example.amia.schoolrent.R;

public class ResetPasswordFragment extends Fragment implements ModifyPasswordInterface {
    private View view;
    private EditText input;
    private int type;

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
}
