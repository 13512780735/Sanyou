package com.likeits.sanyou.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.MyPostsAdapter01;
import com.likeits.sanyou.citypicker.CityPickerActivity;
import com.likeits.sanyou.entity.CenterPosterBean;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.base.BaseFragment;
import com.likeits.sanyou.ui.me.MyfriendActivity;
import com.likeits.sanyou.ui.me.PostsActivity;
import com.likeits.sanyou.ui.shop.TodayRecommendActivity;
import com.likeits.sanyou.ui.weather.WeatherListActivity;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.LoaddingDialog;
import com.likeits.sanyou.utils.UtilPreference;
import com.likeits.sanyou.view.MyListview;
import com.likeits.sanyou.view.ObservableScrollView;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment03 extends BaseFragment implements View.OnClickListener {

    private MyListview listView;
    private ImageView imageView;
    private TextView textView;
    TextView tv_grade, weather_comment, tv_temperature, tv_quality, tv_humidity, tv_pressure;
    private ObservableScrollView scrollView;
    private int imageHeight;
    private LinearLayout llHeader;
    private TextView tvMove, tvFriend, tvFishing;
    private TextView tvAddress;
    private ImageView ivAddress;
    private static final int REQUEST_REGION_PICK = 2;
    private String city;
    int page = 1;
    private List<CenterPosterBean> mData;
    private MyPostsAdapter01 mAdapter;
    private ImageView iv_edit;
    private LoaddingDialog loading;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_fragment03;
    }

    @Override
    protected void lazyLoad() {
        loading=new LoaddingDialog(getActivity());
        city = UtilPreference.getStringValue(getActivity(), "city");
        mData = new ArrayList<>();
        initData(city);
        initPost(page);
        loading.show();
        initView();
        initListeners();
    }

    private void initPost(int page) {
        String url = MyApiService.Getulist;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("page", String.valueOf(page));
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG",response);
                loading.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("code");
                    if ("1".equals(code)) {
                        //total=object.optString("total");
                        JSONArray array = object.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.optJSONObject(i);
                            CenterPosterBean centerPosterBean = JSON.parseObject(obj.toString(), CenterPosterBean.class);
                            mData.add(centerPosterBean);
                        }
                        Log.d("TAG","mData-->"+mData+"");

                        mAdapter.notifyDataSetChanged();
                    } else {
                        showToast(object.optString("message"));
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
               // loading.dismiss();
            }
        });
    }

    private void initView() {
        scrollView = findViewById(R.id.scrollview);
       // scrollView.scrollTo(0, 0);
        scrollView.fullScroll(ObservableScrollView.FOCUS_UP);
        listView = findViewById(R.id.listview);
        imageView = findViewById(R.id.imageview);
        iv_edit=findViewById(R.id.iv_edit);
        llHeader = findViewById(R.id.ll_header);
        tvMove = findViewById(R.id.tv_move);
        tvFriend = findViewById(R.id.tv_friend);
        tvFishing = findViewById(R.id.tv_fishing);
        //定位
        tvAddress = findViewById(R.id.tv_address);
        ivAddress = findViewById(R.id.userinfo_img);
        //天气
        tv_grade = findViewById(R.id.tv_grade);
        weather_comment = findViewById(R.id.weather_comment);
        tv_temperature = findViewById(R.id.tv_temperature);
        tv_quality = findViewById(R.id.tv_quality);
        tv_humidity = findViewById(R.id.tv_humidity);
        tv_pressure = findViewById(R.id.tv_pressure);
        tvAddress.setText(city);
        mAdapter = new MyPostsAdapter01(getActivity(), mData);
        listView.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(listView);
        mAdapter.notifyDataSetChanged();
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


    private void initListeners() {
        tvMove.setOnClickListener(this);
        tvFriend.setOnClickListener(this);
        tvFishing.setOnClickListener(this);
        tvAddress.setOnClickListener(this);
        ivAddress.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = imageView.getHeight();

                scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
                    @Override
                    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                        if (y <= 0) {
                            llHeader.setBackgroundColor(Color.argb((int) 0, 227, 29, 26));//AGB由相关工具获得，或者美工提供
                        } else if (y > 0 && y <= imageHeight) {
                            float scale = (float) y / imageHeight;
                            float alpha = (255 * scale);
                            // 只是layout背景透明(仿知乎滑动效果)
                            llHeader.setBackgroundColor(Color.argb((int) alpha, 227, 29, 26));
                        } else {
                            llHeader.setBackgroundColor(Color.argb((int) 225, 227, 29, 26));
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userinfo_img:
            case R.id.tv_address:
                Intent intent = new Intent(getActivity(), CityPickerActivity.class);
                startActivityForResult(intent, REQUEST_REGION_PICK);
                break;
            case R.id.tv_move:
                Intent intentWeather = new Intent(getActivity(), WeatherListActivity.class);
                intentWeather.putExtra("city", city);
                startActivity(intentWeather);
                break;
            case R.id.tv_friend:
                toActivity(MyfriendActivity.class);
                break;
            case R.id.iv_edit:
                Bundle bundle=new Bundle();
                bundle.putString("keys","1");
                toActivity(PostsActivity.class,bundle);
                break;
            case R.id.tv_fishing:
                toActivity(TodayRecommendActivity.class);
                //   showToast("敬请期待");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REGION_PICK) {
            if (data != null) {
                city = data.getStringExtra("date");
                UtilPreference.saveString(getActivity(), "city", city);
                tvAddress.setText(city);
                refresh();
            }
        }
    }

    private void refresh() {

        initData(city);
    }
    public void setListViewHeightBasedOnChildren(ListView listView){

        // 获取ListView对应的Adapter

        ListAdapter listAdapter=listView.getAdapter();

        if(listAdapter==null){

            return;

        }

        int totalHeight=0;

        for(int i=0;i<listAdapter.getCount();i++){ // listAdapter.getCount()返回数据项的数目

            View listItem=listAdapter.getView(i,null,listView);

            listItem.measure(0,0); // 计算子项View 的宽高

            totalHeight+=listItem.getMeasuredHeight(); // 统计所有子项的总高度

        }

        ViewGroup.LayoutParams params=listView.getLayoutParams();

        params.height=totalHeight
                +(listView.getDividerHeight()*(listAdapter.getCount()-1));

        // listView.getDividerHeight()获取子项间分隔符占用的高度

        // params.height最后得到整个ListView完整显示需要的高度

        listView.setLayoutParams(params);

    }
}
