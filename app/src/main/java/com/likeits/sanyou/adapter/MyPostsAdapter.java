package com.likeits.sanyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.CenterPostImagBean;
import com.likeits.sanyou.entity.CenterPosterBean;
import com.likeits.sanyou.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/3.
 */

public class MyPostsAdapter extends MyBaseAdapter<CenterPosterBean> {
    public MyPostsAdapter(Context context, List<CenterPosterBean> centerPosterBeen) {
        super(context, centerPosterBeen);
    }

    @Override
    public View getItemView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(
                    R.layout.layout_my_postlist_items, parent, false);
            holder.tv_title = (TextView) convertView
                    .findViewById(R.id.tv_title);
            holder.tv_content = (TextView) convertView
                    .findViewById(R.id.tv_content);
            holder.postGridView = (MyGridView) convertView
                    .findViewById(R.id.postGridView);
            holder.tv_del = (TextView) convertView
                    .findViewById(R.id.tv_del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CenterPosterBean centerPosterBean = getItem(position);
        holder.tv_title.setText(centerPosterBean.getTitle());
        holder.tv_content.setText(centerPosterBean.getContent());
        List<CenterPostImagBean> mData = new ArrayList<>();
        for (int i = 0; i < centerPosterBean.getCover_list().size(); i++) {
            CenterPostImagBean CenterPostImagBean1 = new CenterPostImagBean();
            CenterPostImagBean1.setPath(centerPosterBean.getCover_list().get(i));
            mData.add(CenterPostImagBean1);
        }
        CenterPostAdapter mAdapter = new CenterPostAdapter(context, mData);
        holder.postGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        holder.tv_del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDelClickListener.onDelClick(position);

            }
        });

        return convertView;
    }

    /**
     * 点击刪除的时候
     */
    public interface onDelClickListener {
        void onDelClick(int i);
    }

    private ReceivingAddressAdapter.onDelClickListener mDelClickListener;

    public void setOnDelClickListener(
            ReceivingAddressAdapter.onDelClickListener onDelClickListener) {
        this.mDelClickListener = onDelClickListener;
    }


    public class ViewHolder {
        private TextView tv_title, tv_content, tv_edit, tv_del;
        MyGridView postGridView;
    }
}
