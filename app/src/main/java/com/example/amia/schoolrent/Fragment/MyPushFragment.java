package com.example.amia.schoolrent.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Activity.IdleInfoActivity;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.IdleInfoExtend;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.MyPushAdapter;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.slideswaphelper.PlusItemSlideCallback;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.slideswaphelper.WItemTouchHelperPlus;
import com.example.amia.schoolrent.Presenter.MyPushContract;
import com.example.amia.schoolrent.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import static com.example.amia.schoolrent.Task.IdleTask.CLOSE_IDLE_SUCCESS;
import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.MY_IDLE_ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.MY_IDLE_SUCCESS;

public class MyPushFragment extends Fragment implements MyPushContract.View {
    private View view;
    private MyPushContract.Presenter presenter;

    private Student student;

    private PullLoadMoreRecyclerView recyclerView;
    private MyPushAdapter adapter;
    private RelativeLayout progressView;

    private IdleInfoExtend idleInfo;

    public static MyPushFragment newInstance(){
        MyPushFragment myPushFragment=new MyPushFragment();
        return myPushFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_push_fragment,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    private void init(){
        student = ((StudentInterface)getActivity()).getStudent();

        progressView = view.findViewById(R.id.progress_view);
        recyclerView = view.findViewById(R.id.my_push_rv);

        recyclerView.setGridLayout(1);
        adapter = new MyPushAdapter(getActivity(), new MyPushAdapter.ResponseRentInterface() {
            @Override
            public void loadDetail(IdleInfo idleInfo) {
                MyPushFragment.this.loadDetail(idleInfo);
            }

            @Override
            public void closeIdle(IdleInfo idleInfo) {
                progressView.setVisibility(View.VISIBLE);
                presenter.closeIdle(idleInfo,handler);
            }
        });

        RecyclerView rv = recyclerView.getRecyclerView();

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        //允许侧滑
        PlusItemSlideCallback callback = new PlusItemSlideCallback(WItemTouchHelperPlus.SLIDE_ITEM_TYPE_ITEMVIEW);
        WItemTouchHelperPlus extension = new WItemTouchHelperPlus(callback);
        extension.attachToRecyclerView(recyclerView.getRecyclerView());

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

    protected void refresh(){
        idleInfo = new IdleInfoExtend();
        idleInfo.setPage(1);
        presenter.loadMyPush(idleInfo,handler);
    }

    protected void loadMore(){
        presenter.loadMyPush(idleInfo,handler);
    }

    protected void loadDetail(IdleInfo idleInfo){
        Intent intent = new Intent(getActivity(), IdleInfoActivity.class);
        intent.putExtra("idleInfo",idleInfo);
        intent.putExtra("student",student);
        startActivity(intent);
    }

    private void setList(Object o){
        recyclerView.setPullLoadMoreCompleted();
        try{
            List<IdleInfo> idleInfos = (List<IdleInfo>) o;
            //如果当前页为第一页，则直接清除原来的数据
            //否则则添加
            if (idleInfo.getPage() == 1) {
                adapter.setList(idleInfos);
            } else {
                adapter.addIdle(idleInfos);
            }
            if(idleInfos!=null &&idleInfos.size()>0) {
                idleInfo.setPage(idleInfo.getPage() + 1);
            }
        } catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    @Override
    public void linkError() {
        Toast.makeText(getActivity(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(MyPushContract.Presenter presenter) {
        this.presenter = presenter;
    }

    protected void showMessage(Object o){
        if(o == null){
            linkError();
            return;
        }

        try{
            Toast.makeText(getContext(),(String) o,Toast.LENGTH_SHORT).show();
        } catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    protected void closeSuccess(){
        Toast.makeText(getActivity(),R.string.close_success, Toast.LENGTH_SHORT).show();
        refresh();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressView.setVisibility(View.GONE);
            switch (msg.what){
                case MY_IDLE_SUCCESS:
                    setList(msg.obj);
                    break;
                case CLOSE_IDLE_SUCCESS:
                    closeSuccess();
                    break;
                case MY_IDLE_ERROR:
                case ERROR:
                    default:
                    showMessage(msg.obj);
                    break;
            }
        }
    };
}
