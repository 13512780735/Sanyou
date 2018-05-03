package com.likeits.sanyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.ShopIndentBean01;
import com.likeits.sanyou.utils.UtilPreference;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public class IndentDetailsAdapter extends MyBaseAdapter<ShopIndentBean01>{
    public IndentDetailsAdapter(Context context, List<ShopIndentBean01> shopIndentBean01s) {
        super(context, shopIndentBean01s);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(
                    R.layout.layout_indent_details_listview_items, parent, false);
            holder.iv_avatar = (ImageView) convertView
                    .findViewById(R.id.iv_avatar);
            holder.tv_name01 = (TextView) convertView
                    .findViewById(R.id.tv_name01);
            holder.tv_price = (TextView) convertView
                    .findViewById(R.id.tv_price);
            holder.tv_number = (TextView) convertView
                    .findViewById(R.id.tv_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShopIndentBean01 shopIndentBean01 = getItem(position);
        ImageLoader.getInstance().displayImage(shopIndentBean01.getPath(),holder.iv_avatar);
        holder.tv_name01.setText(shopIndentBean01.getGoods_name());
      //  holder.tv_price.setText("¥  "+shopIndentBean01.getMoney_need());
        String role= UtilPreference.getStringValue(context,"role");
        if ("1".equals(role)) {
            holder.tv_price.setText("¥ "+shopIndentBean01.getMoney_need());
        }else {
            holder.tv_price.setText("¥ "+shopIndentBean01.getMoney_vip());
        }
        holder.tv_number.setText("X "+shopIndentBean01.getGood_num());
        return convertView;
    }

    public class ViewHolder {
        private TextView tv_name01, tv_price, tv_number;
        ImageView iv_avatar;
    }
}
