package com.example.amia.schoolrent.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Activity.IdleInfoActivity;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.RentExtend;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.FinishedAdapter;
import com.example.amia.schoolrent.Presenter.MineRentContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import static com.example.amia.schoolrent.Task.IdleTask.CANCEL_RENT;
import static com.example.amia.schoolrent.Task.IdleTask.DEL_RENT;
import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.FIND_BY_ID;
import static com.example.amia.schoolrent.Task.IdleTask.LOAD_AGREE;
import static com.example.amia.schoolrent.Task.IdleTask.LOAD_MINE_REQUEST;
import static com.example.amia.schoolrent.Task.IdleTask.START_RENT;

public class DissagreeFragment extends Fragment implements MineRentContract.View {

    private View view;

    private MineRentContract.Presenter presenter;

    private RelativeLayout progressView;
    private PullLoadMoreRecyclerView recyclerView;
    private FinishedAdapter adapter;

    private RentExtend rentExtend;

    private boolean isRefresh = false;

    public static DissagreeFragment newInstance(){
        DissagreeFragment agreeFragement=new DissagreeFragment();
        return agreeFragement;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_renting,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init(){
        progressView = view.findViewById(R.id.progress_view);

        recyclerView = view.findViewById(R.id.mine_send_rv);

        adapter = new FinishedAdapter(getContext(), new FinishedAdapter.ResponseRentInterface() {
            @Override
            public void delete(Rent rent) {
                progressView.setVisibility(View.VISIBLE);
                presenter.delRent(rent,handler);
                //presenter.cancelRent(rent,handler);
            }

            @Override
            public void toIdle(IdleInfo idleInfo) {
                loadIdleInfo(idleInfo);
            }

            @Override
            public void eval(Rent rent) {
            }
        });
        adapter.canEval(false);

        recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        recyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                DissagreeFragment.this.refresh();
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
        rentExtend = new RentExtend();
        rentExtend.setPage(1);
        rentExtend.setStatus(2);

        isRefresh = true;

        presenter.getMyRent(rentExtend,handler,LOAD_AGREE);
    }

    protected void loadSuccess(Object o){
        recyclerView.setPullLoadMoreCompleted();
        progressView.setVisibility(View.GONE);
        TextView nullInfo = view.findViewById(R.id.list_null_tv);
        try{
            List<Rent> rentList = (List<Rent>) o;
            if(isRefresh){
                isRefresh = false;
                adapter.setList(rentList);
                if(rentList.size() == 0){
                    nullInfo.setVisibility(View.VISIBLE);
                }
            }
            if(rentList == null || rentList.size() ==0 ) {
                showMessage(ActivityUtil.getString(getContext(),R.string.no_more));
                return;
            } else {
                nullInfo.setVisibility(View.GONE);
            }
            if(rentExtend.getPage() == 1){
                adapter.setList(rentList);
            } else {
                adapter.addRent(rentList);
            }
            rentExtend.setPage(rentExtend.getPage()+1);
        } catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    private void loadMore(){
        presenter.getMyRent(rentExtend,handler,LOAD_MINE_REQUEST);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    private void loadIdleInfo(IdleInfo idleInfo){
        presenter.findById(idleInfo.getInfoId(),handler);
    }

    private void toIdleInfoActivity(IdleInfo info){
        StudentInterface studentInterface = (StudentInterface) getActivity();
        Intent intent = new Intent(getActivity(), IdleInfoActivity.class);
        intent.putExtra("idleInfo",info);
        intent.putExtra("student",studentInterface.getStudent());
        startActivity(intent);
    }

    private void changeSuccess(){
        progressView.setVisibility(View.GONE);
        refresh();
        recyclerView.setIsRefresh(true);
    }

    @Override
    public void linkError() {
        Toast.makeText(getContext(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(MineRentContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            recyclerView.setPullLoadMoreCompleted();
            super.handleMessage(msg);
            switch (msg.what){
                case LOAD_MINE_REQUEST:
                case LOAD_AGREE:
                    loadSuccess(msg.obj);
                    break;
                case FIND_BY_ID:
                    try {
                        toIdleInfoActivity((IdleInfo) msg.obj);
                    } catch (ClassCastException e){
                        e.printStackTrace();
                        linkError();
                    }
                    break;
                case DEL_RENT:
                case CANCEL_RENT:
                case START_RENT:
                    changeSuccess();
                    break;
                case ERROR:
                default:
                    showMessage(msg.obj);
                    break;
            }
        }
    };
}
