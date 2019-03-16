package com.example.amia.schoolrent.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amia.schoolrent.Bean.Capital;
import com.example.amia.schoolrent.Bean.CheckStatement;
import com.example.amia.schoolrent.Bean.CheckStatementExtend;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.CheckStateAdapter;
import com.example.amia.schoolrent.Presenter.CheckStateContract;
import com.example.amia.schoolrent.Presenter.IdleInfoContract;
import com.example.amia.schoolrent.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import static com.example.amia.schoolrent.Task.CheckStatementTask.CHECK_LIST;
import static com.example.amia.schoolrent.Task.StudentTask.GET_CAPITAL;

public class CheckStateFragment extends Fragment implements CheckStateContract.View {

    private View view;
    private CheckStateContract.Presenter presenter;

    private PullLoadMoreRecyclerView recyclerView;
    private CheckStateAdapter adapter;

    private CheckStatementExtend extend;

    public static CheckStateFragment newInstance(){
        CheckStateFragment fragment = new CheckStateFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_check_layout,container,false);
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    protected void init(){
        presenter.getMyCapital(handler);

        extend = new CheckStatementExtend();

        recyclerView = view.findViewById(R.id.check_refresh_layout);
        recyclerView.setGridLayout(1);
        adapter = new CheckStateAdapter(getActivity());
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

    private void refresh(){
        extend.setPage(1);
        //刷新分类信息
        presenter.queryMyCheck(extend,handler);
    }

    private void loadCheckSuccess(Object o){
        recyclerView.setPullLoadMoreCompleted();
        try{
            List<CheckStatement> checkStatements = (List<CheckStatement>) o;
            if(checkStatements == null || checkStatements.size()<=0){
                Toast.makeText(getActivity(),R.string.no_more,Toast.LENGTH_SHORT).show();
                return;
            }
            if(extend.getPage() == 1) {
                adapter.setDatas(checkStatements);
            } else {
                adapter.addDatas(checkStatements);
            }
            extend.setPage(extend.getPageSize()+1);
        } catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    private void loadMore(){
        presenter.queryMyCheck(extend,handler);
    }

    private void setCapital(Capital capital){
        TextView textView = view.findViewById(R.id.amount);
        textView.setText(String.valueOf(capital.getCapital()));
    }

    private void loadMyCaptialSuccess(Object o){
        try{
            Capital capital = (Capital) o;
            setCapital(capital);
        } catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    @Override
    public void linkError() {
        Toast.makeText(getContext(), R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(CheckStateContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void errorWithMessge(Object o){
        recyclerView.setPullLoadMoreCompleted();
        if(o == null){
            linkError();
            return;
        }
        try{
            String str = (String) o;
            Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            linkError();
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CHECK_LIST:
                    loadCheckSuccess(msg.obj);
                    break;
                case GET_CAPITAL:
                    loadMyCaptialSuccess(msg.obj);
                    break;
                default:
                    errorWithMessge(msg.obj);
                    break;
            }
            super.handleMessage(msg);

        }
    };
}
