package com.example.amia.schoolrent.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.amia.schoolrent.Activity.ActivityInterface.FillStudentInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.ModifyInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.KeyboardUtil;

public class ModifyFragment extends Fragment implements FillStudentInterface {
    private View view;
    private EditText input;
    private int type;

    private Student student;

    public static ModifyFragment newInstance(){
        ModifyFragment modifyFragment = new ModifyFragment();
        return modifyFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.modify_fragment,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        StudentInterface studentInterface = (StudentInterface) getActivity();
        student = studentInterface.getStudent();
        ModifyInterface modifyInterface = (ModifyInterface) getActivity();
        type = modifyInterface.getFlags();
        input = view.findViewById(R.id.text);
        switch (type){
            case R.id.user_name_tv:
                input.setText(student.getUserName());
                break;
            case R.id.real_name_tv:
                input.setText(student.getRealName());
                break;
        }
        view.findViewById(R.id.clear_btn).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.clear_btn){
                input.setText("");
            }
        }
    };

    @Override
    public Student fillStudent() {
        Student student = new Student();
        KeyboardUtil keyboardUtil = KeyboardUtil.getInstance();
        keyboardUtil.hideKeyBoard(getActivity(),input);

        String str = input.getText().toString();
        if(TextUtils.isEmpty(str)){
            Snackbar.make(view,R.string.null_fill,Snackbar.LENGTH_SHORT).show();
            return null;
        }
        switch (type){
            case R.id.user_name_tv:
                student.setUserName(str);
                break;
            case R.id.real_name_tv:
                student.setRealName(str);
                break;
        }
        return student;
    }
}
