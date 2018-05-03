package com.likeits.sanyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.InformationDetailsCommentInfoEntity;
import com.likeits.sanyou.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class InformationDetailsCommentAdapter extends MyBaseAdapter<InformationDetailsCommentInfoEntity> {
    public InformationDetailsCommentAdapter(Context context, List<InformationDetailsCommentInfoEntity> informationDetailsCommentInfoEntities) {
        super(context, informationDetailsCommentInfoEntities);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
      ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(
                    R.layout.layout_imformation_details_listview_items, parent, false);
            holder.ivAvatar = (CircleImageView) convertView
                    .findViewById(R.id.me_header_avatar);
            holder.tv_name = (TextView) convertView
                    .findViewById(R.id.me_header_name);
            holder.tv_floor = (TextView) convertView
                    .findViewById(R.id.me_header_floor);
            holder.tv_content = (TextView) convertView
                    .findViewById(R.id.home_listview_content);
            holder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InformationDetailsCommentInfoEntity informationDetailsCommentInfoEntity = getItem(position);
        holder.tv_name.setText(informationDetailsCommentInfoEntity.getNickname());
        holder.tv_time.setText(informationDetailsCommentInfoEntity.getInterval());
        holder.tv_content.setText(informationDetailsCommentInfoEntity.getContent());
        holder.tv_floor.setText(informationDetailsCommentInfoEntity.getFloor()+"楼");
        ImageLoader.getInstance().displayImage(informationDetailsCommentInfoEntity.getHeadimg(),holder.ivAvatar);
        return convertView;
    }
    public class ViewHolder {
        private CircleImageView ivAvatar;
        private TextView tv_name, tv_time, tv_content,tv_floor;
    }
}
