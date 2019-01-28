package com.example.amia.schoolrent.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.RegisterInterface;
import com.example.amia.schoolrent.Presenter.StudentContract;
import com.example.amia.schoolrent.R;

public class RegisterBaseFragment extends Fragment implements StudentContract.View {

    protected View view;

    protected StudentContract.Presenter presenter;

    public RegisterBaseFragment() {
    }

    public static RegisterBaseFragment newInstance() {
        return new RegisterBaseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_base, container, false);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    /**
     * 网络连接异常
     */
    @Override
    public void linkError() {
        Toast.makeText(getContext(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(StudentContract.Presenter presenter) {
        this.presenter = presenter;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.back_layout:
                    ((RegisterInterface)getActivity()).loadMailFragment();
                    break;
            }
        }
    };
}
