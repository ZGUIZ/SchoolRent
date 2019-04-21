package com.example.amia.schoolrent.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Activity.ActivityInterface.CapitalInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.PicInterface;
import com.example.amia.schoolrent.Activity.BaseAcitivity;
import com.example.amia.schoolrent.Bean.Capital;
import com.example.amia.schoolrent.Bean.CapitalCash;
import com.example.amia.schoolrent.Bean.Charge;
import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.CashContract;
import com.example.amia.schoolrent.Presenter.ChargeContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.COSUtil;
import com.example.amia.schoolrent.Util.KeyboardUtil;
import com.rey.material.widget.RadioButton;

import java.util.UUID;

import static com.example.amia.schoolrent.Task.ChargeTask.ADD_CAPITALCASH;
import static com.example.amia.schoolrent.Task.ChargeTask.ADD_CHARGE;
import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Util.COSUtil.RESULT_ERROR;
import static com.example.amia.schoolrent.Util.COSUtil.RESULT_SUCCESS;

public class AddChargeFragment extends Fragment implements ChargeContract.View {

    public static final int ALIPAY = 0;
    public static final int WECHAT = 1;

    private ChargeContract.Presenter presenter;

    private View view;

    protected Charge charge;

    protected RadioButton alipay;
    protected RadioButton wechat;
    protected ImageView addBtn;

    private EditText money;
    private Button submit;


    public static AddChargeFragment newInstance(){
        AddChargeFragment cashFragment = new AddChargeFragment();
        return cashFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charge,container,false);
        init();
        return view;
    }

    private void init(){
        charge = new Charge();
        alipay = view.findViewById(R.id.alipay);
        wechat = view.findViewById(R.id.wechat);

        alipay.setOnClickListener(onClickListener);
        wechat.setOnClickListener(onClickListener);

        money = view.findViewById(R.id.money);
        submit = view.findViewById(R.id.submit_btn);
        submit.setOnClickListener(onClickListener);

        addBtn = view.findViewById(R.id.pic);
        addBtn.setOnClickListener(onClickListener);
    }

    protected void submit(){
        KeyboardUtil keyboardUtil = KeyboardUtil.getInstance();
        keyboardUtil.hideKeyBoard(getContext(), money);

        if(charge.getSource() == null){
            Snack(R.string.type_select);
            return;
        }

        float m = 0.0f;
        try {
            String mon = money.getText().toString().trim();
            m = Float.parseFloat(mon);
        } catch (Exception e){
            e.printStackTrace();
            Snack(R.string.money_format_error);
            return;
        }

        if(charge.getPic() == null){
            Snack(R.string.null_pay_pic);
            return;
        }

        charge.setMoney(m);
        presenter.addCharge(charge,handler);
        view.findViewById(R.id.progress_view).setVisibility(View.VISIBLE);
    }

    protected void Snack(String msg){
        if("".equals(msg)){
            Snackbar.make(view,R.string.link_error,Snackbar.LENGTH_SHORT).show();
            return;
        }
        Snackbar.make(view,msg,Toast.LENGTH_SHORT).show();
    }

    protected void Snack(int msg){
        Snackbar.make(view,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addPic(String path) {
        view.findViewById(R.id.progress_view).setVisibility(View.VISIBLE);
        if(path == null || "".equals(path)){
            return;
        }
        Student student = BaseAcitivity.getStudent();
        String tail = UUID.randomUUID().toString().replace("-","");
        //上传图片
        COSUtil cosUtil = new COSUtil(getActivity());
        String fileName = cosUtil.uploadFile(student,path,handler,tail);

        charge.setPic(ActivityUtil.getString(getActivity(),R.string.image_host)+fileName);
    }

    @Override
    public void linkError() {
        Toast.makeText(getActivity(), R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(ChargeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.alipay:
                    wechat.setChecked(false);
                    charge.setSource(ALIPAY);
                    break;
                case R.id.wechat:
                    alipay.setChecked(false);
                    charge.setSource(WECHAT);
                    break;
                case R.id.submit_btn:
                    submit();
                    break;
                case R.id.pic:
                    ((PicInterface)getActivity()).choosePic();
                    break;
            }
        }
    };

    private void uploadSuccess(){
        view.findViewById(R.id.progress_view).setVisibility(View.GONE);
        Glide.with(getContext()).load(charge.getPic()).into(addBtn);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ADD_CHARGE:
                    view.findViewById(R.id.progress_view).setVisibility(View.GONE);
                    Toast.makeText(getContext(),R.string.submit_success,Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    break;
                case ERROR:
                    try{
                        view.findViewById(R.id.progress_view).setVisibility(View.GONE);
                        Snack((String)msg.obj);
                    } catch (Exception e){
                        e.printStackTrace();
                        linkError();
                    }
                    break;
                case RESULT_SUCCESS:
                    uploadSuccess();
                    break;
                case RESULT_ERROR:
                    view.findViewById(R.id.progress_view).setVisibility(View.GONE);
                    charge.setPic(null);
                    linkError();
                    break;
            }
        }
    };
}
