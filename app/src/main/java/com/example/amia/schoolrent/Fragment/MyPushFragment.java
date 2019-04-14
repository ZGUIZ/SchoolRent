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

import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Activity.ComplainActivity;
import com.example.amia.schoolrent.Activity.IdleInfoActivity;
import com.example.amia.schoolrent.Activity.UpdateIdleActivity;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.IdleInfoExtend;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.MyPushAdapter;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.slideswaphelper.PlusItemSlideCallback;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.slideswaphelper.WItemTouchHelperPlus;
import com.example.amia.schoolrent.Presenter.MyPushContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.View.DestoryDialog;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.sql.BatchUpdateException;
import java.util.List;

import static com.example.amia.schoolrent.Task.IdleTask.CANCLE_SUCCESS;
import static com.example.amia.schoolrent.Task.IdleTask.CLOSE_IDLE_SUCCESS;
import static com.example.amia.schoolrent.Task.IdleTask.DEL_SUCCESS;
import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.MY_IDLE_ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.MY_IDLE_SUCCESS;
import static com.example.amia.schoolrent.Task.IdleTask.START_RENT;

public class MyPushFragment extends Fragment implements MyPushContract.View {
    private View view;
    private MyPushContract.Presenter presenter;

    private Student student;

    private PullLoadMoreRecyclerView recyclerView;
    private MyPushAdapter adapter;
    private RelativeLayout progressView;

    private IdleInfoExtend idleInfo;

    //损毁详情的Dialog
    private DestoryDialog destoryDialog;

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

            @Override
            public void cancleOrFinish(IdleInfo idleInfo) {
                progressView.setVisibility(View.VISIBLE);
                presenter.cancelRent(idleInfo,handler);
            }

            @Override
            public void delIdle(IdleInfo idleInfo) {
                progressView.setVisibility(View.VISIBLE);
                presenter.delIdle(idleInfo,handler);
            }

            @Override
            public void updateIdle(IdleInfo idleInfo) {
                loadUpdateActivity(idleInfo);
            }

            @Override
            public void startRent(IdleInfo idleInfo) {
                showStartRentDialog(idleInfo);
            }

            @Override
            public void complain(IdleInfo idleInfo) {
                toComplainActivity(idleInfo);
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

    protected void toComplainActivity(IdleInfo idleInfo){
        Intent intent = new Intent(getActivity(), ComplainActivity.class);
        intent.putExtra("idleInfo",idleInfo);
        startActivity(intent);
    }

    protected void showStartRentDialog(final IdleInfo idleInfo){
        DestoryDialog.Builder builder = new DestoryDialog.Builder(getActivity());

        builder.setPositiveButton(ActivityUtil.getString(getActivity(),R.string.agree),new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressView.setVisibility(View.VISIBLE);
                presenter.startRent(idleInfo,handler);
            }
        });
        builder.setNegativeButton(ActivityUtil.getString(getActivity(), R.string.refuse_rent), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressView.setVisibility(View.VISIBLE);
                presenter.cancelRent(idleInfo,handler);
            }
        });
        builder.setEditAble(false).setTitle(R.string.destroy_detail).setContent(idleInfo.getDestoryInfo());
        destoryDialog = builder.createDialog();
        destoryDialog.show();
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

    protected void loadUpdateActivity(IdleInfo idleInfo){
        Intent intent = new Intent(getActivity(), UpdateIdleActivity.class);
        intent.putExtra("student",student);
        intent.putExtra("idleInfo",idleInfo);
        startActivity(intent);
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

    protected void behaviorSuccess(){
        if(destoryDialog!=null&&destoryDialog.isShowing()){
            destoryDialog.hide();
        }
        Toast.makeText(getActivity(),R.string.behavior_success,Toast.LENGTH_SHORT).show();
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
                case START_RENT:
                case DEL_SUCCESS:
                case CANCLE_SUCCESS:
                    behaviorSuccess();
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
