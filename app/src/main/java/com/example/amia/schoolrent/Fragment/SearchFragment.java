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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Activity.BaseAcitivity;
import com.example.amia.schoolrent.Activity.IdleInfoActivity;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.IdleInfoExtend;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.IdleAdapter;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.IndexClassifyAdapter;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.OnItemClickListener;
import com.example.amia.schoolrent.Presenter.SearchContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.KeyboardUtil;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import static com.example.amia.schoolrent.Task.IdleTask.IDLE_ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.IDLE_SUCESS;
import static com.example.amia.schoolrent.Task.IdleTask.LOAD_MORE_ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.LOAD_MORE_SUCCESS;

public class SearchFragment extends Fragment implements SearchContract.View{

    private View view;

    private PullLoadMoreRecyclerView refreshLayout;
    private IdleAdapter idleAdapter;
    private EditText input;


    private IdleInfoExtend idleInfo;
    private SearchContract.Presenter presenter;

    public SearchFragment(){
        super();
        idleInfo = new IdleInfoExtend();
    }

    public static SearchFragment newInstance(){
        SearchFragment searchFragment = new SearchFragment();
        return searchFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_layout,container,false);
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void init(){
        refreshLayout = view.findViewById(R.id.info_recycler);
        refreshLayout.setGridLayout(2);
        idleAdapter = new IdleAdapter(getActivity());
        idleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemCLick(View view, int position) {
                startInfoActivity(idleAdapter.getIdleInfoByPosition(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        refreshLayout.setAdapter(idleAdapter);
        refreshLayout.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });

        refreshLayout.refresh();
        refreshLayout.setIsRefresh(true);

        view.findViewById(R.id.back_btn).setOnClickListener(onClickListener);
        view.findViewById(R.id.search_iv).setOnClickListener(onClickListener);

        input = view.findViewById(R.id.search_tv);
    }

    private void refresh(){
        idleInfo.setPage(1);
        //刷新分类信息
        presenter.getIdleByPages(idleInfo,handler);
    }

    private void loadMore(){
        presenter.getIdleByPages(idleInfo,handler);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    /**
     * 打开详细页面
     * @param idleInfo
     */
    public void startInfoActivity(IdleInfo idleInfo){
        StudentInterface studentInterface = (StudentInterface) getActivity();
        Intent intent = new Intent( getActivity(), IdleInfoActivity.class);
        intent.putExtra("idleInfo",idleInfo);
        intent.putExtra("student",studentInterface.getStudent());
        startActivity(intent);
    }

    @Override
    public void setClassifyId(String id) {
        idleInfo.setClassifyId(id);
    }

    @Override
    public void linkError() {
        Toast.makeText(getContext(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        this.presenter = presenter;
    }

    //获取到闲置信息
    protected void setIdleInfoList(Object o){
        idleInfo.setPage(2);
        idleAdapter.notifyDataSetChanged();
        try {
            List<IdleInfo> idleInfos = (List<IdleInfo>) o;
            idleAdapter.setIdleInfos(idleInfos);
        } catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    private void finishLoadMore(Object object){
        refreshLayout.setPullLoadMoreCompleted();
        try{
            List<IdleInfo> idleInfos = (List<IdleInfo>) object;

            if(idleInfos==null || idleInfos.size()<=0){
                Toast.makeText(getActivity(),R.string.no_more,Toast.LENGTH_SHORT).show();
                return;
            }

            idleInfo.setPage(idleInfo.getPage() + 1);
            idleAdapter.addIdleInfos(idleInfos);
        } catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    private void search(){
        //填充数据
        idleInfo.setPage(1);
        idleInfo.setSearch(input.getText().toString());

        //刷新数据
        refreshLayout.refresh();
        refreshLayout.setIsRefresh(true);

        //关闭键盘
        KeyboardUtil keyboardUtil = KeyboardUtil.getInstance();
        keyboardUtil.hideKeyBoard(getContext(),input);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case IDLE_SUCESS:
                    setIdleInfoList(msg.obj);
                    break;
                case IDLE_ERROR:
                    linkError();
                    break;
                case LOAD_MORE_SUCCESS:
                    finishLoadMore(msg.obj);
                    break;
                case LOAD_MORE_ERROR:
                    linkError();
                    break;
            }
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.back_btn:
                    getActivity().finish();
                    break;
                case R.id.search_iv:
                    search();
                    break;
            }
        }
    };
}
