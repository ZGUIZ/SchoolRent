package com.example.amia.schoolrent.Task.TaskImpl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.RentNeedsExtend;
import com.example.amia.schoolrent.Bean.Result;
import com.example.amia.schoolrent.Presenter.NetCallBack;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.RentNeedsTask;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.NetUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.example.amia.schoolrent.Task.IdleTask.ERROR;
import static com.example.amia.schoolrent.Task.SchoolTask.ERRORWITHMESSAGE;

public class RentNeedsTaskImpl implements RentNeedsTask {
    @Override
    public void pushArticle(Context context, RentNeeds needs, final Handler handler) {
        try {
            needs.setTitle(URLEncoder.encode(needs.getTitle(),"utf-8"));
            needs.setIdelInfo(URLEncoder.encode(needs.getIdelInfo(),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url  = ActivityUtil.getString(context, R.string.host) + ActivityUtil.getString(context,R.string.add_article);

        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("rentNeeds",needs);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack()  {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getJSONObject(json, null);
                    if(r.getResult()){
                        msg.what = ADD_ARTICLE;
                    } else {
                        msg.what = ERROR;
                        msg.obj = r.getMsg();
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
    public void delArticle(Context context, RentNeeds needs, final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host) + ActivityUtil.getString(context,R.string.del_needs)+needs.getInfoId();
        NetUtils.get(url, new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json, null);
                    if(result.getResult()) {
                        msg.what = DEL_ARTICLE;
                    } else {
                        msg.what = ERROR;
                        msg.obj = result.getMsg();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = ERROR;
                } finally {
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
    public void queryArticle(Context context, RentNeedsExtend needs, final Handler handler) {
        String url  = ActivityUtil.getString(context, R.string.host) + ActivityUtil.getString(context,R.string.needs_list);

        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("rentNeeds",needs);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack()  {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json, RentNeeds.class);
                    if(r.getResult()){
                        msg.what = ARTICLE_LIST;
                        msg.obj = r.getData();
                    } else {
                        msg.what = ERROR;
                        msg.obj = r.getMsg();
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
    public void queryMyArticle(Context context, RentNeedsExtend needs, final Handler handler) {
        String url  = ActivityUtil.getString(context, R.string.host) + ActivityUtil.getString(context,R.string.my_needs);

        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("rentNeeds",needs);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack()  {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json, RentNeeds.class);
                    if(r.getResult()){
                        msg.what = ARTICLE_LIST;
                        msg.obj = r.getData();
                    } else {
                        msg.what = ERROR;
                        msg.obj = r.getMsg();
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
}
