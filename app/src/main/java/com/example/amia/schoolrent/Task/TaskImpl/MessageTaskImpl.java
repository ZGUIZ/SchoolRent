package com.example.amia.schoolrent.Task.TaskImpl;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Activity.BaseAcitivity;
import com.example.amia.schoolrent.Bean.Message;
import com.example.amia.schoolrent.Bean.Result;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.NetCallBack;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.MessageTask;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.NetUtils;

import org.litepal.LitePal;

import java.util.List;

import static com.example.amia.schoolrent.Presenter.PersenterImpl.StudentContractImpl.ERROR_WITH_MESSAGE;

public class MessageTaskImpl implements MessageTask {
    @Override
    public void queryMineMessage(Context context,final Handler handler) {
        //拼接URL
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.message_list);

        NetUtils.get(url, new NetCallBack() {
            @Override
            public void finish(String json) {
                android.os.Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getObjectWithList(json,Message.class);
                    if(result.getResult()){
                        msg.what = MESSAGE;
                        if(result.getData()!=null) {
                            //保存到本地数据库
                            saveMessage((List<Message>) result.getData());
                        }

                    } else {
                        msg.what = ERROR;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = ERROR;
                } finally {
                    //查询数据库
                    msg.obj = readMessage();
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void error(String... msg) {
                android.os.Message message = handler.obtainMessage();
                message.what = ERROR_WITH_MESSAGE;
                message.obj = msg;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void requestPush(Context context, final Handler handler) {
        //拼接URL
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.mine_message);

        NetUtils.get(url, new NetCallBack() {
            @Override
            public void finish(String json) {
                android.os.Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getObjectWithList(json,Message.class);
                    if(result.getResult()){
                        msg.what = MESSAGE;
                        if(result.getData()!=null) {
                            //保存到本地数据库
                            saveMessage((List<Message>) result.getData());
                        }

                    } else {
                        msg.what = ERROR;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = ERROR;
                } finally {
                    //查询数据库
                    msg.obj = readMessage();
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void error(String... msg) {
                android.os.Message message = handler.obtainMessage();
                message.what = ERROR_WITH_MESSAGE;
                message.obj = msg;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void delMessage(Message message, Handler handler) {
        //从本地数据库删除
        try {
            message.delete();
            List<Message> messages = readMessage();
            android.os.Message msg = handler.obtainMessage();
            msg.what = DEL_SUCCESS;
            msg.obj = messages;
            handler.sendMessage(msg);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveMessage(List<Message> messages){
        for(int i = 0;i<messages.size();i++){
            Message message = messages.get(i);
            message.save();
        }
    }

    private List<Message> readMessage(){
        Student student = BaseAcitivity.getStudent();

        List<Message> messageList = LitePal.where("status != 100","userId ="+student.getUserId()).order("createDate desc").find(Message.class);
        return messageList;
    }
}
