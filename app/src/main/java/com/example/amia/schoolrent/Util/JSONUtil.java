package com.example.amia.schoolrent.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

        Object object = className.newInstance();
        Field[] fileds = className.getDeclaredFields();
        JSONObject jsonObject = new JSONObject(json);
        for(int i=0;i<fileds.length;i++){
            Field field = fileds[i];
            String propertyName = field.getName();
            //转换为首字母大写
            field.setAccessible(true);
            field.set(object,jsonObject.get(propertyName));
        }
        return object;
    }
}
