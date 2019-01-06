package com.example.amia.schoolrent.Bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Province {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Province getProvince(JSONObject object) throws JSONException {
        Province province = new Province();
        province.setId(object.getString("id"));
        province.setName(object.getString("text"));
        return province;
    }

    public static List<Province> getProvinces(JSONArray jsonArray) throws JSONException {
        List<Province> provinces = new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++){
            JSONObject object = (JSONObject) jsonArray.get(i);
            Province province=getProvince(object);
            provinces.add(province);
        }
        return provinces;
    }
}
