package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;
import android.os.Message;

import com.example.amia.schoolrent.Presenter.SplashContract;
import com.example.amia.schoolrent.Task.KeyTask;

public class SplashContractImpl implements com.example.amia.schoolrent.Presenter.SplashContract.Presenter {
    private SplashContract.View view;
    private KeyTask keyTask;
    public SplashContractImpl(SplashContract.View view, KeyTask task){
        this.view =view;
        this.keyTask = task;
    }

    @Override
    public void onStart(final SplashContract.CallBack callBack) throws Exception {
        handler.setCallBack(callBack);
        Handler h = handler;
        keyTask.isChangeKey(view.getContext(),h);
    }

    private MyHandler handler = new MyHandler();

    class MyHandler extends Handler{
        private SplashContract.CallBack callBack;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            callBack.setStatus(msg);
        }
        public void setCallBack(SplashContract.CallBack callBack) {
            this.callBack = callBack;
        }
    }
}
