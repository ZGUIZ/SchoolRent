package com.example.amia.schoolrent.View;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amia.schoolrent.Bean.Eval;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.R;

public class DestoryDialog extends Dialog {
    public DestoryDialog(@NonNull Context context) {
        super(context);
    }

    public DestoryDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{
        private String positiveButtonText;
        private String negativeButtonText;
        private DestoryDialog scheTimeDialog;
        private View layout;

        private TextView evalTextView;
        private RatingBar ratingBar;

        private Context context;

        private View.OnClickListener positiveButtonClickListener;
        private View.OnClickListener negativeButtonClickListener;

        public Builder(Context context){
            this.context = context;
            scheTimeDialog=new DestoryDialog(context, R.style.Theme_AppCompat_Light_Dialog);
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout=inflater.inflate(R.layout.dialog_destory_layout,null);
            evalTextView = layout.findViewById(R.id.fill_eval);
            ratingBar = layout.findViewById(R.id.rating_bar);
            scheTimeDialog.addContentView(layout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        }

        public Builder setEditAble(boolean flag){
            EditText contentView = layout.findViewById(R.id.fill_eval);
            if(!flag) {
                contentView.setEnabled(false);
                contentView.setFocusable(false);
                contentView.setKeyListener(null);
            }
            return this;
        }

        public Builder setContent(String content){
            EditText contentView = layout.findViewById(R.id.fill_eval);
            contentView.setText(content);
            return this;
        }

        public Builder setContent(int content){
            EditText contentView = layout.findViewById(R.id.fill_eval);
            contentView.setText(content);
            return this;
        }

        public Builder setTitle(String title){
            TextView titleView = layout.findViewById(R.id.sche_dialog_tv);
            titleView.setText(title);
            return this;
        }

        public Builder setTitle(int title){
            TextView titleView = layout.findViewById(R.id.sche_dialog_tv);
            titleView.setText(title);
            return this;
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

        public String getFill(){
            String val = evalTextView.getText().toString().trim();
            if(TextUtils.isEmpty(val)){
                Toast.makeText(context,R.string.destroy_null,Toast.LENGTH_SHORT).show();
                return null;
            }
            return val;
        }

        public Builder setNegativeButton(String negativeButtonText, View.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
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

        public DestoryDialog createDialog() {
            if (positiveButtonClickListener == null) {
                setDefaultPositiveListener();
            }
            if(negativeButtonClickListener == null){
                setDefaultNegativeListener();
            }
            layout.findViewById(R.id.positiveButton).setOnClickListener(positiveButtonClickListener);
            layout.findViewById(R.id.negativeButton).setOnClickListener(negativeButtonClickListener);
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
            } else {
                ((Button) layout.findViewById(R.id.positiveButton)).setText("确定");
            }
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
            } else {
                ((Button) layout.findViewById(R.id.negativeButton)).setText("取消");
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
