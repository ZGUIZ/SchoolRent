package com.example.amia.schoolrent.Fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyRentFragmentAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<Fragment> fragmentList;
    private List<String> titleList;

    public MyRentFragmentAdapter(FragmentManager fm,Context context,List<Fragment> fragmentList,List<String> title) {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
        this.titleList = title;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
