package com.example.amia.schoolrent.ListenerAdapter;

import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;

public class KeyListenerAdapter implements KeyListener {
    @Override
    public int getInputType() {
        return 0;
    }

    @Override
    public boolean onKeyDown(View view, Editable editable, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public boolean onKeyUp(View view, Editable editable, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public boolean onKeyOther(View view, Editable editable, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void clearMetaKeyState(View view, Editable editable, int i) {

    }
}
