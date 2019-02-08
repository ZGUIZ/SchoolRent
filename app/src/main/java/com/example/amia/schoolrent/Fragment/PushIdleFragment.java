package com.example.amia.schoolrent.Fragment;

import android.app.Activity;
import android.content.Context;
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

import com.example.amia.schoolrent.Activity.ActivityInterface.PushIdleInterface;
import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.LocalPic;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.PushImageAdapter;
import com.example.amia.schoolrent.Presenter.PushIdleContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.COSUtil;

public class PushIdleFragment extends Fragment implements PushIdleContract.View {

    private View view;
    private PushImageAdapter adapter;

    private PushIdleContract.Presenter presenter;

    int imageCount = 0;

    public static PushIdleFragment newInstance(){
        PushIdleFragment pushIdleFragment = new PushIdleFragment();
        return pushIdleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_push_idle_layout,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    protected void init(){
        view.findViewById(R.id.back_button).setOnClickListener(onClickListener);

        //初始化添加图片的RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.idle_image_rv);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        Activity activity = getActivity();
        adapter = new PushImageAdapter(activity);
        ((PushImageAdapter) adapter).setOnItemCLickListener(new PushImageAdapter.OnItemClickListener() {
            @Override
            public void addPic() {
                selectPic();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * 选择图片
     */
    protected void selectPic(){
        PushIdleInterface pushIdleInterface = (PushIdleInterface) getActivity();
        pushIdleInterface.choosePic();
    }

    @Override
    public void leavePage(){
        //对话框，是否退出

        getActivity().finish();
    }

    @Override
    public void setPresenter(PushIdleContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.back_button:
                    leavePage();
                    break;
            }
        }
    };

    @Override
    public void addPic(String path) {
        Student student = ((PushIdleInterface)getActivity()).getStudent();
        LocalPic localPic = new LocalPic();
        localPic.setAdd(false);
        localPic.setLocalUri(path);

        imageCount++;

        //上传图片
        COSUtil cosUtil = new COSUtil(getActivity());
        String fileName = cosUtil.uploadFile(student,path,handler,String.valueOf(imageCount));
        IdelPic idelPic = new IdelPic();
        idelPic.setPicUrl(ActivityUtil.getString(getActivity(),R.string.image_host)+fileName);
        localPic.setPic(idelPic);

        adapter.addPic(localPic);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
}
