package com.likeits.sanyou.ui.shop;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.CategoryDetails;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.EmptyEntity;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.ui.shop.adapter.ShopTabAdapter;
import com.likeits.sanyou.ui.shop.fargment.ShopDetail01Fragment;
import com.likeits.sanyou.ui.shop.fargment.ShopDetail02Fragment;
import com.likeits.sanyou.ui.shop.fargment.ShopDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopDetailsActivity extends BaseActivity {
    private List<String> mDatas;
    private String id;
    private CategoryDetails categoryDetails;
    TabLayout mTabLayout;
    ViewPager viewpager;
    private TextView tv_collect, tv_buy, tv_add;
    private Drawable img;
    private String status;
    private ShopDialogFragment dialog;
    private Bundle bundle;
    private String address;
    private String addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        //, "评价"
        mDatas = new ArrayList<String>(Arrays.asList("商品", "详情"));
        initData();

    }

    private void initData() {
        HttpMethods.getInstance().GetShopDetails(new MySubscriber<CategoryDetails>(this) {
            @Override
            public void onHttpCompleted(HttpResult<CategoryDetails> httpResult) {
                if (httpResult.getCode() == 1) {
                    categoryDetails = httpResult.getData();
                    status = categoryDetails.getStatus();
                    Log.d("TAG33", categoryDetails.getPath());

                    initView();
                }
            }

            @Override
            public void onHttpError(Throwable e) {
            }
        }, ukey, id);
    }

    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tv_collect = (TextView) findViewById(R.id.tv_collect);
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_buy = (TextView) findViewById(R.id.tv_buy);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(viewpager);
        List<Fragment> mfragments = new ArrayList<Fragment>();
        ShopDetail01Fragment shopDetail01Fragment = new ShopDetail01Fragment();
        ShopDetail02Fragment shopDetail02Fragment = new ShopDetail02Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("categoryDetails", categoryDetails);
        shopDetail01Fragment.setArguments(bundle);
        shopDetail02Fragment.setArguments(bundle);
        mfragments.add(shopDetail01Fragment);
        mfragments.add(shopDetail02Fragment);
        //  mfragments.add(new ShopDetail03Fragment());
        viewpager.setAdapter(new ShopTabAdapter(getSupportFragmentManager(), mfragments, mDatas));
        viewpager.setCurrentItem(0);
        if ("0".equals(status)) {
            img = getResources().getDrawable(R.mipmap.icon_uncollect);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            tv_collect.setCompoundDrawables(null, img, null, null); //设置左图标
        } else {
            img = getResources().getDrawable(R.mipmap.icon_collect);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            tv_collect.setCompoundDrawables(null, img, null, null); //设置左图标
        }
    }

    @OnClick({R.id.backBtn, R.id.iv_header_right, R.id.tv_add, R.id.tv_buy, R.id.tv_BackHome, R.id.tv_collect})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.iv_header_right:
                break;
            case R.id.tv_add:

                dialog = new ShopDialogFragment();
                bundle=new Bundle();
                bundle.putSerializable("categoryDetails", categoryDetails);
                bundle.putString("keys","1");
               // bundle.putString("addressId",addressId);
                dialog.setArguments(bundle);
                dialog.show(this.getSupportFragmentManager(), "MakeFriendsFragment");
                break;
            case R.id.tv_buy:
                dialog = new ShopDialogFragment();
                bundle=new Bundle();
                bundle.putSerializable("categoryDetails", categoryDetails);
                bundle.putString("keys","2");
                //bundle.putString("addressId",addressId);
                dialog.setArguments(bundle);
                dialog.show(this.getSupportFragmentManager(), "MakeFriendsFragment");
                break;
            case R.id.tv_BackHome:
                toActivity(ShoppingListActivity.class);
                break;
            case R.id.tv_collect:
                if ("0".equals(status)) {
                    img = getResources().getDrawable(R.mipmap.icon_collect);
                    img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
                    tv_collect.setCompoundDrawables(null, img, null, null); //设置左图标
                    status = "1";

                } else if ("1".equals(status)) {
                    img = getResources().getDrawable(R.mipmap.icon_uncollect);
                    img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
                    tv_collect.setCompoundDrawables(null, img, null, null); //设置左图标
                    status = "0";
                }
                HttpMethods.getInstance().GetShopCollection(new MySubscriber<EmptyEntity>(this) {
                    @Override
                    public void onHttpCompleted(HttpResult<EmptyEntity> httpResult) {
                        if (httpResult.getCode() == 1) {
                            Log.d("TAG", httpResult.getMsg());
                            showToast(httpResult.getMsg());
                        } else {
                            showToast(httpResult.getMsg());
                        }
                    }

                    @Override
                    public void onHttpError(Throwable e) {

                    }
                }, ukey, id, status);
                break;
        }
    }


}
