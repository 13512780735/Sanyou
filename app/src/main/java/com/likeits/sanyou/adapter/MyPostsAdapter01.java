package com.likeits.sanyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.CenterPostImagBean;
import com.likeits.sanyou.entity.CenterPosterBean;
import com.likeits.sanyou.view.CircleImageView;
import com.likeits.sanyou.view.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/3.
 */

public class MyPostsAdapter01 extends MyBaseAdapter<CenterPosterBean> {
    public MyPostsAdapter01(Context context, List<CenterPosterBean> centerPosterBeen) {
        super(context, centerPosterBeen);
    }

    @Override
    public View getItemView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(
                    R.layout.layout_my_postlist_items01, parent, false);
            holder.tv_title = (TextView) convertView
                    .findViewById(R.id.tv_title);
            holder.tv_content = (TextView) convertView
                    .findViewById(R.id.tv_content);
            holder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            holder.user_avatar = (CircleImageView) convertView
                    .findViewById(R.id.user_avatar);
            holder.postGridView = (MyGridView) convertView
                    .findViewById(R.id.postGridView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CenterPosterBean centerPosterBean = getItem(position);
        holder.tv_title.setText(centerPosterBean.getTitle());
        holder.tv_content.setText(centerPosterBean.getContent());
        holder.tv_name.setText(centerPosterBean.getAuthor());
        holder.tv_time.setText(centerPosterBean.getInterval());
        ImageLoader.getInstance().displayImage(centerPosterBean.getHeadimg(), holder.user_avatar);
        List<CenterPostImagBean> mData = new ArrayList<>();
        if (centerPosterBean.getCover_list() == null) {
            holder.postGridView.setVisibility(View.GONE);
        } else if (centerPosterBean.getCover_list() != null) {
            holder.postGridView.setVisibility(View.VISIBLE);
            for (int i = 0; i < centerPosterBean.getCover_list().size(); i++) {
                CenterPostImagBean CenterPostImagBean1 = new CenterPostImagBean();
                CenterPostImagBean1.setPath(centerPosterBean.getCover_list().get(i));
                mData.add(CenterPostImagBean1);
            }
            CenterPostAdapter mAdapter = new CenterPostAdapter(context, mData);
            holder.postGridView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        return convertView;
    }


    public class ViewHolder {
        private TextView tv_title, tv_name, tv_time,tv_content;
        MyGridView postGridView;
        CircleImageView user_avatar;
    }
}
