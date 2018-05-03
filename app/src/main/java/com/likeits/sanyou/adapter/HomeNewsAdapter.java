package com.likeits.sanyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.HomeNewInfoEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class HomeNewsAdapter extends MyBaseAdapter<HomeNewInfoEntity> {
    public HomeNewsAdapter(Context context, List<HomeNewInfoEntity> homeNewInfoEntities) {
        super(context, homeNewInfoEntities);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(
                    R.layout.layout_home_news_listview, parent, false);
            holder.news_title = (TextView) convertView
                    .findViewById(R.id.news_title);
            holder.news_logo = (ImageView) convertView
                    .findViewById(R.id.news_logo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeNewInfoEntity homeNewInfoEntity = getItem(position);
        holder.news_title.setText(homeNewInfoEntity.getTitle());
        ImageLoader.getInstance().displayImage(homeNewInfoEntity.getCover(),holder.news_logo);
        return convertView;
    }

    public class ViewHolder {
        private ImageView news_logo;
        private TextView news_title;
    }
}
