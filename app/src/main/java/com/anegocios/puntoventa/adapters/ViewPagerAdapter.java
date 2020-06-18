package com.anegocios.puntoventa.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> list = new ArrayList<>();


    public ViewPagerAdapter(FragmentManager man) {
        super(man);
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void addFragment(Fragment fragment) {
        list.add(fragment);
    }
}
