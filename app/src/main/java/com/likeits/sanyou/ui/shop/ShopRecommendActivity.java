package com.likeits.sanyou.ui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.CategoryEntity;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.SearchInfoActivity;
import com.likeits.sanyou.ui.base.BaseFragmentActivity;
import com.likeits.sanyou.ui.me.MyShoppingCartActivity;
import com.likeits.sanyou.ui.shop.fargment.Shop01Fragment;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.LoaddingDialog;
import com.likeits.sanyou.utils.UtilPreference;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopRecommendActivity extends BaseFragmentActivity {

    @BindView(R.id.sliding_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private String ukey;
    private LoaddingDialog loadingDialog;
    private List<CategoryEntity> categoryData;
    private CategoryEntity categoryEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_recommend);
        ButterKnife.bind(this);
        loadingDialog = new LoaddingDialog(this);
        ukey = UtilPreference.getStringValue(this, "ukey");
        categoryData = new ArrayList<>();
        categoryEntity = new CategoryEntity();
        categoryEntity.setId("0");
        categoryEntity.setTitle("全部");
        categoryData.add(categoryEntity);
        initData();//商品分类
        loadingDialog.show();

    }

    private void initData() {
        String url = MyApiService.GetCategory;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                loadingDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("code");
                    if ("1".equals(code)) {
                        JSONArray array = object.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.optJSONObject(i);
                            categoryEntity =new CategoryEntity();
                            categoryEntity.setId(obj.optString("id"));
                            categoryEntity.setTitle(obj.optString("title"));
                            categoryData.add(categoryEntity);
                        }
                        initView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                initView();
            }

        });

    }

    private void initView() {
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(viewpager);
        final List<Fragment> mfragments = new ArrayList<>();
        for (int i = 0; i < categoryData.size(); i++) {
            Shop01Fragment fragment = Shop01Fragment.newInstance(categoryData.get(i));
            Bundle bundle = new Bundle();
            bundle.putString("url", categoryData.get(i).getId());
            fragment.setArguments(bundle);
            mfragments.add(fragment);
        }
        for (int i = 0; i < categoryData.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(categoryData.get(i).getTitle()));//添加tab选项
        }
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mfragments.get(position);
            }

            @Override
            public int getCount() {
                return mfragments.size();
            }

            //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
            @Override
            public CharSequence getPageTitle(int position) {
                return categoryData.get(position).getTitle();
            }
        };
        viewpager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(viewpager);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        viewpager.setCurrentItem(0);
    }

    @OnClick({R.id.backBtn, R.id.message_img, R.id.search_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.search_layout:
                Intent intent1 = new Intent(ShopRecommendActivity.this, SearchInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.message_img:
                Intent intent = new Intent(ShopRecommendActivity.this, MyShoppingCartActivity.class);
                startActivity(intent);
                break;
        }
    }
}
