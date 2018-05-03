package com.likeits.sanyou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.likeits.sanyou.fragment.MainFragment01;
import com.likeits.sanyou.fragment.MainFragment02;
import com.likeits.sanyou.fragment.MainFragment03;
import com.likeits.sanyou.fragment.MainFragment04;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/11.
 */

public class HomeViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> frList;

    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
        frList = new ArrayList<Fragment>();
        frList.add(new MainFragment01());
        frList.add(new MainFragment02());
        frList.add(new MainFragment03());
        frList.add(new MainFragment04());

    }

    @Override
    public Fragment getItem(int position) {
        // return frList.get(position);
        if (position == 0) {
            return frList.get(0);
        } else if (position == 1) {
            return frList.get(1);
        } else if (position == 2) {
            return frList.get(2);
        } else if (position == 3) {
            return frList.get(3);
        } else if (position == 4) {
            return frList.get(4);
        }
        return null;
    }

    @Override
    public int getCount() {
        return frList.size();
    }
}
