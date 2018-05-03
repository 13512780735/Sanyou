package com.likeits.sanyou.ui.chat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.likeits.sanyou.ui.chat.fragment.ContactFragment;
import com.likeits.sanyou.ui.chat.fragment.MessageFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/30.
 */

public class ChatViewPageAdatpter extends FragmentPagerAdapter {
    private ArrayList<Fragment> frList;

    public ChatViewPageAdatpter(FragmentManager fm) {
        super(fm);
        frList = new ArrayList<Fragment>();
        frList.add(new MessageFragment());
        frList.add(new ContactFragment());

    }

    @Override
    public Fragment getItem(int position) {
        // return frList.get(position);
        if (position == 0) {
            return frList.get(0);
        } else if (position == 1) {
            return frList.get(1);
        }
        return null;
    }

    @Override
    public int getCount() {
        return frList.size();
    }
}
