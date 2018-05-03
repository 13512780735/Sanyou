package com.likeits.sanyou.message.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.likeits.sanyou.message.message_chat_list.ConversationFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/17.
 */

public class Message_Chat_ViewPagerAdatper extends FragmentPagerAdapter {
    private ArrayList<Fragment> frList;

    public Message_Chat_ViewPagerAdatper(FragmentManager fm) {
        super(fm);
        frList = new ArrayList<Fragment>();
        frList.add(new ConversationFragment());
        //frList.add(new MessageFragment());


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
