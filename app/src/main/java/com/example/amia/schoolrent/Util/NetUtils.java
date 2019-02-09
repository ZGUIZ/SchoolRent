package com.example.amia.schoolrent.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.amia.schoolrent.Presenter.NetCallBack;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by Amia on 2018/3/8.
 */

public class NetUtils {

    private static ExecutorService executorService = Executors.newFixedThreadPool(4);
    private static List<HttpURLConnection> connections = new ArrayList<>();

    private static String sessionId;
    /**
     * get方法发送请求
     * @param url
     * @param callBack
     */
    public static void get(final String url, final NetCallBack callBack){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    String json=requestDataFromNet(url);
                    callBack.finish(json);
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.error(e.getMessage());
                }
            }
        });
    }
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
        int size = connections.size();  //记录当前连接的数量
        try{
            conn=(HttpURLConnection) url.openConnection();

            if(sessionId != null){
                conn.setRequestProperty("cookie",sessionId);
            }

            connections.add(conn);
           /* //设置session
            String cookieval  = conn.getHeaderField("set-cookie");
            if(cookieval!=null){
                sessionId = cookieval.substring(0,cookieval.indexOf(";"));
            }*/

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
            connections.remove(size);
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
        int size=connections.size();
        try {
            URL urlPath = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) urlPath.openConnection();
            connections.add(httpConnection);

            if(sessionId != null){
                httpConnection.setRequestProperty("cookie",sessionId);
            }

            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setUseCaches(false);
            httpConnection.setConnectTimeout(10000);
            httpConnection.setRequestMethod("POST");
            //设置请求属性
            httpConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
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
                //获取session
                String cookieval  = httpConnection.getHeaderField("Set-Cookie");
                if(cookieval!=null){
                    sessionId = cookieval.substring(0,cookieval.indexOf(";"));
                }

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
            connections.remove(size);
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
            Object value = i.getValue();
            if(!(value instanceof String)){
                JSONObject jsonObject = JSONUtil.getJSONObject(value);
                value = jsonObject.toString();
            }
            //sb.append(i.getKey()).append("=").append(URLEncoder.encode(value+"","UTF-8")).append("&");
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 获取网络图片
     * @param url
     * @param callback
     */
    public static void getImage(final String url, final NetImageCallback callback){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = getURLImage(url);
                    callback.finish(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.error();
                }
            }
        });
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

    /**
     * 中断连接
     */
    public static void disConnection(){
        if(connections.size()>0){
            HttpURLConnection httpURLConnection = connections.get(connections.size()-1);
            httpURLConnection.disconnect();
        }
    }

    public static String getSessionId() {
        return sessionId;
    }

    public static void setSessionId(String sessionId) {
        NetUtils.sessionId = sessionId;
    }
}
