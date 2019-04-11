package com.example.amia.schoolrent.View;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amia.schoolrent.Bean.Eval;
import com.example.amia.schoolrent.Bean.Message;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.R;

public class MessageDialog extends Dialog {
    public MessageDialog(@NonNull Context context) {
        super(context);
    }

    public MessageDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{
        private String positiveButtonText;
        private MessageDialog scheTimeDialog;
        private View layout;

        private TextView memoView;
        private TextView titleView;

        private Context context;

        private View.OnClickListener positiveButtonClickListener;
        private View.OnClickListener negativeButtonClickListener;

        public Builder(Context context, Message msg){
            this.context = context;
            scheTimeDialog=new MessageDialog(context, R.style.Theme_AppCompat_Light_Dialog);
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout=inflater.inflate(R.layout.dialog_message_layout,null);
            memoView = layout.findViewById(R.id.memo);
            titleView = layout.findViewById(R.id.sche_dialog_tv);

            titleView.setText(msg.getTitle());
            memoView.setText(msg.getContent());

            scheTimeDialog.addContentView(layout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        }

        private void setDefaultNegativeListener(){
            this.negativeButtonClickListener=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scheTimeDialog.cancel();
                }
            };
        }

        private void setDefaultPositiveListener(){
            this.positiveButtonClickListener=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scheTimeDialog.cancel();
                }
            };
        }

        public Builder setPositiveButton(String positiveButtonText, View.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(View.OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public void cancleDialog(){
            if(scheTimeDialog!=null||scheTimeDialog.isShowing()){
                scheTimeDialog.cancel();
            }
        }

        public MessageDialog createDialog() {
            if (positiveButtonClickListener == null) {
                setDefaultPositiveListener();
            }
            if(negativeButtonClickListener == null){
                setDefaultNegativeListener();
            }
            layout.findViewById(R.id.positiveButton).setOnClickListener(positiveButtonClickListener);
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
            } else {
                ((Button) layout.findViewById(R.id.positiveButton)).setText("确定");
            }
            create();
            return scheTimeDialog;
        }

        private void create(){
            scheTimeDialog.setContentView(layout);
            scheTimeDialog.setCancelable(true);
            scheTimeDialog.setCanceledOnTouchOutside(true);
        }
    }
}
