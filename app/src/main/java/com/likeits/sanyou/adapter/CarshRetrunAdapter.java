package com.likeits.sanyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.CashBackEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/30.
 */

public class CarshRetrunAdapter extends MyBaseAdapter<CashBackEntity>{
    public CarshRetrunAdapter(Context context, List<CashBackEntity> cashBackEntities) {
        super(context, cashBackEntities);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(
                    R.layout.layout_carshreturn_listview, parent, false);
            holder.tv_sn = (TextView) convertView
                    .findViewById(R.id.tv_sn);
            holder.tv_shopName = (TextView) convertView
                    .findViewById(R.id.tv_shopName);
            holder.tv_sum = (TextView) convertView
                    .findViewById(R.id.tv_sum);
            holder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CashBackEntity cashBackEntity = getItem(position);
        holder.tv_sn.setText("订单号："+cashBackEntity.getGoods_id());
        holder.tv_shopName.setText(cashBackEntity.getGoods_name());
        holder.tv_sum.setText("+"+cashBackEntity.getMoney());
        holder.tv_time.setText(cashBackEntity.getTime());
        return convertView;
    }

    public class ViewHolder {
        private TextView tv_sn, tv_shopName, tv_sum,tv_time;
    }
}
