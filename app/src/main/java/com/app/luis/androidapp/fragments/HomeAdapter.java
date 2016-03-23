package com.app.luis.androidapp.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Luis Macias on 15/10/2015.
 */
public class HomeAdapter extends FragmentPagerAdapter {

    public HomeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (position == 0) ? "Tab 1" : "Tab2";
    }

    @Override
    public Fragment getItem(int position) {
        return (position == 0) ? new FragmentHomeNegocios() : new FragmentHomeProductos();
    }

    @Override
    public int getCount() {
        return 2;
    }


}
