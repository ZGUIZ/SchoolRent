package com.example.amia.schoolrent.Fragment;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.IdleInfoInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.PicInterface;
import com.example.amia.schoolrent.Activity.BaseAcitivity;
import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.LocalPic;
import com.example.amia.schoolrent.Bean.OrderComplian;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.PushImageAdapter;
import com.example.amia.schoolrent.Presenter.ComplainContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.COSUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.amia.schoolrent.Fragment.PushIdleFragment.CLOSE_ACTIVITY;
import static com.example.amia.schoolrent.Fragment.PushIdleFragment.CLOSE_DIALOG;
import static com.example.amia.schoolrent.Fragment.PushIdleFragment.UPDATE_TIME;
import static com.example.amia.schoolrent.Task.IdleTask.ADD_COMPLAIN;
import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.ERRORWITHMESSAGE;

public class PushComplainFragment extends Fragment implements ComplainContract.View {

    private View view;
    private ComplainContract.Presenter presenter;

    private TextView lastTime;
    private RelativeLayout errorLayout;
    private RelativeLayout progressLayout;
    private EditText contentEdit;
    private EditText moneyEdit;

    private int imageCount = 0;
    protected IdleInfo idleInfo;
    protected OrderComplian complian;

    private PushImageAdapter adapter;

    public static PushComplainFragment newInstance(){
        PushComplainFragment complainFragment=new PushComplainFragment();
        return complainFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_push_complain_layout,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init(){
        complian = new OrderComplian();
        complian.setUrls(new ArrayList<IdelPic>());

        idleInfo = ((IdleInfoInterface) getActivity()).getIdleInfo();
        complian.setInfoId(idleInfo.getInfoId());
        TextView title = view.findViewById(R.id.idle_title);
        title.setText(idleInfo.getTitle());

        moneyEdit = view.findViewById(R.id.money);

        RecyclerView recyclerView = view.findViewById(R.id.idle_image_rv);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        Activity activity = getActivity();

        adapter = new PushImageAdapter(activity);
        adapter.setOnItemCLickListener(new PushImageAdapter.OnItemClickListener() {
            @Override
            public void addPic() {
                selectPic();
            }
        });
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.push_button).setOnClickListener(onClickListener);

        contentEdit = view.findViewById(R.id.idle_info_et);

        progressLayout = view.findViewById(R.id.progress_view);

        lastTime = view.findViewById(R.id.last_time);
    }

    protected void selectPic(){
        PicInterface pushIdleInterface = (PicInterface) getActivity();
        pushIdleInterface.choosePic();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    @Override
    public void addPic(String path) {

        LocalPic localPic = new LocalPic();
        localPic.setAdd(false);
        localPic.setLocalUri(path);

        imageCount++;

        //上传图片
        COSUtil cosUtil = new COSUtil(getActivity());
        String fileName = cosUtil.uploadFile(BaseAcitivity.getStudent(),path,handler,String.valueOf(imageCount));
        IdelPic idelPic = new IdelPic();
        idelPic.setPicUrl(ActivityUtil.getString(getActivity(),R.string.image_host)+fileName);
        localPic.setPic(idelPic);
        adapter.addPic(localPic);
    }

    @Override
    public void linkError() {
        Toast.makeText(getActivity(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(ComplainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.push_button:
                    pushIdle();
                    break;
            }
        }
    };

    protected void pushIdle(){
        String content = contentEdit.getText().toString();
        if(content == null || "".equals(content.trim())){
            pushError(ActivityUtil.getString(getContext(),R.string.null_fill));
            return;
        }

        String m = moneyEdit.getText().toString();
        if(m == null || "".equals(m.trim())){
            pushError(ActivityUtil.getString(getContext(),R.string.null_fill));
            return;
        }
        Float mon = null;
        try{
            mon = Float.parseFloat(m);
        }catch (Exception e){
            e.printStackTrace();
            pushError(ActivityUtil.getString(getContext(),R.string.money_format_error));
            return;
        }

        complian.setContext(content);
        complian.setMoney(mon);

        List<IdelPic> picList = adapter.getPicList();
        complian.getUrls().addAll(picList);

        progressLayout.setVisibility(View.VISIBLE);

        presenter.addComplain(complian,handler);
    }

    protected void pushSuccess(){
        progressLayout.setVisibility(View.GONE);
        view.findViewById(R.id.success_ll).setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            int time = 3;
            @Override
            public void run() {
                while(time > 0 ){
                    Message msg = handler.obtainMessage();
                    msg.what = UPDATE_TIME;
                    msg.obj = time;
                    handler.sendMessage(msg);
                    try {
                        time--;
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message msg = handler.obtainMessage();
                msg.what = CLOSE_ACTIVITY;
                handler.sendMessage(msg);
            }
        }).start();
    }

    protected void pushError(String... msg){
        progressLayout.setVisibility(View.GONE);
        TextView editText = view.findViewById(R.id.error_message_tv);
        if(msg == null || msg.length <= 0){
            editText.setText(R.string.push_error);
        } else {
            editText.setText(msg[0]);
        }

        errorLayout = view.findViewById(R.id.error_ll);
        errorLayout.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.what = CLOSE_DIALOG;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ADD_COMPLAIN:
                    pushSuccess();
                    break;
                case ERROR:
                    pushError();
                    break;
                case ERRORWITHMESSAGE:
                    pushError((String[]) msg.obj);
                    break;
                case CLOSE_ACTIVITY:
                    getActivity().finish();
                    break;
                case UPDATE_TIME:
                    lastTime.setText("("+msg.obj+" s)");
                    break;
                case CLOSE_DIALOG:
                    errorLayout.setVisibility(View.GONE);
                    break;
            }
        }
    };
}
