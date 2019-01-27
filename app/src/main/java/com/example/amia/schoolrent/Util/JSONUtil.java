package com.example.amia.schoolrent.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.PortUnreachableException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
                        jsonObject.put(propertyName,o);
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

    public static Object getObject(Class className,String json) throws IllegalAccessException, InstantiationException, JSONException {
        if(className == null || json == null ||"".equals("")){
            return null;
        }
        JSONObject jsonObject = new JSONObject(json);
       return getObject(className,jsonObject);
    }

    public static Object getObject(Class className,JSONObject jsonObject) throws IllegalAccessException, InstantiationException, JSONException {
        Object object = className.newInstance();
        Field[] fileds = className.getDeclaredFields();

        for(int i=0;i<fileds.length;i++){
            Field field = fileds[i];
            String propertyName = field.getName();
            //转换为首字母大写
            field.setAccessible(true);
            if("serialVersionUID".equals(field.getName())){
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
                return sdf.parse(jsonObject.getString(propertyName));
            }
            return jsonObject.get(propertyName);
        } catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }
}
