package com.example.amia.schoolrent.Fragment;

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

import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.RentExtend;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.MyRequestAdapter;
import com.example.amia.schoolrent.Presenter.MineRentContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import static com.example.amia.schoolrent.Task.IdleTask.CANCEL_RENT;
import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.LOAD_MINE_REQUEST;

public class MySendRequestFragment extends Fragment implements MineRentContract.View {
    private View view;

    private MineRentContract.Presenter presenter;

    private RelativeLayout progressView;
    private PullLoadMoreRecyclerView recyclerView;
    private MyRequestAdapter adapter;

    private RentExtend rentExtend;

    private boolean isRefresh = false;

    public static MySendRequestFragment newInstance(){
        MySendRequestFragment sendRequestFragment=new MySendRequestFragment();
        return sendRequestFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mine_send_request_layout,container,false);
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

        adapter = new MyRequestAdapter(getContext(), new MyRequestAdapter.ResponseRentInterface() {
            @Override
            public void cancel(Rent rent) {
                progressView.setVisibility(View.VISIBLE);
                presenter.cancelRent(rent,handler);
            }
        });
        recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity()));
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
        rentExtend = new RentExtend();
        rentExtend.setPage(1);
        rentExtend.setStatus(0);

        isRefresh = true;

        presenter.getMyRent(rentExtend,handler,LOAD_MINE_REQUEST);
    }

    protected void loadSuccess(Object o){
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
            super.handleMessage(msg);
            switch (msg.what){
                case LOAD_MINE_REQUEST:
                    loadSuccess(msg.obj);
                    break;
                case CANCEL_RENT:
                    refresh();
                    break;
                case ERROR:
                default:
                    showMessage(msg.obj);
                    break;
            }
        }
    };
}
