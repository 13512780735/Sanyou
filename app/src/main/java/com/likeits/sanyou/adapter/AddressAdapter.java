package com.likeits.sanyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.AddressVo;

import java.util.List;

public class AddressAdapter extends MyBaseAdapter<AddressVo> {

	public AddressAdapter(Context context, List<AddressVo> ts) {
		super(context, ts);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = getInflater().inflate(
					R.layout.addressselect_listviewitems, parent, false);
			holder.tvAddress = (TextView) convertView
					.findViewById(R.id.tv_addressselect);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AddressVo data1 = getItem(position);
		holder.tvAddress.setText(data1.getName());
		return convertView;
	}

	private class ViewHolder {
		TextView tvAddress;

	}
}
