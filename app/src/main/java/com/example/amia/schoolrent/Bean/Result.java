package com.example.amia.schoolrent.Bean;

import android.support.annotation.Nullable;

import com.example.amia.schoolrent.Util.JSONUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Result {
    /**
     * 是否成功
     */
    private Boolean result;
    /**
     * 返回信息
     */
    private String msg;

    private String paramType;
    /**
     * 参数
     */
    private Object data;

    private Integer code;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object param) {
        this.data = param;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static Result getJSONObject(String json, @Nullable Class className) throws JSONException, InstantiationException, IllegalAccessException {
        if(json== null || "".equals(json)){
            return null;
        }
        JSONObject object = new JSONObject(json);
        Result result = new Result();
        result.setResult(object.getBoolean("result"));

        if(result.getResult() && className != null) {
            Object data = JSONUtil.getObject(className, object.getJSONObject("data"));
            result.setData(data);
        } else {
            result.setMsg(object.getString("msg"));
        }
        return result;
    }
    public static Result getObjectWithList(String json,Class className) throws JSONException, InstantiationException, IllegalAccessException {
        if(json== null || "".equals(json)){
            return null;
        }
        JSONObject object = new JSONObject(json);
        Result result = new Result();
        result.setResult(object.getBoolean("result"));
        List<Object> objects = new ArrayList<>();

        if(result.getResult() && className != null) {
            JSONArray jsonArray = object.getJSONArray("data");
            if(jsonArray!=null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Object data = JSONUtil.getObject(className, jsonObject);
                    objects.add(data);
                }
                result.setData(objects);
            } else {
                result.setData(null);
            }
        } else {
            result.setMsg(object.getString("msg"));
        }
        return result;
    }
}
