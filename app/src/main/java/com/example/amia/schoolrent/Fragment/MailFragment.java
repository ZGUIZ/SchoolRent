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
import android.widget.Toast;

import com.example.amia.schoolrent.Presenter.StudentContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;

import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.ERROR_WITH_MESSAGE;
import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.SEND_SUCCESS;

public class MailFragment extends Fragment implements StudentContract.View {
    private View view;

    private static MailFragment mailFragment;

    protected StudentContract.Presenter presenter;

    public static MailFragment newInstance(){
        if(mailFragment == null){
            mailFragment=new MailFragment();
        }
        return mailFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mail,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;
        presenter = null;
    }

    protected void init(){
        view.findViewById(R.id.back_layout).setOnClickListener(onClickListener);
        view.findViewById(R.id.send_mail_btn).setOnClickListener(onClickListener);
    }

    protected void sendMail(){
        EditText editText = view.findViewById(R.id.mailAddress);
        String address = editText.getText().toString().trim();
        //空邮箱不发送邮件
        if(address == null || "".equals(address)){
            Toast.makeText(getActivity(),R.string.mail_null,Toast.LENGTH_SHORT).show();
            return;
        }
        presenter.sendRegisterMail(address,handler);
    }

    protected void sendSuccess(){
        Toast.makeText(getActivity(), ActivityUtil.getString(getActivity(),R.string.send_mail_success),Toast.LENGTH_SHORT).show();
    }

    protected View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.back_layout:
                    getActivity().finish();
                    break;
                case R.id.send_mail_btn:
                    sendMail();
                    break;
            }
        }
    };

    @Override
    public void linkError() {
        Toast.makeText(getContext(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(StudentContract.Presenter presenter) {
        this.presenter = presenter;
    }

    protected Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SEND_SUCCESS:
                    sendSuccess();
                    break;
                case ERROR_WITH_MESSAGE:

                    break;
            }

            super.handleMessage(msg);
        }
    };
}
