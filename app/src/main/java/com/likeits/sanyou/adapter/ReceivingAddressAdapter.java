package com.likeits.sanyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.AddressBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */

public class ReceivingAddressAdapter extends MyBaseAdapter<AddressBean> {
    public ReceivingAddressAdapter(Context context, List<AddressBean> addressBeen) {
        super(context, addressBeen);
    }

    @Override
    public View getItemView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(
                    R.layout.layout_receiving_address_listview_items, parent, false);
            holder.tv_tvName_phone = (TextView) convertView
                    .findViewById(R.id.tv_tvName_phone);
            holder.tv_address = (TextView) convertView
                    .findViewById(R.id.tv_address);
            holder.tv_edit = (TextView) convertView
                    .findViewById(R.id.tv_edit);
            holder.tv_del = (TextView) convertView
                    .findViewById(R.id.tv_del);
            holder.ck_chose = (CheckBox) convertView
                    .findViewById(R.id.ck_chose);
            holder.ivLine = (ImageView) convertView
                    .findViewById(R.id.iv_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AddressBean addressBean = getItem(position);
        String defaultx=addressBean.getDef();
        if("1".equals(defaultx)){
            holder.ivLine.setVisibility(View.VISIBLE);
            holder.ck_chose.setChecked(true);
        }else{
            holder.ivLine.setVisibility(View.GONE);
            holder.ck_chose.setChecked(false);
        }
        holder.tv_tvName_phone.setText("收货人："+addressBean.getName()+"    "+addressBean.getPhone());
        holder.tv_address.setText(addressBean.getDizhi());
        holder.tv_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mEditClickListener.onEditClick(position);

            }
        });
        holder.tv_del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDelClickListener.onDelClick(position);

            }
        });
        holder.ck_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk_choseClickListener.onCk_choseClick(position);
            }
        });
        return convertView;
    }
    /**
     * 点击编辑的时候
     */
    public interface onEditClickListener {
        void onEditClick(int i);
    }

    private onEditClickListener mEditClickListener;

    public void setOnEditClickListener(
            onEditClickListener onEditClickListener) {
        this.mEditClickListener = onEditClickListener;
    }
    /**
     * 点击刪除的时候
     */
    public interface onDelClickListener {
        void onDelClick(int i);
    }

    private onDelClickListener mDelClickListener;

    public void setOnDelClickListener(
            onDelClickListener onDelClickListener) {
        this.mDelClickListener = onDelClickListener;
    }

    /**
     *设置默认
     */
    public interface onCk_choseClickListener {
        void onCk_choseClick(int i);
    }

    private onCk_choseClickListener mCk_choseClickListener;

    public void setOnCk_choseClickListener(
            onCk_choseClickListener onCk_choseClickListener) {
        this.mCk_choseClickListener = onCk_choseClickListener;
    }

    public class ViewHolder {
        private TextView tv_tvName_phone, tv_address, tv_edit, tv_del;
        CheckBox ck_chose;
        ImageView ivLine;
    }
}