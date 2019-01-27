package com.example.amia.schoolrent.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amia.schoolrent.R;

public class MailFragment extends Fragment {
    private View view;

    private static MailFragment mailFragment;

    public static MailFragment newInstance(){
        if(mailFragment == null){
            mailFragment=new MailFragment();
        }
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

    protected void init(){
        view.findViewById(R.id.back_layout).setOnClickListener(onClickListener);
    }

    protected View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.back_layout:
                    getActivity().finish();
                    break;
            }
        }
    };
}
