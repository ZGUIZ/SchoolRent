package com.example.amia.schoolrent.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Activity.ActivityInterface.IdleInfoInterface;
import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;

import java.util.List;

public class IdleInfoFragment extends Fragment {

    private View view;

    public static IdleInfoFragment newInstance(){
        IdleInfoFragment idleInfoFragment = new IdleInfoFragment();
        return idleInfoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_idle_info_layout,container,false);
        init();
        return view;
    }

    protected void init(){
        IdleInfoInterface idleInfoInterface = (IdleInfoInterface) getActivity();
        IdleInfo idleInfo = idleInfoInterface.getIdleInfo();
        Student student = idleInfo.getStudent();

        //加载发布者头像
        view.findViewById(R.id.back_ib).setOnClickListener(onClickListener);
        ImageView userIcon = view.findViewById(R.id.user_icon_riv);
        Glide.with(getActivity()).load(student.getUserIcon()).into(userIcon);

        //加载发布者用户信息
        TextView userName = view.findViewById(R.id.user_name_tv);
        userName.setText(student.getUserName());
        float score = student.getCredit();
        TextView credit = view.findViewById(R.id.credit_tv);
        credit.setText(ActivityUtil.getString(getActivity(),R.string.credit)+score);
        if(score<65){
            credit.setTextColor(Color.rgb(255,0,0));
        } else if(score <= 85) {
            credit.setTextColor(Color.rgb(0,238,118));
        } else {
            credit.setTextColor(Color.rgb(141,238,238));
        }

        //设置租金信息
        TextView deposit = view.findViewById(R.id.deposit_value_tv);
        deposit.setText(String.valueOf(idleInfo.getDeposit()));
        TextView rental = view.findViewById(R.id.rental_value_tv);
        rental.setText(String.valueOf(idleInfo.getRetal()));

        TextView idleInfoDes = view.findViewById(R.id.idle_info_detail);
        idleInfoDes.setText(idleInfo.getIdelInfo());

        loadImages(idleInfo.getPicList());
    }

    //动态生成ImageView
    private void loadImages(List<IdelPic> picList){
        LinearLayout linearLayout = view.findViewById(R.id.idle_info_ll);
        Context context = getContext();
        for(IdelPic pic : picList){
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(6,2,6,2);
            imageView.setLayoutParams(params);
            linearLayout.addView(imageView);
            Glide.with(context).load(pic.getPicUrl()).into(imageView);
        }
    }

    protected void closeActivity(){
        getActivity().finish();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.back_ib:
                    closeActivity();
                    break;
            }
        }
    };
}
