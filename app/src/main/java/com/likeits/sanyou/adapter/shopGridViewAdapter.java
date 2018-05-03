package com.likeits.sanyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.ShopIndentEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/12/25.
 */

public class shopGridViewAdapter extends MyBaseAdapter<ShopIndentEntity.ShopBean> {
    public shopGridViewAdapter(Context context, List<ShopIndentEntity.ShopBean> shopBeen) {
        super(context, shopBeen);
    }


    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(
                    R.layout.shopgridviewadapter_items, parent, false);
            holder.ivAvatar = (ImageView) convertView
                    .findViewById(R.id.iv_avatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShopIndentEntity.ShopBean ShopBean = getItem(position);
        ImageLoader.getInstance().displayImage(ShopBean.getPath(), holder.ivAvatar);
        return convertView;
    }

    public class ViewHolder {
        private ImageView ivAvatar;
    }
}


