package com.example.amia.schoolrent.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.IdleInfoInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.PushIdleInterface;
import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.UpdateImageAdapter;
import com.example.amia.schoolrent.Presenter.UpdateIdleContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.COSUtil;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.ProgressView;

import java.util.List;

import static com.example.amia.schoolrent.Fragment.PushIdleFragment.CLOSE_ACTIVITY;
import static com.example.amia.schoolrent.Fragment.PushIdleFragment.SET_CLASSIFY_TITLE;
import static com.example.amia.schoolrent.Fragment.PushIdleFragment.UPDATE_TIME;
import static com.example.amia.schoolrent.Task.IdleTask.CLASSIFY_ALL;
import static com.example.amia.schoolrent.Task.IdleTask.CLASS_NAME_SUCCESS;
import static com.example.amia.schoolrent.Task.IdleTask.UPDATE_IDLE_SUCCESS;
import static com.example.amia.schoolrent.Util.COSUtil.PUT_PROGRESS;
import static com.example.amia.schoolrent.Util.COSUtil.RESULT_SUCCESS;

public class UpdateIdleFragment extends Fragment implements UpdateIdleContract.View {
    private static View view;
    private UpdateIdleContract.Presenter presenter;

    private IdleInfo idleInfo;
    private Student student;

    //选择类别的View
    private BottomSheetDialog dialog;
    private View selectView;
    private ProgressView progressView;

    private EditText titleEt;
    private EditText idleInfoEt;
    private RecyclerView recyclerView;
    private TextView classify;
    private EditText deposit;
    private EditText rental;
    private EditText address;
    private RelativeLayout progressLayout;
    private TextView lastTime;

    private UpdateImageAdapter adapter;


    public static UpdateIdleFragment newInstance(){
        UpdateIdleFragment updateIdleFragment = new UpdateIdleFragment();
        return updateIdleFragment;
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

    private void init(){
        idleInfo = ((IdleInfoInterface)getActivity()).getIdleInfo();
        student = ((IdleInfoInterface)getActivity()).getStudent();

        view.findViewById(R.id.push_button).setOnClickListener(onClickListener);
        view.findViewById(R.id.classify_rl).setOnClickListener(onClickListener);

        titleEt = view.findViewById(R.id.idle_title_et);
        idleInfoEt = view.findViewById(R.id.idle_info_et);
        recyclerView = view.findViewById(R.id.idle_image_rv);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        classify = view.findViewById(R.id.classify_name_tv);
        deposit = view.findViewById(R.id.deposit_ev);
        rental = view.findViewById(R.id.retal_et);
        address = view.findViewById(R.id.address_et);
        progressLayout = view.findViewById(R.id.progress_view);

        lastTime = view.findViewById(R.id.last_time);

        fillInfo(idleInfo);
    }

    private void fillInfo(IdleInfo idleInfo){
        Classify classify = new Classify();
        classify.setClassifyId(idleInfo.getClassifyId());
        presenter.getClassifyName(classify,handler);

        titleEt.setText(idleInfo.getTitle());
        idleInfoEt.setText(idleInfo.getIdelInfo());
        deposit.setText(String.valueOf(idleInfo.getDeposit()));
        rental.setText(String.valueOf(idleInfo.getRetal()));
        address.setText(idleInfo.getAddress());

        //设置图片的RecyclerView
        adapter = new UpdateImageAdapter(getContext());
        adapter.setOnItemCLickListener(new UpdateImageAdapter.OnItemClickListener() {
            @Override
            public void addPic() {
                selectPic();
            }
        });
        adapter.setPicList(idleInfo.getPicList());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setClassifyName(Object o){
        try{
            Classify c = (Classify) o;
            classify.setText(c.getClassifyName());
        }catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    /**
     * 选择图片
     */
    protected void selectPic(){
        PushIdleInterface pushIdleInterface = (PushIdleInterface) getActivity();
        pushIdleInterface.choosePic();
    }

    protected void pushIdle(){
        if(idleInfo.getClassifyId() == null || "".equals(idleInfo.getClassifyId())){
            errorWithMessage(ActivityUtil.getString(getActivity(),R.string.classify_null));
            return;
        }
        String title = ((EditText)view.findViewById(R.id.idle_title_et)).getText().toString();
        if(title == null || "".equals(title)){
            errorWithMessage(ActivityUtil.getString(getActivity(),R.string.title_null));
            return;
        }
        idleInfo.setTitle(title);

        String info = ((EditText)view.findViewById(R.id.idle_info_et)).getText().toString();
        if(title == null || "".equals(title)){
            errorWithMessage(ActivityUtil.getString(getActivity(),R.string.info_null));
            return;
        }
        idleInfo.setIdelInfo(info);

        float baseCash = Float.valueOf(((EditText)view.findViewById(R.id.deposit_ev)).getText().toString());
        idleInfo.setDeposit(baseCash);
        float retal = Float.valueOf(((EditText)view.findViewById(R.id.retal_et)).getText().toString());
        idleInfo.setRetal(retal);

        List<IdelPic> pics = adapter.getPicList();
        if (pics.size()<=0){
            errorWithMessage(ActivityUtil.getString(getActivity(),R.string.pic_null));
            return;
        }
        idleInfo.setPicList(pics);

        idleInfo.setAddress(((EditText)view.findViewById(R.id.address_et)).getText().toString());

        progressLayout.setVisibility(View.VISIBLE);

        presenter.updateIdleInfo(idleInfo,handler);
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
    public void setPresenter(UpdateIdleContract.Presenter loginPresenter) {
        this.presenter = loginPresenter;
    }

    @Override
    public void addPic(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        IdelPic idelPic = new IdelPic();

        //上传图片
        COSUtil cosUtil = new COSUtil(getActivity());
        String fileName = cosUtil.uploadFile(student,path,handler,String.valueOf(adapter.getItemCount()));
        idelPic.setPicUrl(ActivityUtil.getString(getActivity(),R.string.image_host)+fileName);
        adapter.addPic(idelPic);
    }

    private void showClassifySelector(){
        if(dialog != null) {
            dialog.cancel();
        }
        dialog = new BottomSheetDialog(getActivity());
        selectView = getLayoutInflater().inflate(R.layout.school_selector_layout,null);
        ((TextView)selectView.findViewById(R.id.title_tv)).setText(R.string.select_classify);
        progressView = selectView.findViewById(R.id.login_progress_view);
        dialog.setContentView(selectView);
        dialog.show();
        progressView.setVisibility(View.VISIBLE);
        presenter.getAllClassify(handler);
    }

    private void setClassify(final List<Classify> classifyList) {
        progressView.setVisibility(View.GONE);
        if(classifyList== null || classifyList.size() == 0){
            linkError();
            return;
        }

        ListView listView = selectView.findViewById(R.id.school_list);

        ArrayAdapter<String> adapter;
        if(listView != null){
            //将其转换为数组
            String[] array = new String[classifyList.size()];
            for(int i = 0;i<classifyList.size();i++){
                array[i] = classifyList.get(i).getClassifyName();
            }
            adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,array);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //设置分类信息
                    Classify classify = classifyList.get(i);
                    idleInfo.setClassifyId(classify.getClassifyId());
                    Message msg = handler.obtainMessage();
                    msg.what = SET_CLASSIFY_TITLE;
                    msg.obj = classify;
                    handler.sendMessage(msg);
                    dialog.cancel();
                }
            });
        }

        //解决NestedScrollView和ListView滑动事件冲突
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                NestedScrollView scrollView = selectView.findViewById(R.id.school_nv);
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void setClassifyTextView(Classify classify){
        try {
            TextView textView = view.findViewById(R.id.classify_name_tv);
            textView.setText(classify.getClassifyName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void errorWithMessage(Object o){
        try{
            Toast.makeText(getContext(),(String)o,Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            linkError();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.push_button:
                    pushIdle();
                    break;
                case R.id.classify_rl:
                    showClassifySelector();
                    break;
            }
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CLASSIFY_ALL:
                    setClassify((List<Classify>) msg.obj);
                    break;
                case CLASS_NAME_SUCCESS:
                    setClassifyName(msg.obj);
                    break;
                case UPDATE_IDLE_SUCCESS:
                    pushSuccess();
                    break;
                case UPDATE_TIME:
                    lastTime.setText("("+msg.obj+" s)");
                    break;
                case CLOSE_ACTIVITY:
                    getActivity().finish();
                    break;
                case PUT_PROGRESS:
                    break;
                case SET_CLASSIFY_TITLE:
                    setClassifyTextView((Classify) msg.obj);
                    break;
                case RESULT_SUCCESS:
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    errorWithMessage(msg.obj);
            }
        }
    };
}
