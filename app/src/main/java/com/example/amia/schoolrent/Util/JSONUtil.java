package com.example.amia.schoolrent.Util;

import com.example.amia.schoolrent.Bean.AuthPicture;
import com.example.amia.schoolrent.Bean.IdelPic;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.School;
import com.example.amia.schoolrent.Bean.SecondResponseInfo;
import com.example.amia.schoolrent.Bean.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.PortUnreachableException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JSONUtil {
    /**
     * 反射生成JSONObject对象
     * @param t
     * @param <T>
     * @return
     */
    public static <T> JSONObject getJSONObject(T t){
        JSONObject jsonObject = new JSONObject();
        if(t!=null){
            Class<?> clz = t.getClass();
            Field[] fileds = clz.getDeclaredFields();
            for(Field field:fileds){
                String propertyName = field.getName();
                //转换为首字母大写
                char firstChar = propertyName.charAt(0);
                String upperName = Character.isUpperCase(firstChar) ?
                        propertyName:Character.toUpperCase(firstChar)+propertyName.substring(1);

                try{
                    Method method = t.getClass().getMethod("get"+upperName);
                    Object o = method.invoke(t);
                    if(o!=null){
                        if(o instanceof List){
                            jsonObject.put(propertyName,getJsonArray(o));
                        }else if(o instanceof Date){
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String str = sdf.format(o);
                            jsonObject.put(propertyName,str);
                        } else {
                            jsonObject.put(propertyName, o);
                        }
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonObject;
    }

    public static JSONArray getJsonArray(Object o){
        JSONArray jsonArray = new JSONArray();
        if(o instanceof List){
            List<Object> objects = (List<Object>) o;
            for(Object object : objects){
                JSONObject result = getJSONObject(object);
                jsonArray.put(result);
            }
        }
        return jsonArray;
    }

    public static Object getObject(Class className,String json) throws IllegalAccessException, InstantiationException, JSONException {
        if(className == null || json == null ||"".equals("")){
            return null;
        }
        JSONObject jsonObject = new JSONObject(json);
       return getObject(className,jsonObject);
    }

    public static Object getObject(Class className,JSONObject jsonObject) throws IllegalAccessException, InstantiationException, JSONException {
        if(jsonObject == null){
            return null;
        }
        Object object = className.newInstance();
        Field[] fileds = className.getDeclaredFields();

        for(int i=0;i<fileds.length;i++){
            Field field = fileds[i];
            String propertyName = field.getName();
            //转换为首字母大写
            field.setAccessible(true);
            if("serialVersionUID".equals(field.getName())||"$change".equals(propertyName)){
                continue;
            }else if(field.getType().equals(List.class)){
                try {
                    Type genericType = field.getGenericType();
                    if (genericType == null) {
                        continue;
                    }
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;
                        Class<?> genericClass = (Class<?>) pt.getActualTypeArguments()[0];
                        JSONArray jsonArray = jsonObject.getJSONArray(propertyName);
                        if (genericClass.equals(IdelPic.class)) {
                            List<IdelPic> list = new ArrayList<>();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                IdelPic o = (IdelPic) getObject(IdelPic.class, jsonArray.getJSONObject(j));
                                list.add(o);
                            }
                            field.set(object, list);
                        } else if (genericClass.equals(SecondResponseInfo.class)) {
                            List<SecondResponseInfo> list = new ArrayList<>();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                SecondResponseInfo o = (SecondResponseInfo) getObject(SecondResponseInfo.class, jsonArray.getJSONObject(j));
                                list.add(o);
                            }
                            field.set(object, list);
                        } else if (genericClass.equals(AuthPicture.class)) {
                            List<AuthPicture> authPictures = new ArrayList<>();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                AuthPicture authPicture = (AuthPicture) getObject(AuthPicture.class, jsonArray.getJSONObject(j));
                                authPictures.add(authPicture);
                            }
                            field.set(object, authPictures);
                        } else if(genericClass.equals(Rent.class)){
                            List<Rent> rentList = new ArrayList<>();
                            for(int j = 0;j<jsonArray.length();j++){
                                Rent rent = (Rent)getObject(Rent.class,jsonArray.getJSONObject(j));
                                rentList.add(rent);
                            }
                            field.set(object,rentList);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                continue;
            } else if(field.getType().equals(Student.class)){
                try {
                    Object o = getObject(Student.class, jsonObject.getJSONObject(propertyName));
                    field.set(object, o);
                } catch (Exception e){
                    e.printStackTrace();
                }
                continue;
            } else if(field.getType().equals(School.class)){
                Object o = null;
                try {
                    JSONObject jo = jsonObject.getJSONObject(propertyName);
                    o = getObject(School.class, jo);
                } catch (Exception e){
                    e.printStackTrace();
                }
                field.set(object,o);
                continue;
            } else if(field.getType().equals(IdleInfo.class)){
                try{
                    Object o = getObject(IdleInfo.class,jsonObject.getJSONObject(propertyName));
                    field.set(object,o);
                } catch (Exception e){
                    e.printStackTrace();
                }
                continue;
            }
            field.set(object,getFieldVal(field.getType(),jsonObject,propertyName));
        }
        return object;
    }

    private static Object getFieldVal(Class cls,JSONObject jsonObject,String propertyName){
        if(cls == null){
            return null;
        }
        try{
            if(cls.equals(Date.class)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String d = sdf.format(jsonObject.getDouble(propertyName));
                Date date=sdf.parse(d);
                return date;
            } else if(cls.equals(IdleInfo.class)){
                return getObject(IdleInfo.class,jsonObject);
            } else if(cls.equals(Float.class)){
                return Float.parseFloat(jsonObject.getString(propertyName));
            }
            return jsonObject.get(propertyName);
        } catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }
}
