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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Activity.ActivityInterface.IdleInfoInterface;
import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.ResponseInfo;
import com.example.amia.schoolrent.Bean.SecondResponseInfo;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.OnItemClickListener;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.RefuseAdapter;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.SecondRefuseListener;
import com.example.amia.schoolrent.Presenter.IdleInfoContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.KeyboardUtil;
import com.example.amia.schoolrent.View.InputPayPasswordDialog;
import com.rey.material.app.BottomSheetDialog;
import com.tencent.cos.xml.utils.StringUtils;

import java.util.List;

import static com.example.amia.schoolrent.Task.IdleTask.LOAD_RELATION_ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.LOAD_RELATION_SUCCESS;
import static com.example.amia.schoolrent.Task.IdleTask.RENT_ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.RENT_SUCCESS;
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

    private RelativeLayout rentBtn;
    private TextView rentBtnTextView;
    private InputPayPasswordDialog passwordDialog;
    private RelativeLayout progressView;

    private IdleInfo idleInfo;

    //商品和当前用户的关系
    //0.申请后待确认 -1.正在查询 1.确认 2.拒绝 3.同意后拒绝租赁 4.开始 5.完成 6取消 7无关（客户端回显需要） 8 当前用户发布
    private int relation;

    private IdleInfoContract.Presenter presenter;

    private SecondResponseInfo secondResponseInfo;

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

        this.idleInfo = idleInfo;

        progressView = view.findViewById(R.id.progress_view);
        progressView.setVisibility(View.VISIBLE);

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
        TextView address = view.findViewById(R.id.address_tv);
        address.setText(idleInfo.getAddress());

        TextView idleInfoDes = view.findViewById(R.id.idle_info_detail);
        idleInfoDes.setText(idleInfo.getIdelInfo());

        loadImages(idleInfo.getPicList());

        //设置留言监听事件
        view.findViewById(R.id.refuse_rl).setOnClickListener(onClickListener);

        refuseRecyclerView = view.findViewById(R.id.refuse_ll);
        refuseRecyclerView.setNestedScrollingEnabled(false);

        //加载回复信息
        loadRefuseMessage();

        rentBtn = view.findViewById(R.id.rent_btn);
        rentBtn.setOnClickListener(onClickListener);

        rentBtnTextView = view.findViewById(R.id.rent_btn_tv);

        Student s = idleInfoInterface.getStudent();
        setRentBtn(s,idleInfo);
    }

    private void setRentBtn(Student student,IdleInfo idleInfo){
        //完成或者已经被禁止显示
        if(idleInfo.getStatus() == 3 || idleInfo.getStatus() == 100 ){
            rentBtn.setBackgroundColor(Color.rgb(192,192,192));
            rentBtnTextView.setText(R.string.close);
            return;
        }

        if(student.getUserId().equals(idleInfo.getUserId())){
            relation = 8;
            setRentBtnText();
            return;
        }

        relation = -1;
        setRentBtnText();

        //查询当前用户和商品直接由什么关系
        presenter.getRelation(idleInfo,handler);
    }

    private void setRelation(Object o){
        try{
            Rent rent = (Rent) o;
            relation = rent.getStatus();
            setRentBtnText();
        } catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    private void setRentBtnText(){
        switch (relation){
            case -1:
                rentBtn.setBackgroundColor(Color.rgb(192,192,192));
                rentBtnTextView.setText(R.string.close);
                break;
            case 0:
                rentBtn.setBackgroundColor(Color.rgb(238,44,44));
                rentBtnTextView.setText(R.string.cancel);
                break;
            case 1:
                rentBtn.setBackgroundColor(Color.rgb(238,44,44));
                rentBtnTextView.setText(R.string.received);
                break;
            case 2:
            case 3:
                rentBtn.setBackgroundColor(Color.rgb(238,44,44));
                rentBtnTextView.setText(R.string.rent);
                break;
            case 4:
                rentBtn.setBackgroundColor(Color.rgb(238,44,44));
                rentBtnTextView.setText(R.string.end_rent);
                break;
            case 5:
                rentBtn.setBackgroundColor(Color.rgb(192,192,192));
                rentBtnTextView.setText(R.string.close);
                break;
            case 6:
            case 7:
                rentBtn.setBackgroundColor(Color.rgb(238,44,44));
                rentBtnTextView.setText(R.string.rent);
                break;
            case 8:
                rentBtn.setBackgroundColor(Color.rgb(238,44,44));
                rentBtnTextView.setText(R.string.show_rent);
                break;
        }
    }

    protected void rent(){
        final InputPayPasswordDialog.Builder builder = new InputPayPasswordDialog.Builder(getActivity());
        builder.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = builder.getValue();
                if(password == null){
                    return;
                }
                Rent rent = new Rent();
                rent.setIdelId(idleInfo.getInfoId());
                rent.setPayPassword(password);
                presenter.addRent(rent,handler);
            }
        });
        passwordDialog= builder.createDialog();
        passwordDialog.show();
    }

    /**
     * 租赁按钮点击之后处理
     */
    private void rentBtnClick(){
        switch (relation){
            case -1:
                break;
            case 0:
                rentBtn.setBackgroundColor(Color.rgb(238,44,44));
                rentBtnTextView.setText(R.string.cancel);
                break;
            case 1:
                rentBtn.setBackgroundColor(Color.rgb(238,44,44));
                rentBtnTextView.setText(R.string.received);
                break;
            case 2:
            case 3:
                rentBtn.setBackgroundColor(Color.rgb(238,44,44));
                rentBtnTextView.setText(R.string.rent);
                break;
            case 4:
                rentBtn.setBackgroundColor(Color.rgb(238,44,44));
                rentBtnTextView.setText(R.string.end_rent);
                break;
            case 5:
                break;
            case 6:
            case 7:
                rent();
                break;
            case 8:
                rentBtn.setBackgroundColor(Color.rgb(238,44,44));
                rentBtnTextView.setText(R.string.show_rent);
                break;
        }
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
        } else {
            secondResponseInfo = null;
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
        String refuseMessage = editText.getText().toString();
        if(StringUtils.isEmpty(refuseMessage)){
            Toast.makeText(context,R.string.no_refuse_message_fill,Toast.LENGTH_SHORT).show();
            return;
        }

        if(secondResponseInfo==null) {
            //封装
            IdleInfoInterface infoInterface = (IdleInfoInterface) getActivity();
            IdleInfo idleInfo = infoInterface.getIdleInfo();
            ResponseInfo responseInfo = new ResponseInfo();
            responseInfo.setInfoId(idleInfo.getInfoId());
            responseInfo.setResponseInfo(refuseMessage);
            //发布
            presenter.addRefuse(context, responseInfo, handler);
        } else {
            if(secondResponseInfo.getResponseInfo()!=null){
                refuseMessage = secondResponseInfo.getResponseInfo() +" \t"+refuseMessage;
            }
            secondResponseInfo.setResponseInfo(refuseMessage);
            presenter.addSecondRefuse(context,secondResponseInfo,handler);
        }
    }

    protected void sendRefuseSuccess(){
        secondResponseInfo = null;

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

            final List<ResponseInfo> responseInfos = (List<ResponseInfo>) o;
            if(responseInfos.size() <=0){
                view.findViewById(R.id.none_message_tv).setVisibility(View.VISIBLE);
                refuseRecyclerView.setVisibility(View.GONE);
                return;
            }

            view.findViewById(R.id.none_message_tv).setVisibility(View.GONE);
            refuseRecyclerView.setVisibility(View.VISIBLE);
            refuseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()){
                @Override
                public boolean canScrollVertically(){
                    return false;
                }
            });
            final RefuseAdapter adapter =new RefuseAdapter(getActivity(),responseInfos);
            refuseRecyclerView.setHasFixedSize(true);
            refuseRecyclerView.setFocusable(false);

            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemCLick(View view, int position) {
                    secondResponseInfo = new SecondResponseInfo();
                    ResponseInfo responseInfo = responseInfos.get(position);
                    secondResponseInfo.setParentId(responseInfo.getResponseId());
                    secondResponseInfo.setAlterUser(responseInfo.getUserId());
                    secondResponseInfo.setInfoId(responseInfo.getInfoId());

                    Student student = responseInfo.getStudent();
                    String msg = "@"+student.getUserName();
                    secondResponseInfo.setResponseInfo(msg);
                    pushRefuseDialog(msg);
                }

                @Override
                public void onItemLongClick(View view, int position) {
                }
            });

            adapter.setSecondClickListener(new SecondRefuseListener() {
                @Override
                public void onClick(SecondResponseInfo responseInfo) {
                    secondResponseInfo = new SecondResponseInfo();
                    secondResponseInfo.setAlterUser(responseInfo.getUserId());
                    secondResponseInfo.setInfoId(responseInfo.getInfoId());
                    secondResponseInfo.setParentId(responseInfo.getParentId());

                    Student student = responseInfo.getStudent();
                    String msg = "@"+student.getUserName();
                    secondResponseInfo.setResponseInfo(msg);
                    pushRefuseDialog(msg);
                }
            });

            refuseRecyclerView.setAdapter(adapter);
        }catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    private void rentSuccess(){
        passwordDialog.cancel();
        Toast.makeText(getActivity(),R.string.rent_submit_success,Toast.LENGTH_SHORT).show();
        presenter.getRelation(idleInfo,handler);
    }

    private void rentError(Object o){
        try{
            String message = (String) o;
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
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
                case R.id.rent_btn:
                    rentBtnClick();
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
            progressView.setVisibility(View.GONE);
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
                case LOAD_RELATION_SUCCESS:
                    setRelation(msg.obj);
                    break;
                case LOAD_RELATION_ERROR:
                    linkError();
                    break;
                case RENT_SUCCESS:
                    rentSuccess();
                    break;
                case RENT_ERROR:
                    rentError(msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
