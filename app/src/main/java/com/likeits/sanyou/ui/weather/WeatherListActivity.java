package com.likeits.sanyou.ui.weather;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.WeatherAdapter;
import com.likeits.sanyou.citypicker.CityPickerActivity;
import com.likeits.sanyou.entity.WeatherInfoEntity;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.base.Container;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.UtilPreference;
import com.likeits.sanyou.view.MyGridView;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeatherListActivity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.weather_GridView)
    MyGridView mGridView;
    private static final int REQUEST_REGION_PICK = 3;
    private String city;
    @BindView(R.id.tv_grade)
    TextView tv_grade;//评分
    @BindView(R.id.weather_comment)
    TextView weather_comment;//适不适合
    @BindView(R.id.tv_temperature)
    TextView tv_temperature;//温度
    @BindView(R.id.tv_temperature01)
    TextView tv_temperature01;//气候
    @BindView(R.id.tv_quality)
    TextView tv_quality;//空气质量
    @BindView(R.id.tv_humidity)
    TextView tv_humidity;//湿度:34%
    @BindView(R.id.tv_pressure)
    TextView tv_pressure;//气压:1008hPa
    @BindView(R.id.tv_Sunrise_sunset_time)
    TextView tv_Sunrise_sunset_time;//日出日落:06:09/18:44
    @BindView(R.id.ic_logo)
    ImageView ic_logo;//日出日落:06:09/18:44
    private WeatherInfoEntity mWeatherInfoEntit;
    private List<WeatherInfoEntity> weatherData;
    private WeatherAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_list);
        ButterKnife.bind(this);
        city = UtilPreference.getStringValue(mContext, "city");
        Log.d("TAG8989",city);
        weatherData = new ArrayList<>();
        initView();
        initData(city);
        initAD();
    }

    private void initAD() {
        String url = MyApiService.GetGuanggao;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("cid", 5);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG33", response);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    String msg = obj.optString("msg");
                    if("1".equals(obj.optString("code"))){
                        JSONArray array = obj.optJSONArray("data");
                        JSONObject object=array.optJSONObject(0);
                        ImageLoader.getInstance().displayImage(object.optString("path"),ic_logo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failed(Throwable e) {

            }
        });
    }

    private void initData(String city) {
        String url = "http://apicloud.mob.com/v1/weather/query";
        RequestParams params = new RequestParams();
        params.put("key", "20d684bcd53f6");
        params.put("city", city);
        params.put("province", "");
        HttpUtil.get(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String msg = obj.optString("msg");
                    JSONArray array = obj.optJSONArray("result");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.optJSONObject(i);
                        tv_quality.setText("空气质量:" + object.optString("airCondition"));
                        tv_temperature01.setText(object.optString("weather"));
                        String exerciseIndex = object.optString("exerciseIndex");
                        if ("适宜".equals(exerciseIndex)) {
                            tv_grade.setText("78");
                            weather_comment.setText(exerciseIndex + "钓鱼的好天气！");
                        } else if ("不适宜".equals(exerciseIndex)) {
                            tv_grade.setText("51");
                            weather_comment.setText(exerciseIndex + "钓鱼的好天气！");
                        } else {
                            tv_grade.setText("92");
                            weather_comment.setText(exerciseIndex + "钓鱼的好天气！");
                        }
                        tv_temperature.setText(object.optString("temperature"));
                        tv_humidity.setText(object.optString("humidity"));
                        tv_Sunrise_sunset_time.setText("日出日落:" + object.optString("sunrise") + "/" + object.optString("sunset"));
                        json(object.optJSONArray("future"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void json(JSONArray future) {
        for (int i = 0; i < 4; i++) {
            JSONObject obj = future.optJSONObject(i);
            mWeatherInfoEntit = new WeatherInfoEntity();
            mWeatherInfoEntit.setDate(obj.optString("date"));
            mWeatherInfoEntit.setDayTime(obj.optString("dayTime"));
            mWeatherInfoEntit.setNight(obj.optString("night"));
            mWeatherInfoEntit.setTemperature(obj.optString("temperature"));
            mWeatherInfoEntit.setWeek(obj.optString("week"));
            mWeatherInfoEntit.setWind(obj.optString("wind"));
            weatherData.add(mWeatherInfoEntit);
        }
        mAdapter.notifyDataSetChanged();
        Log.d("TAG333", mWeatherInfoEntit.getDate());
    }


    private void initView() {
        tvHeader.setText(city);
        Drawable drawable = getResources().getDrawable(R.mipmap.nav_adress);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        tvHeader.setCompoundDrawables(drawable, null, null, null);//画在右边
        mAdapter = new WeatherAdapter(mContext, weatherData);
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    @OnClick({R.id.backBtn, R.id.tv_header})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_header:
                Intent intent = new Intent(mContext, CityPickerActivity.class);
                startActivityForResult(intent, REQUEST_REGION_PICK);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REGION_PICK) {
            if (data != null) {

                city = data.getStringExtra("date");
                UtilPreference.saveString(mContext, "city", city);
                Log.d("TAG", "date:" + city);
                tvHeader.setText(city);
                refresh();
            }
        }
    }

    private void refresh() {
        if (weatherData == null || weatherData.size() == 0) {
            return;
        } else weatherData.clear();
        initData(city);
    }
}
