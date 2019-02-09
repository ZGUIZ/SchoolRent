package com.example.amia.schoolrent.Util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesUtil {
    private Context context;

    public SharedPreferencesUtil(Context context) {
        this.context = context;
    }

    public void write(String fileName, Map<String,String> values){
        if(fileName==null||fileName.isEmpty()||values.isEmpty()){
            return;
        }
        SharedPreferences sp=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        Set<String> keys=values.keySet();
        for(String key:keys){
            editor.putString(key,values.get(key));
        }
        editor.commit();
    }

    public HashMap<String,String> read(String fileName,String[] keys){
        HashMap<String,String> values=new HashMap<>();
        SharedPreferences sp=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        for(String key:keys){
            values.put(key,sp.getString(key,null));
        }
        return values;
    }
}
