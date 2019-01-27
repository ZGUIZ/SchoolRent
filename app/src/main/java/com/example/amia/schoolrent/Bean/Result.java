package com.example.amia.schoolrent.Bean;

import com.example.amia.schoolrent.Util.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static Result getJSONObject(String json,Class className) throws JSONException, InstantiationException, IllegalAccessException {
        if(json== null || "".equals(json)){
            return null;
        }
        JSONObject object = new JSONObject(json);
        Result result = new Result();
        result.setResult(object.getBoolean("result"));

        if(result.getResult()) {
            Object data = JSONUtil.getObject(className, object.getJSONObject("data"));
            result.setData(data);
        } else {
            result.setMsg(object.getString("msg"));
        }
        return result;
    }

}
