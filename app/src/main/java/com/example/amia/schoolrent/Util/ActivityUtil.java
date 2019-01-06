package com.example.amia.schoolrent.Util;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ActivityUtil {
    public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int frameId){
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(frameId,fragment);
        transaction.commit();
    }

    public static String getString(Context context,int id){
        return context.getResources().getString(id);
    }
}
