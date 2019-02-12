package com.example.amia.schoolrent.Fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Activity.ActivityInterface.IdleInfoInterface;
import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.ResponseInfo;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.IdleInfoContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.KeyboardUtil;
import com.rey.material.app.BottomSheetDialog;
import com.tencent.cos.xml.utils.StringUtils;

import java.util.List;

import static com.example.amia.schoolrent.Task.RefuseTask.LOAD_REFUSE_ERROR;
import static com.example.amia.schoolrent.Task.RefuseTask.LOAD_REFUSE_SUCCESS;
import static com.example.amia.schoolrent.Task.RefuseTask.PUSH_REFUSE_ERROR;
import static com.example.amia.schoolrent.Task.RefuseTask.PUSH_REFUSE_SUCCESS;

public class IdleInfoFragment extends Fragment implements IdleInfoContract.View {

    private View view;
    private BottomSheetDialog dialog;  //回复弹出窗
    private View refuseView;  //回复输入框的View
    private EditText editText;  //回复输入框
    private RecyclerView refuseRecyclerView; // 回复信息的Layout

    private IdleInfoContract.Presenter presenter;

    public static IdleInfoFragment newInstance(){
        IdleInfoFragment idleInfoFragment = new IdleInfoFragment();
        return idleInfoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_idle_info_layout,container,false);
        init();
        return view;
    }

    protected void init(){
        IdleInfoInterface idleInfoInterface = (IdleInfoInterface) getActivity();
        IdleInfo idleInfo = idleInfoInterface.getIdleInfo();
        Student student = idleInfo.getStudent();

        //加载发布者头像
        //view.findViewById(R.id.back_ib).setOnClickListener(onClickListener);
        ImageView userIcon = view.findViewById(R.id.user_icon_riv);
        Glide.with(getActivity()).load(student.getUserIcon()).into(userIcon);

        //加载发布者用户信息
        TextView userName = view.findViewById(R.id.user_name_tv);
        userName.setText(student.getUserName());
        float score = student.getCredit();
        TextView credit = view.findViewById(R.id.credit_tv);
        credit.setText(ActivityUtil.getString(getActivity(),R.string.credit)+score);
        if(score<65){
            credit.setTextColor(Color.rgb(255,0,0));
        } else if(score <= 85) {
            credit.setTextColor(Color.rgb(0,238,118));
        } else {
            credit.setTextColor(Color.rgb(141,238,238));
        }

        //设置租金信息
        TextView deposit = view.findViewById(R.id.deposit_value_tv);
        deposit.setText(String.valueOf(idleInfo.getDeposit()));
        TextView rental = view.findViewById(R.id.rental_value_tv);
        rental.setText(String.valueOf(idleInfo.getRetal()));

        TextView idleInfoDes = view.findViewById(R.id.idle_info_detail);
        idleInfoDes.setText(idleInfo.getIdelInfo());

        loadImages(idleInfo.getPicList());

        //设置留言监听事件
        view.findViewById(R.id.refuse_rl).setOnClickListener(onClickListener);

        refuseRecyclerView = view.findViewById(R.id.refuse_ll);
        refuseRecyclerView.setNestedScrollingEnabled(false);

        //加载回复信息
        loadRefuseMessage();
    }

    //动态生成ImageView
    private void loadImages(List<IdelPic> picList){
        LinearLayout linearLayout = view.findViewById(R.id.idle_info_ll);
        Context context = getContext();
        for(IdelPic pic : picList){
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(6,2,6,2);
            imageView.setLayoutParams(params);
            linearLayout.addView(imageView);
            Glide.with(context).load(pic.getPicUrl()).into(imageView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    /**
     * 加载回复信息
     */
    protected void loadRefuseMessage(){
        IdleInfoInterface infoInterface = (IdleInfoInterface) getActivity();
        presenter.getReufseList(getContext(),infoInterface.getIdleInfo(),handler);
    }

    /**
     * 弹出回复输入框
     */
    private void pushRefuseDialog(String... hint){
        if(dialog != null) {
            dialog.cancel();
        }
        Activity activity = getActivity();
        dialog = new BottomSheetDialog(activity);
        refuseView = getLayoutInflater().inflate(R.layout.refuse_layout,null);
        dialog.setContentView(refuseView);
        dialog.show();

        editText = refuseView.findViewById(R.id.refuse_et);
        KeyboardUtil keyboardUtil = KeyboardUtil.getInstance();
        keyboardUtil.openKeyBoard(getActivity(),editText);

        if(hint!=null && hint.length>0){
            editText.setHint(hint[0]);
        }

        refuseView.findViewById(R.id.key_board_down).setOnClickListener(onClickListener);

        IdleInfoInterface infoInterface = (IdleInfoInterface) getActivity();
        Student student = infoInterface.getStudent();
        ImageView imageView = refuseView.findViewById(R.id.user_icon_iv);
        Glide.with(activity).load(student.getUserIcon()).into(imageView);

        refuseView.findViewById(R.id.send_btn).setOnClickListener(onClickListener);
    }

    /**
     * 隐藏键盘
     * @param editText
     */
    protected void hideKeyBoard(EditText editText){
        KeyboardUtil keyboardUtil = KeyboardUtil.getInstance();
        keyboardUtil.hideKeyBoard(getActivity(),editText);
    }

    protected void sendRefuseMessage(){
        //校验
        Context context = getContext();
        EditText editText = refuseView.findViewById(R.id.refuse_et);
        String refuesMessage = editText.getText().toString();
        if(StringUtils.isEmpty(refuesMessage)){
            Toast.makeText(context,R.string.no_refuse_message_fill,Toast.LENGTH_SHORT).show();
            return;
        }



        //封装
        IdleInfoInterface infoInterface = (IdleInfoInterface) getActivity();
        IdleInfo idleInfo = infoInterface.getIdleInfo();
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setInfoId(idleInfo.getInfoId());
        responseInfo.setResponseInfo(refuesMessage);
        //发布
        presenter.addRefuse(context,responseInfo,handler);
    }

    protected void sendRefuseSuccess(){
        if(dialog!=null){
            dialog.cancel();
        }
        //重新加载回复信息
        loadRefuseMessage();
    }

    /**
     * 加载回复信息
     * @param o
     */
    protected void loadRefuse(Object o){
        try{
            if(o == null){
                view.findViewById(R.id.none_message_tv).setVisibility(View.VISIBLE);
                refuseRecyclerView.setVisibility(View.GONE);
                return;
            }

            view.findViewById(R.id.none_message_tv).setVisibility(View.GONE);
            refuseRecyclerView.setVisibility(View.VISIBLE);

        }catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.refuse_rl:
                    pushRefuseDialog();
                    break;
                case R.id.key_board_down:
                    hideKeyBoard(editText);
                    break;
                case R.id.send_btn:
                    sendRefuseMessage();
                    break;
            }
        }
    };

    @Override
    public void linkError() {
        Toast.makeText(getContext(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(IdleInfoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PUSH_REFUSE_SUCCESS:
                    sendRefuseSuccess();
                    break;
                case PUSH_REFUSE_ERROR:
                    linkError();
                    break;
                case LOAD_REFUSE_SUCCESS:
                    loadRefuse(msg.obj);
                    break;
                case LOAD_REFUSE_ERROR:
                    linkError();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
