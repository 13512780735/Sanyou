package com.likeits.sanyou.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.likeits.sanyou.R;
import com.likeits.sanyou.ui.base.BaseViewHolder;
import com.likeits.sanyou.ui.base.KKBaseAdapter;
import com.likeits.sanyou.ui.chat.NearFriendActivity;

/**
 * Created by Administrator on 2017/12/4.
 */

public class NearFriendAdapter extends KKBaseAdapter<NearFirendInfoEntity> {

    @Override
    public BaseViewHolder<NearFirendInfoEntity> onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_near_friend_listview_items, parent, false);
        NearFriendViewHolder nearFriendViewHolder = new NearFriendViewHolder(itemView);
        nearFriendViewHolder.setBaseActivity(baseActivity);
        return nearFriendViewHolder;
    }

    NearFriendActivity baseActivity;

    public void setBaseActivity(NearFriendActivity baseActivity) {
        this.baseActivity = baseActivity;
    }
}
