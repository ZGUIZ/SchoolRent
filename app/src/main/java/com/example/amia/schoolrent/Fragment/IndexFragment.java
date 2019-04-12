package com.example.amia.schoolrent.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
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


import com.example.amia.schoolrent.Activity.AllClassifyActivity;
import com.example.amia.schoolrent.Activity.BaseAcitivity;
import com.example.amia.schoolrent.Activity.IdleInfoActivity;
import com.example.amia.schoolrent.Activity.SearchActivity;
import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.IdleInfoExtend;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.IdleAdapter;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.IndexClassifyAdapter;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.OnItemClickListener;
import com.example.amia.schoolrent.Presenter.MainContract;
import com.example.amia.schoolrent.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.ERRORWITHMESSAGE;
import static com.example.amia.schoolrent.Task.IdleTask.IDLE_ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.IDLE_SUCESS;
import static com.example.amia.schoolrent.Task.IdleTask.INDEX_CLASSIFY;
import static com.example.amia.schoolrent.Task.IdleTask.LOAD_MORE_ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.LOAD_MORE_SUCCESS;
import static com.example.amia.schoolrent.Util.COSUtil.PUT_PROGRESS;
import static com.example.amia.schoolrent.Util.COSUtil.RESULT_ERROR;
import static com.example.amia.schoolrent.Util.COSUtil.RESULT_SUCCESS;

public class IndexFragment extends Fragment implements MainContract.View{
    private static View view;
    private PullLoadMoreRecyclerView refreshLayout;
    private RecyclerView classifyView;
    private IndexClassifyAdapter adapter;
    private IdleAdapter idleAdapter;

    private MainContract.Presenter presenter;

    private List<Classify> classifyList;
    private IdleInfoExtend idleInfo;

    public static IndexFragment newInstance(){
        IndexFragment indexFragment = new IndexFragment();
        return indexFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.index_layout,container,false);
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    protected void init(){
        idleInfo = new IdleInfoExtend();

        //设置搜索按钮的点击事件
        view.findViewById(R.id.search_rl).setOnClickListener(onClickListener);

        classifyList = presenter.getCacheClassify();

        classifyView = view.findViewById(R.id.index_classify_rv);
        classifyView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        adapter = new IndexClassifyAdapter(classifyList,getActivity());
        adapter.setOnClickListener(new IndexClassifyAdapter.OnClickListener() {
            @Override
            public void onCLick(String id) {
                startSearchActivity(id);
            }

            @Override
            public void toClassifyAcitivy() {
                startClassifyActivity();
            }
        });
        classifyView.setAdapter(adapter);

        refreshLayout = view.findViewById(R.id.index_refresh_layout);
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
    }

    private void refresh(){
        idleInfo.setPage(1);
        //刷新分类信息
        presenter.getIndexClassify(idleInfo,handler);
    }

    private void loadMore(){
        presenter.getIdleByPages(idleInfo,handler);
    }

    private void finishFresh(){
        refreshLayout.setPullLoadMoreCompleted();
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
        }
    }

    /**
     * 打开详细页面
     * @param idleInfo
     */
    public void startInfoActivity(IdleInfo idleInfo){
        BaseAcitivity acitivity = (BaseAcitivity) getActivity();
        Intent intent = new Intent(acitivity, IdleInfoActivity.class);
        intent.putExtra("idleInfo",idleInfo);
        intent.putExtra("student",acitivity.getStudent());
        startActivity(intent);
    }

    public void startSearchActivity(){
        startSearchActivity(null);
    }

    public void startSearchActivity(String id){
        BaseAcitivity acitivity = (BaseAcitivity) getActivity();
        Intent intent = new Intent(acitivity, SearchActivity.class);
        if(id!=null && !"".equals(id)) {
            intent.putExtra("classifyId", id);
        }
        intent.putExtra("student",acitivity.getStudent());
        startActivity(intent);
    }

    public void startClassifyActivity(){
        BaseAcitivity activity = (BaseAcitivity) getActivity();
        Intent intent = new Intent(activity, AllClassifyActivity.class);
        intent.putExtra("student",activity.getStudent());
        startActivity(intent);
    }

    /**
     * 加载首页的分类信息
     * @param object
     */
    private void loadClassify(Object object){
        try {
            List<Classify> classifies = (List<Classify>) object;
            classifyList.clear();
            for(int i = 0;i<classifies.size();i++){
                classifyList.add(classifies.get(i));
            }
            adapter.notifyDataSetChanged();
        } catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    //获取到闲置信息
    protected void setIdleInfoList(Object o){
        idleInfo.setPage(2);
        idleAdapter.notifyDataSetChanged();
        try {
            List<IdleInfo> idleInfos = (List<IdleInfo>) o;
            idleAdapter.setIdleInfos(idleInfos);
            //idleAdapter.notifyDataSetChanged();
        } catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    private void errorWithMessage(Object o){
        try {
            String[] messages = (String[]) o;
            Toast.makeText(getActivity(),messages[0],Toast.LENGTH_SHORT).show();
        } catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finishFresh();
            switch (msg.what){
                case INDEX_CLASSIFY:
                    loadClassify(msg.obj);
                    break;
                case ERROR:
                    linkError();
                    break;
                case ERRORWITHMESSAGE:
                    errorWithMessage(msg.obj);
                    break;
                case PUT_PROGRESS: //上传图片进度回显
                    break;
                case RESULT_SUCCESS:  //上传图片成功
                    break;
                case RESULT_ERROR:  //上传图片失败
                    Toast.makeText(getActivity(),(String)msg.obj,Toast.LENGTH_SHORT).show();
                    break;
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
                case R.id.search_rl:
                    startSearchActivity();
                    break;
            }
        }
    };

    @Override
    public void linkError() {
        refreshLayout.setPullLoadMoreCompleted();
        Toast.makeText(getActivity(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
