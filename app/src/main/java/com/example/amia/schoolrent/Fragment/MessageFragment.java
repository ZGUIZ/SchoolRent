package com.example.amia.schoolrent.Fragment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.amia.schoolrent.Fragment.RecyclerAdapter.MyMessageAdapter;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.slideswaphelper.PlusItemSlideCallback;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.slideswaphelper.WItemTouchHelperPlus;
import com.example.amia.schoolrent.Presenter.MyMessageContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.View.MessageDialog;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import static com.example.amia.schoolrent.Task.MessageTask.DEL_SUCCESS;
import static com.example.amia.schoolrent.Task.MessageTask.ERROR;
import static com.example.amia.schoolrent.Task.MessageTask.MESSAGE;

public class MessageFragment extends Fragment implements MyMessageContract.View {

    private View view;
    private MyMessageContract.Presenter presenter;

    private PullLoadMoreRecyclerView recyclerView;
    private MyMessageAdapter adapter;

    private TextView nullMessageText;

    public static MessageFragment newInstance(){
        MessageFragment messageFragment = new MessageFragment();
        return messageFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message_list,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView = view.findViewById(R.id.message_list);

        nullMessageText = view.findViewById(R.id.null_message);

        adapter = new MyMessageAdapter(getContext());

        adapter.setOnClick(new MyMessageAdapter.OnClickListener() {
            @Override
            public void onCLick(com.example.amia.schoolrent.Bean.Message msg) {
                showDialog(msg);
            }

            @Override
            public void onDel(com.example.amia.schoolrent.Bean.Message msg) {
                delMessage(msg);
            }
        });

        recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        //只有未租赁的商品允许侧滑
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
            }
        });
        recyclerView.refresh();
        recyclerView.setIsRefresh(true);
    }

    private void showDialog(com.example.amia.schoolrent.Bean.Message message){
        MessageDialog.Builder builder = new MessageDialog.Builder(getContext(),message);
        MessageDialog messageDialog = builder.createDialog();
        messageDialog.show();
    }

    private void delMessage(com.example.amia.schoolrent.Bean.Message message){
        presenter.delMessage(message,handler);
    }

    private void refresh(){
        presenter.getMyMessage(handler);
    }

    protected void loadMessageSuccess(Object o){
        recyclerView.setPullLoadMoreCompleted();
        try{
            List<com.example.amia.schoolrent.Bean.Message> messages = (List<com.example.amia.schoolrent.Bean.Message>) o;
            if(messages == null || messages.size()<=0){
                nullMessageText.setVisibility(View.VISIBLE);
            } else {
                nullMessageText.setVisibility(View.GONE);
            }
            adapter.setList(messages);
        } catch (Exception e){
            e.printStackTrace();
            linkError();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
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
    public void linkError() {
        Toast.makeText(getActivity(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(MyMessageContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE:
                case DEL_SUCCESS:
                    loadMessageSuccess(msg.obj);
                    break;
                default:
                    showMessage(msg.obj);
                    break;
            }
        }
    };
}
