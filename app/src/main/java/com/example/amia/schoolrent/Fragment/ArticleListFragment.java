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
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ArticleInfoActivity;
import com.example.amia.schoolrent.Activity.BaseAcitivity;
import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.RentNeedsExtend;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.ArticleAdapter;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.OnItemClickListener;
import com.example.amia.schoolrent.Presenter.ArticleContract;
import com.example.amia.schoolrent.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import static com.example.amia.schoolrent.Task.RentNeedsTask.ARTICLE_LIST;

public class ArticleListFragment extends Fragment implements ArticleContract.View {

    private View view;
    private ArticleContract.Presenter presenter;

    private PullLoadMoreRecyclerView recyclerView;
    private ArticleAdapter adapter;

    private RentNeedsExtend rentNeedsExtend;

    private Student student;

    public static ArticleListFragment newInstance(){
        ArticleListFragment articleListFragment = new ArticleListFragment();
        return articleListFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_article_list,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        BaseAcitivity baseAcitivity = (BaseAcitivity) getActivity();
        student = baseAcitivity.getStudent();

        recyclerView = view.findViewById(R.id.article_list);
        adapter = new ArticleAdapter(getContext());
        recyclerView.setGridLayout(1);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemCLick(View view, int position) {
                RentNeeds rentNeeds = adapter.getIdleInfoByPosition(position);
                loadRentInfoActivity(rentNeeds);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
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
        rentNeedsExtend = new RentNeedsExtend();
        rentNeedsExtend.setPage(1);
        presenter.queryArticleList(rentNeedsExtend,handler);
    }

    private void loadMore(){
        presenter.queryArticleList(rentNeedsExtend,handler);
    }

    protected void loadListSuccess(Object o){
        recyclerView.setPullLoadMoreCompleted();
        try{
            List<RentNeeds> rentNeedsList = (List<RentNeeds>) o;
            if(rentNeedsExtend.getPage() == 1) {
                adapter.setNeedsInfos(rentNeedsList);
            } else {
                if(rentNeedsList ==null || rentNeedsList.size()==0){
                    Toast.makeText(getActivity(), R.string.no_more, Toast.LENGTH_SHORT).show();
                }
                adapter.addIdleInfos(rentNeedsList);
            }
            rentNeedsExtend.setPage(rentNeedsExtend.getPage()+1);
        } catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    private void errorWithMessge(Object o){
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

    private void loadRentInfoActivity(RentNeeds rentNeeds){
        Intent intent = new Intent(getActivity(), ArticleInfoActivity.class);
        intent.putExtra("student",student);
        intent.putExtra("rentNeeds",rentNeeds);
        startActivity(intent);
    }

    private void linkError(){
        Toast.makeText(getContext(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ARTICLE_LIST:
                    loadListSuccess(msg.obj);
                    break;
                default:
                    errorWithMessge(msg.obj);
                    break;
            }
        }
    };

    @Override
    public void setPresenter(ArticleContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
