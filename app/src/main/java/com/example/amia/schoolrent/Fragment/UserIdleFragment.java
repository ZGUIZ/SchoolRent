package com.example.amia.schoolrent.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.BaseAcitivity;
import com.example.amia.schoolrent.Activity.IdleInfoActivity;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.IdleInfoExtend;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.IdleAdapter;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.OnItemClickListener;
import com.example.amia.schoolrent.Presenter.UserIdleContract;
import com.example.amia.schoolrent.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import static com.example.amia.schoolrent.Task.IdleTask.USER_PUSH;

public class UserIdleFragment extends Fragment implements UserIdleContract.View{

    private View view;
    private PullLoadMoreRecyclerView recyclerView;
    private IdleAdapter adapter;
    private UserIdleContract.Presenter presenter;

    private IdleInfoExtend extend;
    private Student student;

    public static UserIdleFragment newInstance(){
        UserIdleFragment idleFragment = new UserIdleFragment();
        return idleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_idle_layout,container,false);
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    protected void init(){
        recyclerView = view.findViewById(R.id.index_refresh_layout);

        recyclerView.setGridLayout(2);
        adapter = new IdleAdapter(getActivity());
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemCLick(View view, int position) {
                startInfoActivity(adapter.getIdleInfoByPosition(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        recyclerView.refresh();
        recyclerView.setIsRefresh(true);
    }

    private void finishLoadMore(Object object){
        recyclerView.setPullLoadMoreCompleted();
        try{
            List<IdleInfo> idleInfos = (List<IdleInfo>) object;

            if(idleInfos==null || idleInfos.size()<=0){
                Toast.makeText(getActivity(),R.string.no_more,Toast.LENGTH_SHORT).show();
                return;
            }
            if(extend.getPage() == 1){
                adapter.setIdleInfos(idleInfos);
            } else{
                adapter.addIdleInfos(idleInfos);
            }
            extend.setPage(extend.getPage() + 1);

        } catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    /**
     * 打开详细页面
     * @param idleInfo
     */
    public void startInfoActivity(IdleInfo idleInfo){
        Intent intent = new Intent(getActivity(), IdleInfoActivity.class);
        intent.putExtra("idleInfo",idleInfo);
        intent.putExtra("student",student);
        startActivity(intent);
    }

    private void refresh(){
        extend = new IdleInfoExtend();
        extend.setPage(1);
        extend.setPageSize(6);
        extend.setUserId(student.getUserId());
        presenter.getUserIdle(extend,handler);
    }

    private void loadMore(){
        presenter.getUserIdle(extend,handler);
    }

    public void setPresenter(UserIdleContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public void linkError() {
        Toast.makeText(getContext(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case USER_PUSH:
                    finishLoadMore(msg.obj);
                    break;
            }
        }
    };
}
