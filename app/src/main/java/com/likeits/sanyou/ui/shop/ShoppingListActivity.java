package com.likeits.sanyou.ui.shop;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.ShopHomeListAdapter;
import com.likeits.sanyou.entity.HomeAdInfoEntity;
import com.likeits.sanyou.entity.ShopHomeInfoEntity;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.SearchInfoActivity;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.ui.me.MyShoppingCartActivity;
import com.likeits.sanyou.utils.LoaddingDialog;
import com.likeits.sanyou.utils.ToastUtil;
import com.likeits.sanyou.utils.UtilPreference;
import com.likeits.sanyou.view.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingListActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ScrollView> {
    @BindView(R.id.ll_home_scrollview)
    PullToRefreshScrollView mPullToRefreshScrollView;
    @BindView(R.id.shopList_gridView)
    MyGridView mGridView;
    @BindView(R.id.shopping_gridview01)
    MyGridView mGridView01;
    @BindView(R.id.slider)
    SliderLayout sliderShow;
    private ImageView iv_shop01, iv_shop02, iv_shop03;
    private TextView mNewPrice, mNewPrice02, mNewPrice03;
    private TextView tv_shopname02, tv_shopname03, tv_shoptitle02, tv_shoptitle03;
    private TextView mOldPrice, mOldPrice2, mOldPrice3;
    private LinearLayout llShop01, llShop02, llShop03;
    private List<HomeAdInfoEntity> adData;
    private int[] icon = {R.mipmap.icon_shopping_all, R.mipmap.icon_shopping_activity,
            R.mipmap.icon_shopping_new, R.mipmap.icon_shopping_sanyou, R.mipmap.icon_shopping_accessories};
    private String[] iconName = {"全部分类", "商城活动", "新品专区", "三友钓鱼灯", "通用配件"};
    private List<Map<String, Object>> dataList;
    private SimpleAdapter simpleAdapter;


//    /**
//     * 初始數據
//     */
//    private int[] icon01 = {R.mipmap.shop_test01, R.mipmap.shop_test02,
//            R.mipmap.shop_test03, R.mipmap.shop_test04};
//    private String[] iconName01 = {"顶点钓鱼灯夜钓灯 紫光蓝光黄白四光源大容量...", "顶点钓鱼灯夜钓灯 紫光蓝光黄白四光源大容量...", "顶点钓鱼灯夜钓灯 紫光蓝光黄白四光源大容量...", "顶点钓鱼灯夜钓灯 紫光蓝光黄白四光源大容量..."};
//    private String[] iconPrice = {"¥ 680.00", "¥ 680.00", "¥ 680.00", "¥ 680.00"};
//    private String[] iconSales = {"销量:1713", "销量:1713", "销量:1713", "销量:1713"};
//    private List<Map<String, Object>> dataList01;
    private SimpleAdapter simpleAdapter01;
    private List<ShopHomeInfoEntity> shopData;
    private ShopHomeListAdapter mGridAdapter;
    private LoaddingDialog loading;
    private List<HomeAdInfoEntity> adData01;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ButterKnife.bind(this);
        loading=new LoaddingDialog(this);
        initView();
        initAD01();
        initAD02();//轮播
        initShopList();
        loading.show();
      //  imageSlider();
    }

    private void initAD02() {
        Log.d("TAG","1");
        HttpMethods.getInstance().GetGuanggao(new MySubscriber<ArrayList<HomeAdInfoEntity>>(this) {
            @Override
            public void onHttpCompleted(HttpResult<ArrayList<HomeAdInfoEntity>> httpResult) {
                if (httpResult.getCode() == 1) {
                    adData01 = httpResult.getData();
                    imageSlider();
                } else {
                    showToast(httpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey, 4);
    }

    private void initShopList() {
        Log.d("TAG","2");
        HttpMethods.getInstance().GetShopHomeList(new MySubscriber<ArrayList<ShopHomeInfoEntity>>(this) {
            @Override
            public void onHttpCompleted(HttpResult<ArrayList<ShopHomeInfoEntity>> httpResult) {
                loading.dismiss();
                if (httpResult.getCode() == 1) {
                    shopData = httpResult.getData();
                    mGridAdapter = new ShopHomeListAdapter(mContext, shopData);
                    mGridView01.setAdapter(mGridAdapter);
                    mGridAdapter.notifyDataSetChanged();
                } else {
                    showToast(httpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey);
    }

    private void initAD01() {
        Log.d("TAG","1");
        HttpMethods.getInstance().GetGuanggao(new MySubscriber<ArrayList<HomeAdInfoEntity>>(this) {
            @Override
            public void onHttpCompleted(HttpResult<ArrayList<HomeAdInfoEntity>> httpResult) {
                if (httpResult.getCode() == 1) {
                    String role= UtilPreference.getStringValue(mContext,"role");
                    adData = httpResult.getData();
                    ImageLoader.getInstance().displayImage(adData.get(0).getPath(), iv_shop01);

                    mOldPrice.setText("¥ " + adData.get(0).getMoney_raw());
                    ImageLoader.getInstance().displayImage(adData.get(1).getPath(), iv_shop02);

                    mOldPrice2.setText("¥ " + adData.get(1).getMoney_raw());
                    tv_shopname02.setText(adData.get(1).getGoods_name());
                    tv_shoptitle02.setText(adData.get(1).getGoods_introduct());
                    ImageLoader.getInstance().displayImage(adData.get(2).getPath(), iv_shop03);

                    mOldPrice3.setText("¥ " + adData.get(2).getMoney_raw());
                    tv_shopname03.setText(adData.get(2).getGoods_name());
                    tv_shoptitle03.setText(adData.get(2).getGoods_introduct());
                    if ("1".equals(role)) {
                        mNewPrice.setText("¥ " + adData.get(0).getMoney_need());
                        mNewPrice02.setText("¥ " + adData.get(1).getMoney_need());
                        mNewPrice03.setText("¥ " + adData.get(2).getMoney_need());
                    }else {
                        mNewPrice.setText("¥ " + adData.get(0).getMoney_vip());
                        mNewPrice02.setText("¥ " + adData.get(1).getMoney_vip());
                        mNewPrice03.setText("¥ " + adData.get(2).getMoney_vip());
                    }
                } else {
                    showToast(httpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey, 1);
    }

    private void imageSlider() {

        for (int i = 0; i < adData01.size(); i++) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(mContext);
            // textSliderView.description("");//设置标题
            defaultSliderView.image(adData01.get(i).getPath());//设置图片的网络地址

            //添加到布局中显示
            sliderShow.addSlider(defaultSliderView);
        }
        //其他设置
        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);//使用默认指示器，在底部显示
        sliderShow.setDuration(5000);//停留时间
        sliderShow.setPresetTransformer(SliderLayout.Transformer.Default);
    }

    private void initView() {
        //中部
        iv_shop01 = (ImageView) findViewById(R.id.iv_shop01);
        iv_shop02 = (ImageView) findViewById(R.id.iv_shop02);
        iv_shop03 = (ImageView) findViewById(R.id.iv_shop03);
        tv_shopname02 = (TextView) findViewById(R.id.tv_shopname02);
        tv_shoptitle02 = (TextView) findViewById(R.id.tv_shoptitle02);
        tv_shopname03 = (TextView) findViewById(R.id.tv_shopname03);
        tv_shoptitle03 = (TextView) findViewById(R.id.tv_shoptitle03);
        mNewPrice = (TextView) findViewById(R.id.home_header_gridView_new_price);
        mNewPrice02 = (TextView) findViewById(R.id.home_header_gridView_new_price02);
        mNewPrice03 = (TextView) findViewById(R.id.home_header_gridView_new_price03);
        mOldPrice = (TextView) findViewById(R.id.home_header_gridView_old_price);
        mOldPrice2 = (TextView) findViewById(R.id.home_header_gridView_old_price02);
        mOldPrice3 = (TextView) findViewById(R.id.home_header_gridView_old_price03);
        mOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mOldPrice2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mOldPrice3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        llShop01 = (LinearLayout) findViewById(R.id.ll_shop01);
        llShop02 = (LinearLayout) findViewById(R.id.ll_shop02);
        llShop03 = (LinearLayout) findViewById(R.id.ll_shop03);
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
        simpleAdapter = new SimpleAdapter(mContext, dataList, R.layout.layout_home_header_gridview_item, from, to);
        //配置适配器
        mGridView.setAdapter(simpleAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        toActivity(ShopRecommendActivity.class);
                        break;
                    case 1:
                        ToastUtil.showS(mContext, "敬请期待");
                        break;
                    case 2:
                        toActivity(ShopRecommendActivity.class);
                        break;
                    case 3:
                        toActivity(ShopRecommendActivity.class);
                        break;
                    case 4:
                        toActivity(ShopRecommendActivity.class);
                        break;
                }
            }
        });
        //dataList01 = new ArrayList<Map<String, Object>>();
        //getData01();
//        String[] from1 = {"img", "name", "price", "sales"};
//        final int[] to1 = {R.id.iv_avatar, R.id.tv_tvName, R.id.tv_price, R.id.tv_sales};
//        simpleAdapter01 = new SimpleAdapter(mContext, dataList01, R.layout.shop_recommned_listview_items, from1, to1);
//        //配置适配器

        mGridView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //toActivity(ShopDetailsActivity.class);
                Intent intent=new Intent(mContext, ShopDetailsActivity.class);
                intent.putExtra("id",shopData.get(position).getId());
                startActivity(intent);
            }
        });

    }

//    private List<Map<String, Object>> getData01() {
//        for (int i = 0; i < icon01.length; i++) {
//            Log.d("TAG", "" + icon01.length);
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("img", icon01[i]);
//            map.put("name", iconName01[i]);
//            map.put("price", iconPrice[i]);
//            map.put("sales", iconSales[i]);
//            dataList01.add(map);
//        }
//        return dataList01;
//    }

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

    @OnClick({R.id.backBtn, R.id.message_img, R.id.ll_shop01, R.id.ll_shop02, R.id.ll_shop03, R.id.search_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.search_layout:
                toActivity(SearchInfoActivity.class);
                break;
            case R.id.message_img:
             intent = new Intent(mContext, MyShoppingCartActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_shop01:
                intent=new Intent(mContext, ShopDetailsActivity.class);
                intent.putExtra("id",adData.get(0).getGoodsid());
                startActivity(intent);
                break;
            case R.id.ll_shop02:
                intent=new Intent(mContext, ShopDetailsActivity.class);
                intent.putExtra("id",adData.get(1).getGoodsid());
                startActivity(intent);
                break;
            case R.id.ll_shop03:
                intent=new Intent(mContext, ShopDetailsActivity.class);
                intent.putExtra("id",adData.get(2).getGoodsid());
                startActivity(intent);
                break;
        }
    }
}
