package com.likeits.sanyou.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.ShopIndentEntity;
import com.likeits.sanyou.ui.base.BaseViewHolder;
import com.likeits.sanyou.ui.base.KKBaseAdapter;
import com.likeits.sanyou.ui.shop.ShopIndentActivity;

/**
 * Created by Administrator on 2017/12/7.
 */

public class ShopIndentAdapter extends KKBaseAdapter<ShopIndentEntity> {
    @Override
    public BaseViewHolder<ShopIndentEntity> onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ordlist_listview_items, parent, false);
        ShopIndentViewHolder shopIndentViewHolder = new ShopIndentViewHolder(itemView);
        shopIndentViewHolder.setBaseActivity(baseActivity);
        return shopIndentViewHolder;
    }

    ShopIndentActivity baseActivity;

    public void setBaseActivity(ShopIndentActivity baseActivity) {
        this.baseActivity = baseActivity;
    }
}
