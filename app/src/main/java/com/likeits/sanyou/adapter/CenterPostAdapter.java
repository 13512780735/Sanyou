package com.likeits.sanyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.CenterPostImagBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2018/1/3.
 */

public class CenterPostAdapter extends MyBaseAdapter<CenterPostImagBean> {
    public CenterPostAdapter(Context context, List<CenterPostImagBean> centerPostImagBeen) {
        super(context, centerPostImagBeen);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(
                    R.layout.layout_post_image_items, parent, false);
            holder.iv_image = (ImageView) convertView
                    .findViewById(R.id.iv_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CenterPostImagBean centerPostImagBean = getItem(position);
        ImageLoader.getInstance().displayImage(centerPostImagBean.getPath(), holder.iv_image);
        return convertView;
    }


    public class ViewHolder {
        ImageView iv_image;
    }
}
