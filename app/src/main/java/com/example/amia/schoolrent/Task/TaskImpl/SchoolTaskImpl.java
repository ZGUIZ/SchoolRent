package com.example.amia.schoolrent.Task.TaskImpl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.amia.schoolrent.Bean.Province;
import com.example.amia.schoolrent.Bean.School;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.SchoolTask;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.NetUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.amia.schoolrent.Presenter.PersenterImpl.LoginContractImpl.CITY;
import static com.example.amia.schoolrent.Presenter.PersenterImpl.LoginContractImpl.ERROR;
import static com.example.amia.schoolrent.Presenter.PersenterImpl.LoginContractImpl.PROVINCE;
import static com.example.amia.schoolrent.Presenter.PersenterImpl.LoginContractImpl.SCHOOL;

public class SchoolTaskImpl implements SchoolTask {

    private static ExecutorService service = Executors.newSingleThreadExecutor();

    @Override
    public void getProvince(final Context context, final Handler handler) {
        service.submit(new Runnable() {
            @Override
            public void run(){
                Message msg = handler.obtainMessage();
                try {
                    String result=NetUtils.requestDataFromNet(ActivityUtil.getString(context,R.string.host)+ActivityUtil.getString(context,R.string.getProvince));
                    msg.what = PROVINCE;
                    msg.obj = Province.getProvinces(new JSONArray(result));
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = ERROR;
                }finally {
                    handler.sendMessage(msg);
                }
            }
        });
    }

    @Override
    public void getCity(final Context context, final Province province,final Handler handler){
        service.submit(new Runnable() {
            @Override
            public void run(){
                Message msg = handler.obtainMessage();
                try {
                    String pro = URLEncoder.encode(province.getName(), "UTF-8");
                    String result = NetUtils.requestDataFromNet(ActivityUtil.getString(context, R.string.host) + ActivityUtil.getString(context, R.string.getCity) + pro);
                    List<Province> provinces = Province.getProvinces(new JSONArray(result));
                    msg.what = CITY;
                    msg.obj = provinces;
                } catch (Exception e){
                    e.printStackTrace();
                    msg.what = ERROR;
                }finally {
                    handler.sendMessage(msg);
                }
            }
        });
    }

    @Override
    public void getSchool(final Context context, final Province province, final Handler handler){
        service.submit(new Runnable() {
            @Override
            public void run() {
                Message msg=handler.obtainMessage();
                try {
                    String pro = URLEncoder.encode(province.getName(), "UTF-8");
                    String result = NetUtils.requestDataFromNet(ActivityUtil.getString(context, R.string.host) + ActivityUtil.getString(context, R.string.getSchool) + pro);
                    List<School> schools = School.getSchool(new JSONArray(result));
                    msg.what = SCHOOL;
                    msg.obj = schools;
                } catch (Exception e){
                    e.printStackTrace();
                    msg.what = ERROR;
                } finally {
                    handler.sendMessage(msg);
                }
            }
        });
    }
}
