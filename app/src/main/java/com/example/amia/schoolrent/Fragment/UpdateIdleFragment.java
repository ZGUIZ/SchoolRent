package com.example.amia.schoolrent.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.amia.schoolrent.Presenter.UpdateIdleContract;
import com.example.amia.schoolrent.R;

public class UpdateIdleFragment extends Fragment implements UpdateIdleContract.View {
    private static View view;
    private UpdateIdleContract.Presenter presenter;


    public static UpdateIdleFragment newInstance(){
        UpdateIdleFragment updateIdleFragment = new UpdateIdleFragment();
        return updateIdleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_push_idle_layout,container,false);
        return view;
    }

    @Override
    public void linkError() {
        Toast.makeText(getContext(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(UpdateIdleContract.Presenter loginPresenter) {
        this.presenter = loginPresenter;
    }
}
