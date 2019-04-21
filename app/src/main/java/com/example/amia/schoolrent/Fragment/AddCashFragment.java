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
import android.widget.Toast;

import com.example.amia.schoolrent.Activity.ActivityInterface.CapitalInterface;
import com.example.amia.schoolrent.Bean.Capital;
import com.example.amia.schoolrent.Bean.CapitalCash;
import com.example.amia.schoolrent.Presenter.CashContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.KeyboardUtil;
import com.rey.material.widget.RadioButton;

import static com.example.amia.schoolrent.Task.ChargeTask.ADD_CAPITALCASH;
import static com.example.amia.schoolrent.Task.IdleTask.ERROR;

public class AddCashFragment extends Fragment implements CashContract.View {

    public static final int ALIPAY = 0;
    public static final int WECHAT = 1;

    private CashContract.Presenter presenter;

    private View view;

    protected Capital capital;
    protected CapitalCash capitalCash;

    protected RadioButton alipay;
    protected RadioButton wechat;
    private EditText account;
    private EditText money;
    private Button submit;


    public static AddCashFragment newInstance(){
        AddCashFragment cashFragment = new AddCashFragment();
        return cashFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_cash_layout,container,false);
        init();
        return view;
    }

    private void init(){
        capitalCash = new CapitalCash();
        capital = ((CapitalInterface) getActivity()).getCapital();
        alipay = view.findViewById(R.id.alipay);
        wechat = view.findViewById(R.id.wechat);

        alipay.setOnClickListener(onClickListener);
        wechat.setOnClickListener(onClickListener);

        account = view.findViewById(R.id.account);
        money = view.findViewById(R.id.money);
        submit = view.findViewById(R.id.submit_btn);
        submit.setOnClickListener(onClickListener);
    }

    protected void submit(){
        KeyboardUtil keyboardUtil = KeyboardUtil.getInstance();
        keyboardUtil.hideKeyBoard(getContext(),account);
        keyboardUtil.hideKeyBoard(getContext(),money);

        if(capitalCash.getSource() == null){
            Snack(R.string.type_select);
            return;
        }

        String acc = account.getText().toString().trim();
        if("".equals(acc)){
            Snack(R.string.account_null);
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

        if(m>capital.getCapital()){
            Snack(R.string.money_out_border);
            return;
        }

        capitalCash.setAccount(acc);
        capitalCash.setCapital(m);
        presenter.addCash(capitalCash,handler);

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
    public void linkError() {
        Toast.makeText(getActivity(), R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(CashContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.alipay:
                    wechat.setChecked(false);
                    capitalCash.setSource(ALIPAY);
                    break;
                case R.id.wechat:
                    alipay.setChecked(false);
                    capitalCash.setSource(WECHAT);
                    break;
                case R.id.submit_btn:
                    submit();
                    break;
            }
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            view.findViewById(R.id.progress_view).setVisibility(View.GONE);
            super.handleMessage(msg);
            switch (msg.what){
                case ADD_CAPITALCASH:
                    Toast.makeText(getContext(),R.string.submit_success,Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    break;
                case ERROR:
                    try{
                        Snack((String)msg.obj);
                    } catch (Exception e){
                        e.printStackTrace();
                        linkError();
                    }
                    break;
            }
        }
    };
}
