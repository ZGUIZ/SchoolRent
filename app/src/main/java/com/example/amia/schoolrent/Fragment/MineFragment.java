package com.example.amia.schoolrent.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Activity.BaseAcitivity;
import com.example.amia.schoolrent.Activity.BaseInfoActivity;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;

public class MineFragment extends Fragment {
    private View view;

    protected boolean isHideName;

    public static MineFragment newInstance(){
        MineFragment mineFragment=new MineFragment();
        return mineFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mine_layout,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    protected void init(){
        BaseAcitivity baseAcitivity = (BaseAcitivity) getActivity();
        Student student = baseAcitivity.getStudent();

        TextView userName = view.findViewById(R.id.user_name_tv);
        userName.setText(student.getUserName());

        TextView realName = view.findViewById(R.id.real_name_tv);
        String name = student.getRealName();
        //设置名称显示形式
        String newName = name;
        if(isHideName && name!= null &&name.length()>2){
            newName = newName.substring(0,1);
            for(int i = 1;i<name.length();i++){
                newName = newName+"*";
            }
        }

        realName.setText(newName);

        TextView credit = view.findViewById(R.id.credit_tv);
        credit.setText(ActivityUtil.getString(baseAcitivity,R.string.credit)+String.valueOf(student.getCredit()));

        ImageView imageView = view.findViewById(R.id.user_icon);
        Glide.with(getActivity()).load(student.getUserIcon()).into(imageView);

        view.findViewById(R.id.user_info_layout).setOnClickListener(onClickListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;
    }

    protected void loadInfoActivity(){
        BaseAcitivity baseAcitivity = (BaseAcitivity) getActivity();
        Student student = baseAcitivity.getStudent();
        Intent intent = new Intent(baseAcitivity, BaseInfoActivity.class);
        intent.putExtra("student",student);
        startActivity(intent);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.user_info_layout:
                    loadInfoActivity();
                    break;
            }
        }
    };
}
