package com.example.amia.schoolrent.Fragment;

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

import com.example.amia.schoolrent.Bean.Eval;
import com.example.amia.schoolrent.Bean.EvalExtend;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.EvalAdapter;
import com.example.amia.schoolrent.Presenter.UserEvalContract;
import com.example.amia.schoolrent.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import static com.example.amia.schoolrent.Task.IdleTask.GET_EVAL;
import static com.example.amia.schoolrent.Task.IdleTask.USER_PUSH;

public class UserEvalFragment extends Fragment implements UserEvalContract.View{

    private View view;
    private PullLoadMoreRecyclerView recyclerView;
    private EvalAdapter adapter;
    private UserEvalContract.Presenter presenter;

    private EvalExtend extend;
    private Student student;

    public static UserEvalFragment newInstance(){
        UserEvalFragment userEvalFragment = new UserEvalFragment();
        return userEvalFragment;
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

        recyclerView.setGridLayout(1);
        adapter = new EvalAdapter(getActivity());
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
            List<Eval> idleInfos = (List<Eval>) object;

            if(idleInfos==null || idleInfos.size()<=0){
                if(extend.getPage() == 1){
                    view.findViewById(R.id.none_content).setVisibility(View.VISIBLE);
                }

                Toast.makeText(getActivity(),R.string.no_more,Toast.LENGTH_SHORT).show();
                return;
            }
            view.findViewById(R.id.none_content).setVisibility(View.GONE);
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

    private void refresh(){
        extend = new EvalExtend();
        extend.setPage(1);
        extend.setPageSize(6);
        extend.setUserId(student.getUserId());
        presenter.getUserEval(extend,handler);
    }

    private void loadMore(){
        presenter.getUserEval(extend,handler);
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
                case GET_EVAL:
                    finishLoadMore(msg.obj);
                    break;
            }
        }
    };

    @Override
    public void setPresenter(UserEvalContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
