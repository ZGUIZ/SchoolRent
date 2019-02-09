package com.example.amia.schoolrent.Fragment;

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


import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.IdleInfoExtend;
import com.example.amia.schoolrent.Bean.MapKeyValue;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.IndexClassifyAdapter;
import com.example.amia.schoolrent.Presenter.MainContract;
import com.example.amia.schoolrent.R;
import com.ufo.dwrefresh.view.DWRefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.amia.schoolrent.Task.IdleTask.CLASSIFY_ICON;
import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.ERRORWITHMESSAGE;
import static com.example.amia.schoolrent.Task.IdleTask.IDLE_ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.IDLE_SUCESS;
import static com.example.amia.schoolrent.Task.IdleTask.INDEX_CLASSIFY;
import static com.example.amia.schoolrent.Util.COSUtil.PUT_PROGRESS;
import static com.example.amia.schoolrent.Util.COSUtil.RESULT_ERROR;
import static com.example.amia.schoolrent.Util.COSUtil.RESULT_SUCCESS;

public class IndexFragment extends Fragment implements MainContract.View{
    private static View view;
    private DWRefreshLayout refreshLayout;
    private RecyclerView classifyView;
    private IndexClassifyAdapter adapter;

    private MainContract.Presenter presenter;

    private List<Classify> classifyList;
    private Map<String, Bitmap> iconMap;
    private IdleInfoExtend idleInfo;

    public static IndexFragment newInstance(){
        IndexFragment indexFragment = new IndexFragment();
        return indexFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.index_layout,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    protected void init(){
        idleInfo = new IdleInfoExtend();

        iconMap = new HashMap<>();
        classifyList = presenter.getCacheClassify();

        classifyView = view.findViewById(R.id.index_classify_rv);
        classifyView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        adapter = new IndexClassifyAdapter(classifyList,getActivity());
        adapter.setIconMap(iconMap);
        classifyView.setAdapter(adapter);

        refreshLayout = view.findViewById(R.id.index_refresh_layout);
        refreshLayout.setOnRefreshListener(new DWRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });

        //打开时刷新页面
        refreshLayout.setRefresh(true);
        refresh();
    }

    private void refresh(){
        //刷新分类信息
        presenter.getIndexClassify(handler);
    }

    private void loadMore(){
        presenter.getIdleByPages(idleInfo,handler);
    }

    private void finishFresh(){
        refreshLayout.setRefresh(false);
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

    private void loadIcon(Object object){
        try{
            MapKeyValue<Bitmap> keyValue = (MapKeyValue<Bitmap>) object;
            iconMap.put(keyValue.getId(),keyValue.getData());
            adapter.notifyDataSetChanged();
        } catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //presenter = null;
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
                case CLASSIFY_ICON:
                    loadIcon(msg.obj);
                    break;
                case PUT_PROGRESS: //上传图片进度回显

                    break;
                case RESULT_SUCCESS:  //上传图片成功

                    break;
                case RESULT_ERROR:  //上传图片失败
                    Toast.makeText(getActivity(),(String)msg.obj,Toast.LENGTH_SHORT).show();
                    break;
                case IDLE_SUCESS:

                    break;
                case IDLE_ERROR:

                    break;
            }
        }
    };

    @Override
    public void linkError() {
        Toast.makeText(getActivity(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
