package com.example.amia.schoolrent.Fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Activity.ActivityInterface.IdleInfoInterface;
import com.example.amia.schoolrent.Bean.AuthPicture;
import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.ResponseInfo;
import com.example.amia.schoolrent.Bean.SecondResponseInfo;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.IconClickListener;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.OnItemClickListener;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.RecAdapter;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.RefuseAdapter;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.SecondRefuseListener;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.slideswaphelper.PlusItemSlideCallback;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.slideswaphelper.WItemTouchHelperPlus;
import com.example.amia.schoolrent.ListenerAdapter.AnimationListenerAdapter;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.IdleInfoContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.UserEvalContractImpl;
import com.example.amia.schoolrent.Presenter.PersenterImpl.UserIdleContractImpl;
import com.example.amia.schoolrent.Presenter.UserEvalContract;
import com.example.amia.schoolrent.Presenter.UserIdleContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.TaskImpl.IdleTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.KeyboardUtil;
import com.example.amia.schoolrent.View.InputPayPasswordDialog;
import com.example.amia.schoolrent.View.RoundImageView;
import com.rey.material.app.BottomSheetDialog;
import com.tencent.cos.xml.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.amia.schoolrent.Task.IdleTask.CANCEL_RENT;
import static com.example.amia.schoolrent.Task.IdleTask.DIS_RENT;
import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.IDLE_RENT_LIST_ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.IDLE_RENT_LIST_SUCCESS;
import static com.example.amia.schoolrent.Task.IdleTask.LOAD_RELATION_ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.LOAD_RELATION_SUCCESS;
import static com.example.amia.schoolrent.Task.IdleTask.RENT_AGREE_ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.RENT_AGREE_SUCCESS;
import static com.example.amia.schoolrent.Task.IdleTask.RENT_COUNT;
import static com.example.amia.schoolrent.Task.IdleTask.RENT_ERROR;
import static com.example.amia.schoolrent.Task.IdleTask.RENT_SUCCESS;
import static com.example.amia.schoolrent.Task.RefuseTask.LOAD_REFUSE_ERROR;
import static com.example.amia.schoolrent.Task.RefuseTask.LOAD_REFUSE_SUCCESS;
import static com.example.amia.schoolrent.Task.RefuseTask.PUSH_REFUSE_ERROR;
import static com.example.amia.schoolrent.Task.RefuseTask.PUSH_REFUSE_SUCCESS;
import static com.example.amia.schoolrent.Task.StudentTask.BASE_INFO_ERROR;
import static com.example.amia.schoolrent.Task.StudentTask.BASE_INFO_SUCCESS;

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
    private RecyclerView rentList;
    private RelativeLayout rentListLayout;
    private LinearLayout rentLinearLayout;
    private RecAdapter recAdapter;  //租赁人员的adapter
    private boolean isShowList;  //rentList是否已经弹出

    private RelativeLayout userInfoLayout;

    private IdleInfo idleInfo;

    private UserIdleFragment userIdleFragment;
    private IdleTask task;
    private UserIdleContract.Presenter idlePresenter;
    private UserEvalFragment userEvalFragment;
    private UserEvalContract.Presenter evalPresenter;

    protected List<Fragment> fragmentList;
    protected List<String> titleList;

    //动态显示是否可见的组件
    private TextView phone;
    private TextView mail;
    private TextView realName;
    private TextView studentId;
    private TextView studenIdLabel;
    private TextView phoneLabel;
    private TextView mailLabel;
    private ImageView schoolIcon;
    private ImageView idIcon;
    private TextView sex;
    private TextView wantCount;

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
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        titleList.add(ActivityUtil.getString(getContext(),R.string.user_push));
        titleList.add(ActivityUtil.getString(getContext(),R.string.eval));

        IdleInfoInterface idleInfoInterface = (IdleInfoInterface) getActivity();
        IdleInfo idleInfo = idleInfoInterface.getIdleInfo();
        Student student = idleInfo.getStudent();

        this.idleInfo = idleInfo;

        progressView = view.findViewById(R.id.progress_view);
        progressView.setVisibility(View.VISIBLE);
        rentListLayout = view.findViewById(R.id.rent_list_rl);
        rentListLayout.setOnClickListener(onClickListener);
        rentList = view.findViewById(R.id.rent_list_view);
        rentLinearLayout = view.findViewById(R.id.request_person_ll);

        userInfoLayout = view.findViewById(R.id.resp_user_layout);
        userInfoLayout.setOnClickListener(onClickListener);


        //加载发布者头像
        ImageView userIcon = view.findViewById(R.id.user_icon_riv);
        Glide.with(getActivity()).load(student.getUserIcon()).into(userIcon);
        userIcon.setOnClickListener(onClickListener);

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

        //设置租赁人员适配器
        recAdapter = new RecAdapter(getActivity(), new RecAdapter.ResponseRentInterface() {
            @Override
            public void agree(Rent rent) {
                presenter.agreeRent(rent,handler);
            }

            @Override
            public void refuse(Rent rent) {
                progressView.setVisibility(View.VISIBLE);
                presenter.disagreeRent(rent,handler);
            }

            @Override
            public void showUserMsg(Student student) {
                loadUserInfo(student);
                presenter.getUserInfo(student,handler);
            }
        });

        rentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rentList.setAdapter(recAdapter);
        //只有未租赁的商品允许侧滑
        if(idleInfo.getStatus()==0) {
            PlusItemSlideCallback callback = new PlusItemSlideCallback(WItemTouchHelperPlus.SLIDE_ITEM_TYPE_ITEMVIEW);
            WItemTouchHelperPlus extension = new WItemTouchHelperPlus(callback);
            extension.attachToRecyclerView(rentList);
        }

        studenIdLabel = view.findViewById(R.id.student_id_label);
        phoneLabel = view.findViewById(R.id.phone);
        mailLabel = view.findViewById(R.id.mail);

        schoolIcon = view.findViewById(R.id.student_id_validate);
        idIcon = view.findViewById(R.id.real_name_validate);

        wantCount = view.findViewById(R.id.num_of_wanted);
        presenter.getRentCount(idleInfo.getInfoId(),handler);
    }

    private void setRentBtn(Student student,IdleInfo idleInfo){
        //完成或者已经被禁止显示
        if(idleInfo.getStatus() != 0){
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
            case 1:
                rentBtn.setBackgroundColor(Color.rgb(192,192,192));
                rentBtnTextView.setText(R.string.requested);
                break;
            case 2:
            case 3:
                rentBtn.setBackgroundColor(Color.rgb(238,44,44));
                rentBtnTextView.setText(R.string.rent);
                break;
            case 4:
                rentBtn.setBackgroundColor(Color.rgb(192,192,192));
                rentBtnTextView.setText(R.string.renting);
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
        builder.setPayNum(idleInfo.getDeposit());
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
                rent();
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
                /*rentBtn.setBackgroundColor(Color.rgb(238,44,44));
                rentBtnTextView.setText(R.string.show_rent);*/
                if(isShowList){
                    hideRentList();
                } else {
                    presenter.getRentList(idleInfo,handler);
                    showRent();
                }
                break;
        }
    }

    private void showRent(){
        isShowList =true;
        rentListLayout.setVisibility(View.VISIBLE);
        Animation inAnimation= AnimationUtils.loadAnimation(getActivity(),R.anim.push_bottom_in_short);
        Animation alphaAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.alpha_anim);
        rentListLayout.startAnimation(alphaAnimation);
        rentLinearLayout.startAnimation(inAnimation);
    }

    private void loadRentPerson(Object o){
        try {
            List<Rent> rents = (List<Rent>) o;
            recAdapter.setList(rents);
            if(rents == null ||rents.size()<=0){
                view.findViewById(R.id.null_person_tv).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.null_person_tv).setVisibility(View.GONE);
            }
        } catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    private void hideRentList(){
        isShowList = false;
        Animation outAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.push_buttom_out);
        Animation alphaAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.alpha_out_anim);
        outAnimation.setFillAfter(true);
        outAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationStart(Animation animation) {
                super.onAnimationStart(animation);
                rentListLayout.setVisibility(View.GONE);
            }
        });
        rentLinearLayout.startAnimation(outAnimation);
        rentListLayout.startAnimation(alphaAnimation);
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
     * 加载用户信息
     * @param student
     */
    protected void loadUserInfo(Student student){
        userInfoLayout.setVisibility(View.VISIBLE);

        Animation inAnimation= AnimationUtils.loadAnimation(getActivity(),R.anim.push_bottom_in_short);
        Animation alphaAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.alpha_anim);
        userInfoLayout.startAnimation(alphaAnimation);
        View v = view.findViewById(R.id.info_layout);
        v.startAnimation(inAnimation);

        ImageView userIcon = userInfoLayout.findViewById(R.id.user_icon);
        TextView userName = userInfoLayout.findViewById(R.id.user_name);
        TextView credit = userInfoLayout.findViewById(R.id.score);


        sex = userInfoLayout.findViewById(R.id.sex_tv);
        sex.setText(student.getSex());
        realName = userInfoLayout.findViewById(R.id.real_name);
        phone = userInfoLayout.findViewById(R.id.telephone);
        mail = userInfoLayout.findViewById(R.id.email);
        studentId = userInfoLayout.findViewById(R.id.student_id);

        loadHideInfo(student);

        Glide.with(getContext()).load(student.getUserIcon()).into(userIcon);
        userName.setText(student.getUserName());
        //判断是否有看到真实姓名的权限
        IdleInfoInterface infoInterface = (IdleInfoInterface) getActivity();
        Student s = infoInterface.getStudent();

        //判断是否有有看到真实姓名的权限
        if(s.getUserId().equals(idleInfo.getUserId())) {
            setMessageRead();
        } else {
            setMessageUnRead();
        }

        credit.setText(String.valueOf(student.getCredit()));

        //找到对应的Tab
        ViewPager viewPager = userInfoLayout.findViewById(R.id.view_pager1);
        TabLayout tabLayout = userInfoLayout.findViewById(R.id.tab_layout1);

        //加载信息
        loadInfoTab(student);
        loadEvalTab(student);

        //将Fragment与Tab关联
        viewPager.setAdapter(new MyRentFragmentAdapter(getActivity().getSupportFragmentManager(),getActivity(),fragmentList,titleList));
        tabLayout.setupWithViewPager(viewPager);
    }

    protected void setMessageUnRead(){
        realName.setVisibility(View.GONE);
        studentId.setVisibility(View.GONE);
        phone.setVisibility(View.GONE);
        mail.setVisibility(View.GONE);
        studenIdLabel.setVisibility(View.GONE);
        phoneLabel.setVisibility(View.GONE);
        mailLabel.setVisibility(View.GONE);
        schoolIcon.setVisibility(View.GONE);
        idIcon.setVisibility(View.GONE);
    }

    protected void setMessageRead(){
        realName.setVisibility(View.VISIBLE);
        studentId.setVisibility(View.VISIBLE);
        phone.setVisibility(View.VISIBLE);
        mail.setVisibility(View.VISIBLE);
        studenIdLabel.setVisibility(View.VISIBLE);
        phoneLabel.setVisibility(View.VISIBLE);
        mailLabel.setVisibility(View.VISIBLE);
        schoolIcon.setVisibility(View.VISIBLE);
        idIcon.setVisibility(View.VISIBLE);
    }

    protected void loadUserInfoSuccess(Object o){
        try{
            Student student = (Student) o;
            loadHideInfo(student);
            setValidateMessage(student);
        } catch (Exception e){
            e.printStackTrace();
            linkError();
        }
    }

    protected void loadHideInfo(Student student){
        phone.setText(student.getTelephone());
        mail.setText(student.getEmail());
        studentId.setText(student.getStudentId());
        realName.setText(student.getRealName());
        sex.setText(student.getSex());
    }

    private void setValidateMessage(Student student){
        List<AuthPicture> authPictures = student.getAuthPictureList();

        if(authPictures == null || authPictures.size() <= 0){
            return;
        }

        //设置验证信息
        for(AuthPicture authPicture:authPictures){
            int type = authPicture.getType();
            int status =authPicture.getStatus();
            if( type == 1){
                switch (status){
                    case 0:
                        break;
                    case 1:
                        schoolIcon.setImageResource(R.drawable.validate);
                        break;
                    case 2:
                        schoolIcon.setImageResource(R.drawable.unvalidate);
                        break;
                    case 3:
                        schoolIcon.setImageResource(R.drawable.unvalidate);
                        break;
                }
            } else if(type == 2){
                switch (status){
                    case 0:
                        break;
                    case 1:
                        idIcon.setImageResource(R.drawable.validate);
                        break;
                    case 2:
                        idIcon.setImageResource(R.drawable.unvalidate);
                        break;
                    case 3:
                        idIcon.setImageResource(R.drawable.validate);
                        break;
                }
            }
        }
    }

    protected void loadInfoTab(Student student){
        if(userIdleFragment == null){
            userIdleFragment = UserIdleFragment.newInstance();
            fragmentList.add(userIdleFragment);
        }
        if(task == null){
            task = new IdleTaskImpl();
        }
        if(idlePresenter == null) {
            idlePresenter = new UserIdleContractImpl(userIdleFragment, task);
            userIdleFragment.setPresenter(idlePresenter);
        }
        userIdleFragment.setStudent(student);
    }

    protected void loadEvalTab(Student student){
        if(userEvalFragment == null){
            userEvalFragment = UserEvalFragment.newInstance();
            fragmentList.add(userEvalFragment);
        }
        if(task == null){
            task = new IdleTaskImpl();
        }
        if(evalPresenter == null){
            evalPresenter = new UserEvalContractImpl(userEvalFragment,task);
            userEvalFragment.setPresenter(evalPresenter);
        }
        userEvalFragment.setStudent(student);
    }

    private void hideInfoLayout(){
        Animation outAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.push_buttom_out);
        Animation alphaAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.alpha_out_anim);
        outAnimation.setFillAfter(true);
        outAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationStart(Animation animation) {
                super.onAnimationStart(animation);
                userInfoLayout.setVisibility(View.GONE);
            }
        });
        View v = view.findViewById(R.id.info_layout);
        v.startAnimation(outAnimation);
        userInfoLayout.startAnimation(alphaAnimation);
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

            adapter.setIconClickListener(new IconClickListener() {
                @Override
                public void onClick(Student student) {
                    loadUserInfo(student);
                    presenter.getUserInfo(student,handler);
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

    protected void agreeSuccess(){
        idleInfo.setStatus(1);
        Toast.makeText(getActivity(),R.string.agree_success,Toast.LENGTH_SHORT).show();
        presenter.getRentList(idleInfo,handler);
    }

    protected void cancelSuccess(){
        progressView.setVisibility(View.GONE);
        Toast.makeText(getContext(),R.string.behavior_success,Toast.LENGTH_SHORT).show();
        presenter.getRentList(idleInfo,handler);
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
                case R.id.progress_view:
                    break;
                case R.id.rent_list_rl:
                    hideRentList();
                    break;
                case R.id.resp_user_layout:
                    hideInfoLayout();
                    break;
                case R.id.user_icon_riv:
                    currentIdleUser();
                    break;
            }
        }
    };

    private void currentIdleUser(){
        IdleInfoInterface idleInfoInterface = (IdleInfoInterface) getActivity();
        IdleInfo idleInfo = idleInfoInterface.getIdleInfo();
        Student student = idleInfo.getStudent();
        loadUserInfo(student);

        presenter.getUserInfo(student,handler);
    }

    private void getRentCount(Object o){
        try{
            Integer count = (Integer) o;
            wantCount.setText(String.valueOf(count));
        } catch (Exception e){
            e.printStackTrace();
            linkError();
        }
    }

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
                case DIS_RENT:
                case CANCEL_RENT:
                    cancelSuccess();
                    break;
                case RENT_ERROR:
                    rentError(msg.obj);
                    break;
                case IDLE_RENT_LIST_SUCCESS:
                    loadRentPerson(msg.obj);
                    break;
                case IDLE_RENT_LIST_ERROR:
                    linkError();
                    break;
                case RENT_AGREE_SUCCESS:
                    agreeSuccess();
                    break;
                case BASE_INFO_SUCCESS:
                    loadUserInfoSuccess(msg.obj);
                    break;
                case RENT_COUNT:
                    getRentCount(msg.obj);
                    break;
                case RENT_AGREE_ERROR:
                    break;
                case BASE_INFO_ERROR:
                case ERROR:
                default:
                    linkError();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
