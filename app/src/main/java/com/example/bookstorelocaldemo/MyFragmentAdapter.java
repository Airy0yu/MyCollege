package com.example.bookstorelocaldemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;
    private List<Fragment> list;

    public MyFragmentAdapter(FragmentManager manager, List<Fragment> list){
        super(manager);

        this.fragmentManager=manager;
        this.list=list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
