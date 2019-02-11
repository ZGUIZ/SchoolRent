package com.example.amia.schoolrent.Util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

public class KeyboardUtil {
    private static KeyboardUtil keyboardUtil;
    private KeyboardUtil(){
    }

    public static KeyboardUtil getInstance(){
        if(keyboardUtil == null){
            keyboardUtil = new KeyboardUtil();
        }
        return keyboardUtil;
    }

    /**
     * 隐藏软键盘
     * @param context
     * @param view
     */
    public void hideKeyBoard(Context context, View view){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public void openKeyBoard(final Context context, final EditText view){
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(view,0);
                view.setSelection(view.getText().length());
            }
        },200);
    }
}
