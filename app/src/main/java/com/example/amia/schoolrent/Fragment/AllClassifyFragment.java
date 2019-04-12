package com.example.amia.schoolrent.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Activity.BaseAcitivity;
import com.example.amia.schoolrent.Activity.SearchActivity;
import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.IndexClassifyAdapter;
import com.example.amia.schoolrent.Presenter.ClassifyContract;
import com.example.amia.schoolrent.Presenter.IdleInfoContract;
import com.example.amia.schoolrent.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.amia.schoolrent.Task.IdleTask.CLASSIFY_ALL;
import static com.example.amia.schoolrent.Task.IdleTask.ERROR;

public class AllClassifyFragment extends Fragment implements ClassifyContract.View {

    private View view;
    private ClassifyContract.Presenter presenter;

    private RecyclerView recyclerView;
    private IndexClassifyAdapter adapter;

    private List<Classify> classifyList;

    public static AllClassifyFragment newInstance(){
        AllClassifyFragment classifyFragment = new AllClassifyFragment();
        return classifyFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_classify_layout,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init(){
        classifyList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        adapter = new IndexClassifyAdapter(classifyList,getActivity());
        adapter.setOnClickListener(new IndexClassifyAdapter.OnClickListener() {
            @Override
            public void onCLick(String id) {
                startSearchActivity(id);
            }

            @Override
            public void toClassifyAcitivy() {
            }
        });
        recyclerView.setAdapter(adapter);

        presenter.queryClassify(handler);
    }

    public void startSearchActivity(String id){
        StudentInterface acitivity = (StudentInterface) getActivity();
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("classifyId", id);
        intent.putExtra("student",acitivity.getStudent());
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    private void loadClassifySuccess(Object o){
        try{
            List<Classify> classifies = (List<Classify>) o;
            adapter.setList(classifies);
        } catch (Exception e){
            e.printStackTrace();
            linkError();
        }
    }

    @Override
    public void linkError() {
        Toast.makeText(getContext(), R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(ClassifyContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CLASSIFY_ALL:
                    loadClassifySuccess(msg.obj);
                    break;
                case ERROR:
                    linkError();
                    break;
            }
        }
    };
}
