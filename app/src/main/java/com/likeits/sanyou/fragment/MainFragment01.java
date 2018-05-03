package com.likeits.sanyou.fragment;


import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.HomeNewsAdapter;
import com.likeits.sanyou.citypicker.CityPickerActivity;
import com.likeits.sanyou.entity.HomeAdInfoEntity;
import com.likeits.sanyou.entity.HomeNewInfoEntity;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.SearchInfoActivity;
import com.likeits.sanyou.ui.base.BaseFragment;
import com.likeits.sanyou.ui.chat.NearFriendActivity;
import com.likeits.sanyou.ui.information.ImformationListActivity;
import com.likeits.sanyou.ui.information.InformationDetailsActivity;
import com.likeits.sanyou.ui.me.AboutActivity;
import com.likeits.sanyou.ui.me.MyShoppingCartActivity;
import com.likeits.sanyou.ui.shop.ShopDetailsActivity;
import com.likeits.sanyou.ui.shop.ShoppingListActivity;
import com.likeits.sanyou.ui.shop.TodayRecommendActivity;
import com.likeits.sanyou.ui.weather.WeatherListActivity;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.LoaddingDialog;
import com.likeits.sanyou.utils.StringUtil;
import com.likeits.sanyou.utils.UtilPreference;
import com.likeits.sanyou.view.MyGridView;
import com.likeits.sanyou.view.MyListview;
import com.likeits.sanyou.view.NoScrollViewPager;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment01 extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ScrollView>, View.OnClickListener {
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private MyGridView mGridView;
    private static final int REQUEST_REGION_PICK = 1;//城市返回标识


    private int[] icon = {R.mipmap.icon_home_recommend, R.mipmap.icon_home_device,
            R.mipmap.icon_home_message, R.mipmap.icon_home_friend, R.mipmap.icon_home_shop, R.mipmap.icon_home_shopping_cart, R.mipmap.icon_home_weather, R.mipmap.icon_home_about};
    private String[] iconName = {"今日推荐", "连接设备", "今日资讯", "同城钓友", "商城", "购物车", "天气", "关于我们"};
    private List<Map<String, Object>> dataList;
    private SimpleAdapter simpleAdapter;
    private TextView mOldPrice, mOldPrice2, mOldPrice3;
    private LinearLayout llShop01, llShop02, llShop03;
    private NoScrollViewPager mViewPage;
    private TextView tvWeather;
    private TextView tvAddress;
    private ImageView ivAddress;
    private LinearLayout searchLayout;
    private String city;
    private String weather;
    private LoaddingDialog loaddingDialog;
    private MyListview mListView;
    private List<HomeNewInfoEntity> newsData;
    private HomeNewsAdapter newAdapter;
    private List<HomeAdInfoEntity> adData;
    private ImageView iv_shop01, iv_shop02, iv_shop03;
    private TextView mNewPrice, mNewPrice02, mNewPrice03;
    private TextView tv_shopname02, tv_shopname03, tv_shoptitle02, tv_shoptitle03;
    private Intent intent;
    private Drawable rightDrawable;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_fragment01;
    }

    @Override
    protected void lazyLoad() {
        loaddingDialog = new LoaddingDialog(getActivity());
        city = UtilPreference.getStringValue(getActivity(), "city");

        newsData = new ArrayList<>();
        adData = new ArrayList<>();
        if (StringUtil.isBlank(city)) {
            city = "中山";
            UtilPreference.saveString(getActivity(), "city", city);
        }
        initView();
        initData(city);//天气
        loaddingDialog.show();
        initAD();
        initNews();//資訊
    }

    private void initAD() {
        String url = MyApiService.GetGuanggao;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("cid", 1);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG33", response);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    String msg = obj.optString("msg");
                    JSONArray array = obj.optJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        HomeAdInfoEntity homeAdInfoEntity = JSON.parseObject(array.optString(i), HomeAdInfoEntity.class);
                        adData.add(homeAdInfoEntity);
                    }
                    //newAdapter.notifyDataSetChanged();
                    String role= UtilPreference.getStringValue(getActivity(),"role");
                    ImageLoader.getInstance().displayImage(adData.get(0).getPath(), iv_shop01);
                    mOldPrice.setText("¥ "+adData.get(0).getMoney_raw());
                    ImageLoader.getInstance().displayImage(adData.get(1).getPath(), iv_shop02);
                    mOldPrice2.setText("¥ "+adData.get(1).getMoney_raw());
                    tv_shopname02.setText(adData.get(1).getGoods_name());
                    tv_shoptitle02.setText(adData.get(1).getGoods_introduct());
                    ImageLoader.getInstance().displayImage(adData.get(2).getPath(), iv_shop03);
                    mOldPrice3.setText("¥ "+adData.get(2).getMoney_raw());
                    tv_shopname03.setText(adData.get(2).getGoods_name());
                    tv_shoptitle03.setText(adData.get(2).getGoods_introduct());
                    if ("1".equals(role)) {
                        mNewPrice.setText("¥ " + adData.get(0).getMoney_need());
                        mNewPrice02.setText("¥ " + adData.get(1).getMoney_need());
                        mNewPrice03.setText("¥ " + adData.get(2).getMoney_need());
                    }else{
                        mNewPrice.setText("¥ " + adData.get(0).getMoney_vip());
                        mNewPrice02.setText("¥ " + adData.get(1).getMoney_vip());
                        mNewPrice03.setText("¥ " + adData.get(2).getMoney_vip());
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

    private void initNews() {
        String url = MyApiService.GetHomeList;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG22", response);
                loaddingDialog.dismiss();
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    String msg = obj.optString("msg");
                    JSONArray array = obj.optJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        HomeNewInfoEntity homeNewInfoEntity = JSON.parseObject(array.optString(i), HomeNewInfoEntity.class);
                        newsData.add(homeNewInfoEntity);
                    }
                    newAdapter = new HomeNewsAdapter(getActivity(), newsData);
                    mListView.setAdapter(newAdapter);
                    newAdapter.notifyDataSetChanged();

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
                loaddingDialog.dismiss();
               //initView();
            }
        });
    }

    private void initData(final String city) {
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
                        String exerciseIndex = object.optString("exerciseIndex");
                        weather = object.optString("weather");
                    }
                    tvAddress.setText(city);
                    tvWeather.setText(weather);
                    if("晴".equals(weather)){
                        rightDrawable = getResources().getDrawable(R.drawable.icon_sunshine);
                        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                        tvWeather.setCompoundDrawables(null, null, rightDrawable, null);
                    }else if("多云".equals(weather)||"少云".equals(weather)||"阴".equals(weather)){
                        rightDrawable = getResources().getDrawable(R.drawable.icon_cloudy);
                        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                        tvWeather.setCompoundDrawables(null, null, rightDrawable, null);
                    }
                    else if("小雨".equals(weather)||"雨".equals(weather)||"中雨".equals(weather)||"阵雨".equals(weather)||"零散阵雨".equals(weather)){
                        rightDrawable = getResources().getDrawable(R.drawable.icon_rain);
                        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                        tvWeather.setCompoundDrawables(null, null, rightDrawable, null);
                    }
                    else if("小雪".equals(weather)||"雨夹雪".equals(weather)||"阵雪".equals(weather)||"大雪".equals(weather)){
                        rightDrawable = getResources().getDrawable(R.drawable.icon_snow);
                        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                        tvWeather.setCompoundDrawables(null, null, rightDrawable, null);
                    } else if("雷阵雨".equals(weather)||"零散雷雨".equals(weather)){
                        rightDrawable = getResources().getDrawable(R.drawable.icon_thundershower);
                        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                        tvWeather.setCompoundDrawables(null, null, rightDrawable, null);
                    } else if("霾".equals(weather)){
                        rightDrawable = getResources().getDrawable(R.drawable.icon_haze);
                        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                        tvWeather.setCompoundDrawables(null, null, rightDrawable, null);
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

    private void initListener() {
        //llImformation.setOnClickListener(this);
        llShop01.setOnClickListener(this);
        llShop02.setOnClickListener(this);
        llShop03.setOnClickListener(this);
        tvWeather.setOnClickListener(this);
        tvAddress.setOnClickListener(this);
        ivAddress.setOnClickListener(this);
        searchLayout.setOnClickListener(this);
        tvWeather.setOnClickListener(this);
    }

    private void initView() {
        tvAddress = findViewById(R.id.tv_address);
        ivAddress = findViewById(R.id.userinfo_img);
        searchLayout = findViewById(R.id.search_layout);
        mListView = findViewById(R.id.home_new_listview);
        mPullToRefreshScrollView = findViewById(R.id.ll_home_scrollview);
        tvWeather = findViewById(R.id.tv_weather);
        //头部
        mGridView = findViewById(R.id.home_header_gridView);
        //中部
        iv_shop01 = findViewById(R.id.iv_shop01);
        iv_shop02 = findViewById(R.id.iv_shop02);
        iv_shop03 = findViewById(R.id.iv_shop03);
        tv_shopname02 = findViewById(R.id.tv_shopname02);
        tv_shoptitle02 = findViewById(R.id.tv_shoptitle02);
        tv_shopname03 = findViewById(R.id.tv_shopname03);
        tv_shoptitle03 = findViewById(R.id.tv_shoptitle03);
        mNewPrice = findViewById(R.id.home_header_gridView_new_price);
        mNewPrice02 = findViewById(R.id.home_header_gridView_new_price02);
        mNewPrice03 = findViewById(R.id.home_header_gridView_new_price03);
        mOldPrice = findViewById(R.id.home_header_gridView_old_price);
        mOldPrice2 = findViewById(R.id.home_header_gridView_old_price02);
        mOldPrice3 = findViewById(R.id.home_header_gridView_old_price03);
        mOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mOldPrice2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mOldPrice3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        //资讯
        //  llImformation = findViewById(R.id.ll_imfromation);
        llShop01 = findViewById(R.id.ll_shop01);
        llShop02 = findViewById(R.id.ll_shop02);
        llShop03 = findViewById(R.id.ll_shop03);

        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshScrollView.setOnRefreshListener(this);
        mPullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
                "上次刷新时间");
        mPullToRefreshScrollView.getLoadingLayoutProxy()
                .setPullLabel("下拉刷新");
        mPullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                "松开即可刷新");
        dataList = new ArrayList<Map<String, Object>>();
        getData();
        String[] from = {"img", "name"};
        final int[] to = {R.id.home_header_gridView_avatar, R.id.home_header_gridView_header};
        simpleAdapter = new SimpleAdapter(getActivity(), dataList, R.layout.layout_home_header_gridview_item, from, to);
        //配置适配器
        mGridView.setAdapter(simpleAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        toActivity(TodayRecommendActivity.class);
                        break;
                    case 1:
                        mViewPage = (NoScrollViewPager) getActivity().findViewById(R.id.home_viewpager);
                        mViewPage.setCurrentItem(1);
                        break;
                    case 2:
                        toActivity(ImformationListActivity.class);
                        break;
                    case 3:
                        toActivity(NearFriendActivity.class);
                        break;
                    case 4:
                        toActivity(ShoppingListActivity.class);
                        break;
                    case 5:
                        toActivity(MyShoppingCartActivity.class);
                        break;
                    case 6:
                    case R.id.tv_weather:
                        Intent intent = new Intent(getActivity(), WeatherListActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        toActivity(AboutActivity.class);
                        break;
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context, InformationDetailsActivity.class);
                intent.putExtra("id",newsData.get(position).getId());
                context.startActivity(intent);
            }
        });
        initListener();
    }

    private List<Map<String, Object>> getData() {
        for (int i = 0; i < icon.length; i++) {
            Log.d("TAG", "" + icon.length);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", icon[i]);
            map.put("name", iconName[i]);
            dataList.add(map);
        }
        return dataList;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        mPullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        mPullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.ll_imfromation:
//                toActivity(InformationDetailsActivity.class);
//                break;
            case R.id.search_layout:
                toActivity(SearchInfoActivity.class);
                break;
            case R.id.ll_shop01:
                intent = new Intent(getActivity(), ShopDetailsActivity.class);
                intent.putExtra("id", adData.get(0).getGoodsid());
                startActivity(intent);
                break;
            case R.id.ll_shop02:
                intent = new Intent(getActivity(), ShopDetailsActivity.class);
                intent.putExtra("id", adData.get(1).getGoodsid());
                startActivity(intent);

                break;
            case R.id.ll_shop03:
                intent = new Intent(getActivity(), ShopDetailsActivity.class);
                intent.putExtra("id", adData.get(2).getGoodsid());
                startActivity(intent);
                break;
            case R.id.tv_weather:
                toActivity(WeatherListActivity.class);
                break;
            case R.id.tv_address:
            case R.id.userinfo_img:
                Intent intent = new Intent(getActivity(), CityPickerActivity.class);
                startActivityForResult(intent, REQUEST_REGION_PICK);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REGION_PICK) {
            if (data != null) {
                city = data.getStringExtra("date");
                UtilPreference.saveString(getActivity(), "city", city);
                initData(city);
                //tvAddress.setText(city);
            }
        }

    }

}
