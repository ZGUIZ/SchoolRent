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
import android.widget.Toast;

import com.example.amia.schoolrent.R;

public class InputPayPasswordDialog extends Dialog {
    public InputPayPasswordDialog(@NonNull Context context) {
        super(context);
    }

    public InputPayPasswordDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{
        private String positiveButtonText;
        private String negativeButtonText;
        private InputPayPasswordDialog scheTimeDialog;
        private View layout;
        private EditText passWordInput;

        private Context context;

        private View.OnClickListener positiveButtonClickListener;
        private View.OnClickListener negativeButtonClickListener;

        public Builder(Context context){
            this.context = context;
            scheTimeDialog=new InputPayPasswordDialog(context, R.style.Theme_AppCompat_Light_Dialog);
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout=inflater.inflate(R.layout.pay_password_input_layout,null);
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

        public String getValue(){
            String password = passWordInput.getText().toString().trim();
            if(TextUtils.isEmpty(password)){
                Toast.makeText(context,R.string.pay_password_null,Toast.LENGTH_SHORT).show();
                return null;
            }
            return password;
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

        public InputPayPasswordDialog createDialog() {
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
            passWordInput=layout.findViewById(R.id.password_input);
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
