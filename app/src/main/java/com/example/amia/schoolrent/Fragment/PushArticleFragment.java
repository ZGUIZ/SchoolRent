package com.example.amia.schoolrent.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.PushIdleInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.PushArticleContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;

import static com.example.amia.schoolrent.Task.IdleTask.ERRORWITHMESSAGE;
import static com.example.amia.schoolrent.Task.IdleTask.PUSH_ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.PUSH_SUCCESS;
import static com.example.amia.schoolrent.Task.RentNeedsTask.ADD_ARTICLE;

public class PushArticleFragment extends Fragment implements PushArticleContract.View {

    private View view;

    private PushArticleContract.Presenter presenter;

    private TextView lastTime;
    private RelativeLayout errorLayout;
    private RelativeLayout progressLayout;

    protected RentNeeds rentNeeds;
    protected Student student;

    protected static final int CLOSE_ACTIVITY = 400;
    protected static final int UPDATE_TIME = 401;
    protected static final int CLOSE_DIALOG = 402;

    public static PushArticleFragment newInstance(){
        PushArticleFragment pushIdleFragment = new PushArticleFragment();
        return pushIdleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_push_article_layout,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    protected void init(){
        student = ((StudentInterface)getActivity()).getStudent();
        rentNeeds = new RentNeeds();
        rentNeeds.setUserId(student.getUserId());
        rentNeeds.setSchoolId(student.getSchoolId());

        view.findViewById(R.id.push_button).setOnClickListener(onClickListener);

        progressLayout = view.findViewById(R.id.progress_view);

        lastTime = view.findViewById(R.id.last_time);
    }

    protected void pushIdle(){

        String title = ((EditText)view.findViewById(R.id.idle_title_et)).getText().toString();
        if(title == null || "".equals(title)){
            pushError(ActivityUtil.getString(getActivity(),R.string.title_null));
            return;
        }
        rentNeeds.setTitle(title);

        String info = ((EditText)view.findViewById(R.id.idle_info_et)).getText().toString();
        if(title == null || "".equals(title)){
            pushError(ActivityUtil.getString(getActivity(),R.string.info_null));
            return;
        }
        rentNeeds.setIdelInfo(info);

        progressLayout.setVisibility(View.VISIBLE);

        presenter.pushArticle(rentNeeds,handler);
    }

    @Override
    public void leavePage(){
        //对话框，是否退出

        getActivity().finish();
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

    public void linkError() {
        Toast.makeText(getContext(),R.string.link_error,Toast.LENGTH_SHORT).show();
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
                case ADD_ARTICLE:
                    pushSuccess();
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
                default:
                    pushError();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void setPresenter(PushArticleContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
