package com.example.amia.schoolrent.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amia.schoolrent.Presenter.PushIdleContract;
import com.example.amia.schoolrent.R;

public class PushIdleFragment extends Fragment implements PushIdleContract.View {

    private View view;
    private PushIdleContract.Presenter presenter;

    public static PushIdleFragment newInstance(){
        PushIdleFragment pushIdleFragment = new PushIdleFragment();
        return pushIdleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_push_idle_layout,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    protected void init(){
        view.findViewById(R.id.back_button).setOnClickListener(onClickListener);
    }

    protected void leavePage(){
        //对话框，是否退出

        getActivity().finish();
    }

    @Override
    public void setPresenter(PushIdleContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.back_button:
                    leavePage();
                    break;
            }
        }
    };
}
