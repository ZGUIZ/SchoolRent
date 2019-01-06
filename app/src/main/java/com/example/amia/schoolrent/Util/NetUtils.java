package com.example.amia.schoolrent.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.NetCallBack;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Amia on 2018/3/8.
 */

public class NetUtils {

    private static ExecutorService executorService = Executors.newFixedThreadPool(4);
    /**
     * 读取网页内容
     * @param uri
     * @return
     * @throws MalformedURLException
     */
    public static String requestDataFromNet(String uri) throws IOException {
        URL url=new URL(uri);
        InputStreamReader inReader=null;
        BufferedReader bufReader=null;
        HttpURLConnection conn=null;
        StringBuffer sb=new StringBuffer();
        try{
            conn=(HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setRequestMethod("GET");
            inReader=new InputStreamReader(conn.getInputStream());
            bufReader=new BufferedReader(inReader);
            String line;
            while((line=bufReader.readLine())!=null){
                sb.append(line);
            }
            return sb.toString();
        }
        catch (IOException e){
            e.printStackTrace();
            throw e;
        }
        finally {
            try {
                if (bufReader != null) {
                    bufReader.close();
                }
                if(inReader!=null){
                    inReader.close();
                }
                if(conn!=null){
                    conn.disconnect();
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /*public static <T> void doPost(final String url, final Map<String,Object> param, final Map<String,String> header){
        final Student student = null;
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                try {
                    String json=post(url,param,header);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(new Subscriber<String>(){

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String json) {
                student = JSONUtil.getObject(Student.class,json)
            }
        });
    }*/

    public static void doPost(final String url, final Map<String,Object> param, final Map<String,String> header, final NetCallBack callBack){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    String json=post(url,param,header);
                    callBack.finish(json);
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.error(e.getMessage());
                }
            }
        });
    }

    public static String post(String url, Map<String,Object> param,Map<String,String> header) throws IOException {
        StringBuffer sb =new StringBuffer();
        DataOutputStream outputStream = null;
        try {
            URL urlPath = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) urlPath.openConnection();
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setUseCaches(false);
            httpConnection.setConnectTimeout(10000);
            httpConnection.setRequestMethod("POST");
            //设置请求属性
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConnection.setRequestProperty("Charset", "UTF-8");
            //设置头部参数
            for (String key : header.keySet()) {
                httpConnection.setRequestProperty(key, header.get(key));
            }
            httpConnection.connect();

            outputStream = new DataOutputStream(httpConnection.getOutputStream());
            outputStream.writeBytes(urlencode(param));
            outputStream.flush();
            int resultCode = httpConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == resultCode) {
                String readLine = null;
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), "UTF-8"));
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine).append("\n");
                }
                responseReader.close();
            }
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        } finally {
            if(outputStream != null){
                outputStream.close();
            }
        }
        return sb.toString();
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String,Object>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                Object value = i.getValue();
                if(!(value instanceof String)){
                    JSONObject jsonObject = JSONUtil.getJSONObject(value);
                    value = jsonObject.toString();
                }
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(value+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 从网络上加载图片
     * @param url
     * @return
     */
    public static Bitmap getURLImage(String url) throws IOException {
        Bitmap bitmap=null;
        //Log.i("NetUtils","ImageUrl="+url);
        URL conn_url=new URL(url);
        HttpURLConnection connection=(HttpURLConnection)conn_url.openConnection();
        connection.setConnectTimeout(40000);
        connection.setDoInput(true);
        connection.setUseCaches(true);
        connection.connect();
        InputStream is=connection.getInputStream();
        bitmap= BitmapFactory.decodeStream(is);
        return bitmap;
    }
}
