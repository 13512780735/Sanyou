package com.likeits.sanyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.utils.UtilPreference;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class CategoryListAdapter extends MyBaseAdapter<CategoryInfoEntity> {
    public CategoryListAdapter(Context context, List<CategoryInfoEntity> categoryInfoEntity) {
        super(context, categoryInfoEntity);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(
                    R.layout.shop_recommned_listview_items, parent, false);
            holder.ivAvatar = (ImageView) convertView
                    .findViewById(R.id.iv_avatar);
            holder.tv_tvName = (TextView) convertView
                    .findViewById(R.id.tv_tvName);
            holder.tv_price = (TextView) convertView
                    .findViewById(R.id.tv_price);
            holder.tv_sales = (TextView) convertView
                    .findViewById(R.id.tv_sales);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CategoryInfoEntity categoryInfoEntity1 = getItem(position);
        ImageLoader.getInstance().displayImage(categoryInfoEntity1.getPath(), holder.ivAvatar);
        holder.tv_tvName.setText(categoryInfoEntity1.getGoods_name());
       // holder.tv_price.setText("¥ "+categoryInfoEntity1.getMoney_need());
        String role= UtilPreference.getStringValue(context,"role");
        if ("1".equals(role)) {
            holder.tv_price.setText("¥ "+categoryInfoEntity1.getMoney_need());
        }else {
            holder.tv_price.setText("¥ "+categoryInfoEntity1.getMoney_vip());
        }
        holder. tv_sales.setText("销量:"+categoryInfoEntity1.getSell_num());
        return convertView;
    }

    public class ViewHolder {
        private ImageView ivAvatar;
        private TextView tv_tvName, tv_price, tv_sales;
    }

}
