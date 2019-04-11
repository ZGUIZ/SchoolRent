package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.os.Handler;

import com.example.amia.schoolrent.Bean.Message;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.MyMessageContract;
import com.example.amia.schoolrent.Task.MessageTask;


public class MyMessageContractImpl implements MyMessageContract.Presenter {

    private BaseView view;
    private MessageTask task;

    public MyMessageContractImpl(BaseView view, MessageTask task) {
        this.view = view;
        this.task = task;
    }

    @Override
    public void getMyMessage(Handler handler) {
        task.queryMineMessage(view.getContext(),handler);
    }

    @Override
    public void requestPush(Handler handler) {
        task.requestPush(view.getContext(),handler);
    }

    @Override
    public void delMessage(Message message, Handler handler) {
        task.delMessage(message,handler);
    }
}
