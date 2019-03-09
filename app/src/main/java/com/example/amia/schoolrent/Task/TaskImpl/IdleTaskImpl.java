package com.example.amia.schoolrent.Task.TaskImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.widget.Toast;

import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.IdleInfoExtend;
import com.example.amia.schoolrent.Bean.KeyValue;
import com.example.amia.schoolrent.Bean.MapKeyValue;
import com.example.amia.schoolrent.Bean.NetBitmap;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.RentExtend;
import com.example.amia.schoolrent.Bean.Result;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.NetCallBack;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.COSUtil;
import com.example.amia.schoolrent.Util.NetImageCallback;
import com.example.amia.schoolrent.Util.NetUtils;
import com.example.amia.schoolrent.Util.RSAUtil;

import org.litepal.LitePal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdleTaskImpl implements IdleTask {

    private Date updateTime;

    @Override
    public void getIndexClassify(final Context context,IdleInfoExtend idleInfoExtend,final Handler handler) {
        updateTime = new Date();
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
                            //加载图片信息
                            loadIcon(classify.getClassifyId(),classify.getImageUrl(),handler);
                            //保存到数据库
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

        getListInfo(context,idleInfoExtend,handler);
    }

    public void loadIcon(final String id,String url, final Handler handler){
        //下载图片
        if(url != null  && !"".equals(url)) {
            NetUtils.getImage(url, new NetImageCallback() {
                @Override
                public void finish(Bitmap bitmap) {
                    Message message = handler.obtainMessage();
                    message.what = CLASSIFY_ICON;

                    //封装对象
                    MapKeyValue<Bitmap> keyValue = new MapKeyValue<>();
                    keyValue.setId(id);
                    keyValue.setData(bitmap);

                    message.obj = keyValue;
                    handler.sendMessage(message);
                }

                @Override
                public void error() {
                }
            });
        }
    }

    @Override
    public List<Classify> getIndexClassifyFromCache() {
        List<Classify> classifies = LitePal.findAll(Classify.class);
        return classifies;
    }


    @Override
    public void getAllClassify(Context context,final Handler handler) {
        NetUtils.get(ActivityUtil.getString(context, R.string.host) + ActivityUtil.getString(context, R.string.classify_all), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json,Classify.class);
                    if(r.getResult()){
                        List<Classify> results = (List<Classify>) r.getData();
                        msg.what = CLASSIFY_ALL;
                        msg.obj = results;
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
    public String uploadImage(Context context,Student student, String srcPath, Handler handler) {
        Date now = new Date();
        COSUtil cosUtil = new COSUtil(context);
        String id = "/"+student.getUserId() +"/"+now.getTime();
        String cosUrl = ActivityUtil.getString(context,R.string.idle_pic_package) + id;
        cosUtil.uploadFile(cosUrl,srcPath,handler, id);
        return id;
    }

    @Override
    public void stopUpload(String id) {
        COSUtil.stop(id);
    }

    @Override
    public void getListInfo(Context context, final IdleInfoExtend idleInfo, final Handler handler) {
        String search = idleInfo.getSearch();
        try {
            if (search != null) {
                idleInfo.setSearch(URLEncoder.encode(search, "utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        idleInfo.setCreateDate(updateTime);

        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.get_idle_page);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("idleInfo",idleInfo);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getObjectWithList(json,IdleInfo.class);
                    if(result.getResult()) {
                        if(idleInfo.getPage() == 1) {
                            msg.what = IDLE_SUCESS;
                        } else {
                            msg.what = LOAD_MORE_SUCCESS;
                        }
                        msg.obj = result.getData();
                    } else {
                        msg.what = IDLE_ERROR;
                        msg.obj = result.getMsg();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = IDLE_ERROR;
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
    public void pushIdle(Context context, IdleInfo idleInfo, final Handler handler) {

        try {
            idleInfo.setTitle(URLEncoder.encode(idleInfo.getTitle(),"utf-8"));
            idleInfo.setIdelInfo(URLEncoder.encode(idleInfo.getIdelInfo(),"utf-8"));
            if(idleInfo.getAddress()!=null){
                idleInfo.setAddress(URLEncoder.encode(idleInfo.getAddress(),"utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.add_idle);

        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("idleInfo",idleInfo);
        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()) {
                        msg.what = PUSH_SUCCESS;
                        msg.obj = result.getData();
                    } else {
                        msg.what = PUSH_ERROR;
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
    public void loadImage(final String id, String url, final Handler handler) {
        NetUtils.getImage(url, new NetImageCallback() {
            @Override
            public void finish(Bitmap bitmap) {
                Message message = handler.obtainMessage();
                message.what = PIC_LOAD_SUCCESS;
                NetBitmap netBitmap = new NetBitmap();
                netBitmap.setId(id);
                netBitmap.setBitmap(bitmap);
                message.obj = netBitmap;
                handler.sendMessage(message);
            }

            @Override
            public void error() {
            }
        });
    }

    @Override
    public void getRelation(Context context, IdleInfo idleInfo, final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host) + ActivityUtil.getString(context,R.string.get_relation) + idleInfo.getInfoId();
        NetUtils.get(url, new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json, Rent.class);
                    if(result.getResult()) {
                        msg.what = LOAD_RELATION_SUCCESS;
                        msg.obj = result.getData();
                    } else {
                        msg.what = LOAD_RELATION_ERROR;
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
    public void addRent(Context context, Rent rent, final Handler handler) {
        List<KeyValue> keyValues = LitePal.where("key = ?","publicKey").find(KeyValue.class);
        if(keyValues.size()>0) {
            KeyValue keyValue = keyValues.get(0);
            byte[] bytes = Base64.decode(keyValue.getValue(), Base64.DEFAULT);
            //还原公钥
            //RSAUtil.restorePublicKey(bytes);
            PublicKey key = RSAUtil.restorePublicKey(bytes);
            byte[] encodePassword = null;
            try {
                encodePassword = RSAUtil.RSAEncode(key, rent.getPayPassword().getBytes("utf-8"));
                rent.setPayPassword(Base64.encodeToString(encodePassword, Base64.DEFAULT));
            } catch (Exception e) {
                Toast.makeText(context,R.string.link_error,Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String url = ActivityUtil.getString(context,R.string.host) + ActivityUtil.getString(context,R.string.add_rent);

        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("rent",rent);
        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()) {
                        msg.what = RENT_SUCCESS;
                        msg.obj = result.getData();
                    } else {
                        msg.what = RENT_ERROR;
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
    public void getIdleRentList(Context context, IdleInfo idleInfo, final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host) + ActivityUtil.getString(context,R.string.idle_rent_list)+idleInfo.getInfoId();
        NetUtils.get(url, new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getObjectWithList(json,Rent.class);
                    if(result.getResult()) {
                        msg.what = IDLE_RENT_LIST_SUCCESS;
                        msg.obj = result.getData();
                    } else {
                        msg.what = IDLE_RENT_LIST_ERROR;
                        msg.obj = result.getMsg();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = IDLE_RENT_LIST_ERROR;
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
    public void agree(Context context, Rent rent,final Handler handler) {
        Rent param = new Rent();
        param.setRentId(rent.getRentId());
        param.setIdelId(rent.getIdelId());
        String url = ActivityUtil.getString(context,R.string.host) + ActivityUtil.getString(context,R.string.rent_agree);

        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("rent",param);
        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()) {
                        msg.what = RENT_AGREE_SUCCESS;
                    } else {
                        msg.what = RENT_AGREE_ERROR;
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
    public void getMyPushList(Context context,IdleInfoExtend idleInfoExtend, final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.get_my_push);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("idleInfo",idleInfoExtend);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack()  {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json,IdleInfo.class);
                    if(r.getResult()){
                        List<Classify> results = (List<Classify>) r.getData();
                        msg.what = MY_IDLE_SUCCESS;
                        msg.obj = results;
                    } else {
                        msg.what = MY_IDLE_ERROR;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = MY_IDLE_ERROR;
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
    public void closeIdle(Context context, IdleInfo idleInfo, final Handler handler) {
        IdleInfo param = new IdleInfo();
        param.setInfoId(idleInfo.getInfoId());
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.idle_close);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("idleInfo",param);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack()  {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json,null);
                    if(r.getResult()){
                        msg.what = CLOSE_IDLE_SUCCESS;
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
    public void cancelRent(Context context, IdleInfo idleInfo, final Handler handler) {
        String url  = ActivityUtil.getString(context,R.string.host) + ActivityUtil.getString(context,R.string.rent_finish);
        IdleInfo param = new IdleInfo();
        param.setInfoId(idleInfo.getInfoId());
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("idleInfo",param);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack()  {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json,null);
                    if(r.getResult()){
                        msg.what = CANCLE_SUCCESS;
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
    public void updateIdleInfo(Context context, IdleInfo idleInfo, final Handler handler) {
        idleInfo.setStudent(null);
        try {
            idleInfo.setTitle(URLEncoder.encode(idleInfo.getTitle(),"utf-8"));
            idleInfo.setIdelInfo(URLEncoder.encode(idleInfo.getIdelInfo(),"utf-8"));
            if(idleInfo.getAddress()!=null){
                idleInfo.setAddress(URLEncoder.encode(idleInfo.getAddress(),"utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.update_idle);

        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("idleInfo",idleInfo);
        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,null);
                    if(result.getResult()) {
                        msg.what = UPDATE_IDLE_SUCCESS;
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
    public void delIdleInfo(Context context, IdleInfo idleInfo, final Handler handler) {
        IdleInfo param = new IdleInfo();
        param.setInfoId(idleInfo.getInfoId());
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.del_idle);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("idleInfo",param);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack()  {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json,null);
                    if(r.getResult()){
                        msg.what = DEL_SUCCESS;
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
    public void getClassifyName(Context context, Classify classify, final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host) + ActivityUtil.getString(context,R.string.get_classify_name)+classify.getClassifyId();
        NetUtils.get(url, new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,Classify.class);
                    if(result.getResult()) {
                        msg.what = CLASS_NAME_SUCCESS;
                        msg.obj = result.getData();
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
    public void loadMineRent(Context context, RentExtend rentExtend, final Handler handler, final int flag) {
        String url  = ActivityUtil.getString(context,R.string.host) + ActivityUtil.getString(context,R.string.query_mine_rent);

        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("rent",rentExtend);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack()  {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json,Rent.class);
                    if(r.getResult()){
                        msg.what = flag;
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
    public void cancelRent(Context context, Rent rent, final Handler handler) {
        Rent r = new Rent();
        r.setRentId(rent.getRentId());
        r.setIdelId(rent.getIdelId());
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.cancel_rent);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("rent",r);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack()  {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json,null);
                    if(r.getResult()){
                        msg.what = CANCEL_RENT;
                    } else {
                        msg.what = ERROR;
                        msg.obj = r.getMsg();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = ERROR;
                    msg.obj = e.getMessage();
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
    public void findIdleById(Context context, String id, final Handler handler) {
        String url = ActivityUtil.getString(context,R.string.host) + ActivityUtil.getString(context,R.string.find_idle)+id;
        NetUtils.get(url, new NetCallBack() {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result result = Result.getJSONObject(json,IdleInfo.class);
                    if(result.getResult()) {
                        msg.what = FIND_BY_ID;
                        msg.obj = result.getData();
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
    public void startRent(Context context, Rent rent, final Handler handler) {
        Rent r = new Rent();
        r.setRentId(rent.getRentId());
        r.setIdelId(rent.getIdelId());
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.start_rent);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("rent",r);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack()  {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json,null);
                    if(r.getResult()){
                        msg.what = START_RENT;
                    } else {
                        msg.what = ERROR;
                        msg.obj = r.getMsg();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = ERROR;
                    msg.obj = e.getMessage();
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
    public void delRent(Context context, Rent rent, final Handler handler) {
        Rent r = new Rent();
        r.setRentId(rent.getRentId());
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.del_rent);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("rent",r);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack()  {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json,null);
                    if(r.getResult()){
                        msg.what = DEL_RENT;
                    } else {
                        msg.what = ERROR;
                        msg.obj = r.getMsg();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = ERROR;
                    msg.obj = e.getMessage();
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
    public void disagreeRent(Context context, Rent rent, final Handler handler) {
        Rent r = new Rent();
        r.setRentId(rent.getRentId());
        String url = ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.disagree_rent);
        Map<String,Object> keyValueMap = new HashMap<>();
        keyValueMap.put("rent",r);

        NetUtils.doPost(url, keyValueMap, new HashMap<String, String>(), new NetCallBack()  {
            @Override
            public void finish(String json) {
                Message msg = handler.obtainMessage();
                try {
                    Result r = Result.getObjectWithList(json,null);
                    if(r.getResult()){
                        msg.what = DIS_RENT;
                    } else {
                        msg.what = ERROR;
                        msg.obj = r.getMsg();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = ERROR;
                    msg.obj = e.getMessage();
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
