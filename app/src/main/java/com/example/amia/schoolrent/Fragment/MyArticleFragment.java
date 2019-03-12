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
import com.example.amia.schoolrent.Activity.ArticleInfoActivity;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.RentNeedsExtend;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.MyArticleAdapter;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.MyPushAdapter;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.slideswaphelper.PlusItemSlideCallback;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.slideswaphelper.WItemTouchHelperPlus;
import com.example.amia.schoolrent.Presenter.MyArticleContract;
import com.example.amia.schoolrent.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import static com.example.amia.schoolrent.Task.RentNeedsTask.ARTICLE_LIST;
import static com.example.amia.schoolrent.Task.RentNeedsTask.DEL_ARTICLE;

public class MyArticleFragment extends Fragment implements MyArticleContract.View {

    private MyArticleContract.Presenter presenter;

    private View view;

    private PullLoadMoreRecyclerView recyclerView;
    private MyArticleAdapter adapter;

    private Student student;
    private RentNeedsExtend rentNeedsExtend;

    private RelativeLayout progressView;

    public static MyArticleFragment newInstance(){
        MyArticleFragment myArticleFragment=new MyArticleFragment();
        return myArticleFragment;
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

        recyclerView = view.findViewById(R.id.my_push_rv);
        progressView = view.findViewById(R.id.progress_view);

        recyclerView.setGridLayout(1);
        adapter = new MyArticleAdapter(getActivity(), new MyArticleAdapter.ResponseRentInterface() {
            @Override
            public void del(RentNeeds rent) {
                progressView.setVisibility(View.VISIBLE);
                presenter.del(rent,handler);
            }

            @Override
            public void toInfo(RentNeeds rentNeeds) {
                toNeedInfoActivity(rentNeeds);
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

    protected void toNeedInfoActivity(RentNeeds rentNeeds){
        Intent intent = new Intent(getActivity(), ArticleInfoActivity.class);
        intent.putExtra("student",student);
        intent.putExtra("rentNeeds",rentNeeds);
        startActivity(intent);
    }

    protected void refresh(){
        rentNeedsExtend = new RentNeedsExtend();
        rentNeedsExtend.setPage(1);
        presenter.queryMyArticle(rentNeedsExtend,handler);
    }

    protected void loadMore(){
        presenter.queryMyArticle(rentNeedsExtend,handler);
    }

    protected void loadArticleSuccess(Object o){
        recyclerView.setPullLoadMoreCompleted();
        try{
            List<RentNeeds> rentNeedsList = (List<RentNeeds>) o;
            adapter.setList(rentNeedsList);
        } catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    protected void showMessage(Object o){
        recyclerView.setPullLoadMoreCompleted();
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

    protected void delSuccess(){
        refresh();
        progressView.setVisibility(View.GONE);
    }

    @Override
    public void linkError() {
        Toast.makeText(getActivity(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(MyArticleContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ARTICLE_LIST:
                    loadArticleSuccess(msg.obj);
                    break;
                case DEL_ARTICLE:
                    delSuccess();
                    break;
                default:
                    showMessage(msg.obj);
                    break;
            }
        }
    };
}
