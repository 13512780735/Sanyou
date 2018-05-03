package com.likeits.sanyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.BankInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/12/30.
 */

public class BankListAdapter extends MyBaseAdapter<BankInfo> {
    public BankListAdapter(Context context, List<BankInfo> bankInfos) {
        super(context, bankInfos);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(
                    R.layout.layout_bank_listview_item, parent, false);
            holder.tvName = (TextView) convertView
                    .findViewById(R.id.tv_tvName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankInfo bankInfo = getItem(position);
        holder.tvName.setText(bankInfo.getBank());
        return convertView;
    }

    public class ViewHolder {
        private TextView tvName;
    }
}
