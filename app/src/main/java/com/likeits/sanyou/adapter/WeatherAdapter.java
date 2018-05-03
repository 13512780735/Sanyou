package com.likeits.sanyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.WeatherInfoEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */

public class WeatherAdapter extends MyBaseAdapter<WeatherInfoEntity> {
    public WeatherAdapter(Context context, List<WeatherInfoEntity> mWeatherInfoEntity) {
        super(context, mWeatherInfoEntity);
    }

    @Override
    public View getItemView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(
                    R.layout.layout_weather_listview_items, parent, false);
            holder.tv_today = (TextView) convertView
                    .findViewById(R.id.weather_time);
            holder.tv_temperature01 = (TextView) convertView
                    .findViewById(R.id.tv_temperature01);
            holder.tv_temperature = (TextView) convertView
                    .findViewById(R.id.tv_tv_temperature);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        WeatherInfoEntity mWeatherInfoEntity = getItem(position);
        holder.tv_today.setText(mWeatherInfoEntity.getWeek());
        String dayTime = mWeatherInfoEntity.getDayTime();
        if ("晴".equals(dayTime)) {
            holder.tv_temperature01.setText(dayTime);
            holder.tv_temperature01.setBackgroundResource(R.drawable.weather_orange);
        } else if("阴".equals(dayTime)||"雨".equals(dayTime)||"雷阵雨".equals(dayTime)||"零散阵雨".equals(dayTime)||"零散雷雨".equals(dayTime)||"雨夹雪".equals(dayTime)||"霾".equals(dayTime)){
            holder.tv_temperature01.setText(dayTime);
            holder.tv_temperature01.setBackgroundResource(R.drawable.weather_black);
        }else{
            holder.tv_temperature01.setText(dayTime);
            holder.tv_temperature01.setBackgroundResource(R.drawable.weather_blue);
        }
        holder.tv_temperature.setText(mWeatherInfoEntity.getTemperature());
        return convertView;
    }


    public class ViewHolder {
        private TextView tv_today, tv_temperature01, tv_temperature;
    }
}