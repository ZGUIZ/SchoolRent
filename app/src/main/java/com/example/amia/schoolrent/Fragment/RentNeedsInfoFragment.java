package com.example.amia.schoolrent.Fragment;

import android.app.Activity;
import android.content.Context;
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
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Activity.ActivityInterface.IdleInfoInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.RentNeedsInterface;
import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.ResponseInfo;
import com.example.amia.schoolrent.Bean.SecondResponseInfo;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.IconClickListener;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.OnItemClickListener;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.RefuseAdapter;
import com.example.amia.schoolrent.Fragment.RecyclerAdapter.SecondRefuseListener;
import com.example.amia.schoolrent.ListenerAdapter.AnimationListenerAdapter;
import com.example.amia.schoolrent.Presenter.PersenterImpl.UserEvalContractImpl;
import com.example.amia.schoolrent.Presenter.PersenterImpl.UserIdleContractImpl;
import com.example.amia.schoolrent.Presenter.RentNeedsContract;
import com.example.amia.schoolrent.Presenter.UserEvalContract;
import com.example.amia.schoolrent.Presenter.UserIdleContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.TaskImpl.IdleTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.DateUtil;
import com.example.amia.schoolrent.Util.KeyboardUtil;
import com.example.amia.schoolrent.View.RoundImageView;
import com.rey.material.app.BottomSheetDialog;
import com.tencent.cos.xml.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.amia.schoolrent.Task.RefuseTask.LOAD_REFUSE_SUCCESS;
import static com.example.amia.schoolrent.Task.RefuseTask.PUSH_REFUSE_SUCCESS;
import static com.example.amia.schoolrent.Task.StudentTask.BASE_INFO_SUCCESS;

public class RentNeedsInfoFragment extends Fragment implements RentNeedsContract.View {
    private View view;
    private BottomSheetDialog dialog;  //回复弹出窗
    private View refuseView;  //回复输入框的View
    private EditText editText;  //回复输入框
    private RecyclerView refuseRecyclerView; // 回复信息的Layout
    private RelativeLayout progressView;

    private RentNeedsContract.Presenter presenter;

    private SecondResponseInfo secondResponseInfo;

    private RentNeeds rentNeeds;
    private Student student;

    private RelativeLayout userInfoLayout;
    private TextView sex;

    protected List<Fragment> fragmentList;
    protected List<String> titleList;

    private UserIdleFragment userIdleFragment;
    private IdleTask task;
    private UserIdleContract.Presenter idlePresenter;
    private UserEvalFragment userEvalFragment;
    private UserEvalContract.Presenter evalPresenter;

    public static RentNeedsInfoFragment newInstance(){
        RentNeedsInfoFragment rentNeedsInfoFragment = new RentNeedsInfoFragment();
        return rentNeedsInfoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_need_info_layout,container,false);
        init();
        return view;
    }

    private void init(){
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        titleList.add(ActivityUtil.getString(getContext(),R.string.user_push));
        titleList.add(ActivityUtil.getString(getContext(),R.string.eval));

        userInfoLayout = view.findViewById(R.id.resp_user_layout);

        RentNeedsInterface rentNeedsInterface = (RentNeedsInterface) getActivity();
        rentNeeds = rentNeedsInterface.getRentNeeds();
        student = ((RentNeedsInterface) getActivity()).getStudent();

        Student needStudent = rentNeeds.getStudent();
        RoundImageView imageView = view.findViewById(R.id.user_icon_riv);
        Glide.with(getActivity()).load(needStudent.getUserIcon()).into(imageView);
        imageView.setOnClickListener(onClickListener);
        TextView userName = view.findViewById(R.id.user_name_tv);
        userName.setText(needStudent.getUserName());
        TextView info = view.findViewById(R.id.idle_info_detail);
        info.setText(rentNeeds.getIdelInfo());
        TextView time = view.findViewById(R.id.create_time);
        time.setText(DateUtil.formatDate(rentNeeds.getCreateDate(),"yyyy-MM-dd hh:mm"));

        progressView = view.findViewById(R.id.progress_view);

        view.findViewById(R.id.refuse_rl).setOnClickListener(onClickListener);
        refuseRecyclerView = view.findViewById(R.id.refuse_ll);

        TextView credit = view.findViewById(R.id.credit_tv);
        credit.setText(ActivityUtil.getString(getActivity(),R.string.credit)+needStudent.getCredit());

        loadResponseInfo();

        view.findViewById(R.id.resp_user_layout).setOnClickListener(onClickListener);
    }

    /**
     * 加载回复信息
     */
    private void loadResponseInfo(){
        progressView.setVisibility(View.VISIBLE);
        presenter.loadResponseInfo(rentNeeds,handler);
    }

    @Override
    public void setPresenter(RentNeedsContract.Presenter presenter) {
        this.presenter = presenter;
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

        ImageView imageView = refuseView.findViewById(R.id.user_icon_iv);
        Glide.with(activity).load(student.getUserIcon()).into(imageView);

        refuseView.findViewById(R.id.send_btn).setOnClickListener(onClickListener);
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
            ResponseInfo responseInfo = new ResponseInfo();
            responseInfo.setInfoId(rentNeeds.getInfoId());
            responseInfo.setResponseInfo(refuseMessage);
            //发布
            presenter.addRefuse(responseInfo, handler);
        } else {
            if(secondResponseInfo.getResponseInfo()!=null){
                refuseMessage = secondResponseInfo.getResponseInfo() +" \t"+refuseMessage;
            }
            secondResponseInfo.setResponseInfo(refuseMessage);
            presenter.addSecondRefuse(secondResponseInfo,handler);
        }
    }

    protected void loadBaseInfoSuccess(Object o){
        try{
            Student student = (Student) o;
            loadUserInfo(student);
        }catch (Exception e){
            e.printStackTrace();
            linkError();
        }
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

        Glide.with(getContext()).load(student.getUserIcon()).into(userIcon);
        userName.setText(student.getUserName());

        setMessageUnRead();

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

    /**
     * 隐藏部分字段
     */
    protected void setMessageUnRead(){
        view.findViewById(R.id.real_name).setVisibility(View.GONE);
        view.findViewById(R.id.real_name_validate).setVisibility(View.GONE);
        view.findViewById(R.id.student_id_label).setVisibility(View.GONE);
        view.findViewById(R.id.student_id).setVisibility(View.GONE);
        view.findViewById(R.id.student_id_validate).setVisibility(View.GONE);
        view.findViewById(R.id.phone).setVisibility(View.GONE);
        view.findViewById(R.id.telephone).setVisibility(View.GONE);
        view.findViewById(R.id.mail).setVisibility(View.GONE);
        view.findViewById(R.id.email).setVisibility(View.GONE);
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

    /**
     * 加载回复信息
     * @param o
     */
    protected void loadRefuse(Object o){
        progressView.setVisibility(View.GONE);
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

            adapter.setIconClickListener(new IconClickListener() {
                @Override
                public void onClick(Student student) {
                    presenter.getStudentInfo(student,handler);
                }
            });

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

    protected void sendRefuseSuccess(){
        secondResponseInfo = null;

        if(dialog!=null){
            dialog.cancel();
        }
        //重新加载回复信息
        loadResponseInfo();
    }

    private void errorWithMessage(Object o){
        progressView.setVisibility(View.GONE);
        if(o == null){
            linkError();
            return;
        }
        try{
            String str = (String) o;
            Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            linkError();
        }
    }

    private void linkError(){
        Toast.makeText(getContext(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    /**
     * 隐藏键盘
     * @param editText
     */
    protected void hideKeyBoard(EditText editText){
        KeyboardUtil keyboardUtil = KeyboardUtil.getInstance();
        keyboardUtil.hideKeyBoard(getActivity(),editText);
    }

    private void loadArticleUser(){
        Student student = new Student();
        student.setUserId(rentNeeds.getUserId());
        presenter.getStudentInfo(student,handler);
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

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PUSH_REFUSE_SUCCESS:
                    sendRefuseSuccess();
                    break;
                case LOAD_REFUSE_SUCCESS:
                    loadRefuse(msg.obj);
                    break;
                case BASE_INFO_SUCCESS:
                    loadBaseInfoSuccess(msg.obj);
                    break;
                default:
                    errorWithMessage(msg.obj);

            }
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.key_board_down:
                    hideKeyBoard(editText);
                    break;
                case R.id.send_btn:
                    sendRefuseMessage();
                    break;
                case R.id.refuse_rl:
                    pushRefuseDialog();
                    break;
                case R.id.resp_user_layout:
                    hideInfoLayout();
                    break;
                case R.id.user_icon_riv:
                    loadArticleUser();
                    break;
            }
        }
    };
}
