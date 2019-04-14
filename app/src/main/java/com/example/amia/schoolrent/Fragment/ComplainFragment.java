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

import com.example.amia.schoolrent.Activity.ActivityInterface.ComplainInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.IdleInfoInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.RentNeedsInterface;
import com.example.amia.schoolrent.Bean.Complain;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Presenter.OtherComplainContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;

import static com.example.amia.schoolrent.Fragment.PushArticleFragment.UPDATE_TIME;
import static com.example.amia.schoolrent.Fragment.PushIdleFragment.CLOSE_ACTIVITY;
import static com.example.amia.schoolrent.Fragment.PushIdleFragment.CLOSE_DIALOG;
import static com.example.amia.schoolrent.Task.ComplainTask.PUSH_SUCCESS;
import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Task.SchoolTask.ERRORWITHMESSAGE;

public class ComplainFragment extends Fragment implements OtherComplainContract.View {
    private View view;
    private OtherComplainContract.Presenter presenter;

    private TextView lastTime;
    private RelativeLayout errorLayout;
    private RelativeLayout progressLayout;
    private EditText contentEdit;

    protected IdleInfo idleInfo;
    protected RentNeeds rentNeeds;
    protected Complain complain;


    public static ComplainFragment newInstance(){
        ComplainFragment complainFragment=new ComplainFragment();
        return complainFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_complain_layout,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init(){
        complain = ((ComplainInterface)getActivity()).getComplain();

        TextView title = view.findViewById(R.id.idle_title);

        switch (complain.getComplainType()){
            case 1:
                idleInfo = ((IdleInfoInterface) getActivity()).getIdleInfo();
                complain.setInfoId(idleInfo.getInfoId());
                title.setText(idleInfo.getTitle());
                break;
            case 2:
                rentNeeds = ((RentNeedsInterface)getActivity()).getRentNeeds();
                complain.setInfoId(rentNeeds.getInfoId());
                title.setText(rentNeeds.getTitle());
                break;
        }

        view.findViewById(R.id.push_button).setOnClickListener(onClickListener);

        contentEdit = view.findViewById(R.id.idle_info_et);

        progressLayout = view.findViewById(R.id.progress_view);

        lastTime = view.findViewById(R.id.last_time);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    @Override
    public void linkError() {
        Toast.makeText(getActivity(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(OtherComplainContract.Presenter presenter) {
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


        complain.setMsg(content);

        progressLayout.setVisibility(View.VISIBLE);

        presenter.addComplain(complain,handler);
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
                case PUSH_SUCCESS:
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
