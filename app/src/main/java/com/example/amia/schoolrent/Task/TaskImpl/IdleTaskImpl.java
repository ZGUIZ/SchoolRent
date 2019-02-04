package com.example.amia.schoolrent.Task.TaskImpl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Result;
import com.example.amia.schoolrent.Presenter.NetCallBack;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.NetUtils;

import org.litepal.LitePal;

import java.util.List;

public class IdleTaskImpl implements IdleTask {

    @Override
    public void getIndexClassify(final Context context,final Handler handler) {
        NetUtils.get(ActivityUtil.getString(context, R.string.host) + ActivityUtil.getString(context, R.string.classify_index), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json,Classify.class);
                    if(r.getResult()){
                        List<Classify> results = (List<Classify>) r.getData();
                        msg.what = INDEX_CLASSIFY;
                        msg.obj = results;
                        LitePal.deleteAll(Classify.class);
                        for(int i=0;i<results.size();i++){
                            Classify classify = results.get(i);
                            classify.save();
                        }
                    } else {
                        msg.what = ERROR;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = ERROR;
                }finally {
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void error(String... msg) {
                Message message = handler.obtainMessage();
                message.what = ERRORWITHMESSAGE;
                message.obj = msg;
            }
        });
    }

    @Override
    public List<Classify> getIndexClassifyFromCache() {
        List<Classify> classifies = LitePal.findAll(Classify.class);
        return classifies;
    }


    @Override
    public void getAllClassify(Context context,Handler handler) {

    }

    @Override
    public void getListInfo(List<IdleInfo> list, int index,Handler handler) {

    }
}
