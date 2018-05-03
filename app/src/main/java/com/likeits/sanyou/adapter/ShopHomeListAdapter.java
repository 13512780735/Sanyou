package com.likeits.sanyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.ShopHomeInfoEntity;
import com.likeits.sanyou.utils.UtilPreference;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class ShopHomeListAdapter extends MyBaseAdapter<ShopHomeInfoEntity> {
    public ShopHomeListAdapter(Context context, List<ShopHomeInfoEntity> shopHomeInfoEntities) {
        super(context, shopHomeInfoEntities);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(
                    R.layout.shop_recommned_listview_items, parent, false);
            holder.news_title = (TextView) convertView
                    .findViewById(R.id.tv_tvName);
            holder.tv_price = (TextView) convertView
                    .findViewById(R.id.tv_price);
            holder.tv_sales = (TextView) convertView
                    .findViewById(R.id.tv_sales);
            holder.news_logo = (ImageView) convertView
                    .findViewById(R.id.iv_avatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShopHomeInfoEntity shopHomeInfoEntity = getItem(position);
        holder.news_title.setText(shopHomeInfoEntity.getGoods_name());

        String role= UtilPreference.getStringValue(context,"role");
        if ("1".equals(role)) {
            holder.tv_price.setText("¥ "+shopHomeInfoEntity.getMoney_need());
        }else {
            holder.tv_price.setText("¥ "+shopHomeInfoEntity.getMoney_vip());
        }
        holder.tv_sales.setText("销量:"+shopHomeInfoEntity.getSell_num());
       ImageLoader.getInstance().displayImage(shopHomeInfoEntity.getPath(),holder.news_logo);
        return convertView;
    }

    public class ViewHolder {
        private ImageView news_logo;
        private TextView news_title, tv_price, tv_sales;
    }
}
