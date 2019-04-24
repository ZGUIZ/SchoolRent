package com.example.amia.schoolrent.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.PushIdleInterface;
import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.LocalPic;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.PushImageAdapter;
import com.example.amia.schoolrent.Presenter.PushIdleContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.COSUtil;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.ProgressView;

import java.util.List;

import static com.example.amia.schoolrent.Task.IdleTask.CLASSIFY_ALL;
import static com.example.amia.schoolrent.Task.IdleTask.ERRORWITHMESSAGE;
import static com.example.amia.schoolrent.Task.IdleTask.PUSH_ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.PUSH_SUCCESS;

public class PushIdleFragment extends Fragment implements PushIdleContract.View {

    private View view;
    private PushImageAdapter adapter;

    private PushIdleContract.Presenter presenter;

    private BottomSheetDialog dialog;
    private View selectView;
    private ProgressView progressView;
    private TextView lastTime;
    private RelativeLayout errorLayout;
    private RelativeLayout progressLayout;

    private int imageCount = 0;
    protected IdleInfo idleInfo;
    protected Student student;

    protected static final int SET_CLASSIFY_TITLE = 1001;
    protected static final int CLOSE_ACTIVITY = 400;
    protected static final int UPDATE_TIME = 401;
    protected static final int CLOSE_DIALOG = 402;

    public static PushIdleFragment newInstance(){
        PushIdleFragment pushIdleFragment = new PushIdleFragment();
        return pushIdleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_push_idle_layout,container,false);
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    protected void init(){
        student = ((PushIdleInterface)getActivity()).getStudent();
        idleInfo = new IdleInfo();
        idleInfo.setUserId(student.getUserId());
        idleInfo.setSchoolId(student.getSchoolId());

        //view.findViewById(R.id.back_button).setOnClickListener(onClickListener);

        //初始化添加图片的RecyclerView
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

        view.findViewById(R.id.classify_rl).setOnClickListener(onClickListener);

        view.findViewById(R.id.push_button).setOnClickListener(onClickListener);

        progressLayout = view.findViewById(R.id.progress_view);

        lastTime = view.findViewById(R.id.last_time);
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
            pushError(ActivityUtil.getString(getActivity(),R.string.classify_null));
            return;
        }
        String title = ((EditText)view.findViewById(R.id.idle_title_et)).getText().toString();
        if(title == null || "".equals(title)){
            pushError(ActivityUtil.getString(getActivity(),R.string.title_null));
            return;
        }
        idleInfo.setTitle(title);

        String info = ((EditText)view.findViewById(R.id.idle_info_et)).getText().toString();
        if(title == null || "".equals(title)){
            pushError(ActivityUtil.getString(getActivity(),R.string.info_null));
            return;
        }
        idleInfo.setIdelInfo(info);

        float baseCash = Float.valueOf(((EditText)view.findViewById(R.id.deposit_ev)).getText().toString());
        idleInfo.setDeposit(baseCash);
        float retal = Float.valueOf(((EditText)view.findViewById(R.id.retal_et)).getText().toString());
        idleInfo.setRetal(retal);

        if(retal>baseCash){
            pushError(ActivityUtil.getString(getActivity(),R.string.money_error));
            return;
        }

        List<IdelPic> pics = adapter.getPicList();
        if (pics.size()<=0){
            pushError(ActivityUtil.getString(getActivity(),R.string.pic_null));
            return;
        }
        idleInfo.setPicList(pics);

        idleInfo.setAddress(((EditText)view.findViewById(R.id.address_et)).getText().toString());


        progressLayout.setVisibility(View.VISIBLE);

        presenter.pushIdle(getActivity(),idleInfo,handler);
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
                /*case R.id.back_button:
                    leavePage();
                    break;*/
                case R.id.classify_rl:
                    showClassifySelector();
                    break;
                case R.id.push_button:
                    pushIdle();
                    break;
            }
        }
    };

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
        presenter.getAllClassify(getActivity(),handler);
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

    public void linkError() {
        Toast.makeText(getContext(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addPic(String path) {
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

    private void setClassifyTextView(Classify classify){
        try {
            TextView textView = view.findViewById(R.id.classify_name_tv);
            textView.setText(classify.getClassifyName());
        }catch (Exception e){
            e.printStackTrace();
        }
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
            switch (msg.what){
                case CLASSIFY_ALL:
                    setClassify((List<Classify>) msg.obj);
                    break;
                case SET_CLASSIFY_TITLE:
                    setClassifyTextView((Classify) msg.obj);
                    break;
                case PUSH_SUCCESS:
                    pushSuccess();
                    break;
                case PUSH_ERROR:
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
            super.handleMessage(msg);
        }
    };
}
