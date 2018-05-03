package com.likeits.sanyou.ui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.CategoryInfoEntity;
import com.likeits.sanyou.adapter.CategoryListAdapter;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.base.Container;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.LoaddingDialog;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TodayRecommendActivity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    private List<CategoryInfoEntity> mData;
    private LoaddingDialog loadingdialog;
    private String total;
    private CategoryInfoEntity categoryInfoEntity;
    private PullToRefreshGridView mGridView;
    private CategoryListAdapter mCategoryListAdapter;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_recommend);
        ButterKnife.bind(this);
        loadingdialog = new LoaddingDialog(mContext);
        mData = new ArrayList<>();
        initView();
        initData(1);
        loadingdialog.show();
    }

    private void initData(int page) {
        String url = MyApiService.GetShopHomeList;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("page", String.valueOf(page));
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                loadingdialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("code");
                    if ("1".equals(code)) {
                        total = object.optString("total");
                        JSONArray array = object.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.optJSONObject(i);
                            categoryInfoEntity = JSON.parseObject(obj.toString(),CategoryInfoEntity.class);
//                            categoryInfoEntity = new CategoryInfoEntity();
//                            categoryInfoEntity.setId(obj.optString("id"));
//                            categoryInfoEntity.setGoods_name(obj.optString("goods_name"));
//                            categoryInfoEntity.setPath(obj.optString("path"));
//                            categoryInfoEntity.setMoney_need(obj.optString("money_need"));
//                            categoryInfoEntity.setSell_num(obj.optString("sell_num"));
                            mData.add(categoryInfoEntity);
                        }
                        mCategoryListAdapter.notifyDataSetChanged();
                    } else {
                        showToast("没有更多数据了");
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
                loadingdialog.dismiss();
                mGridView.onRefreshComplete();
            }
        });
    }

    private void initView() {
        tvHeader.setText("商品推荐");
        mGridView = (PullToRefreshGridView) findViewById(R.id.shopList_gridView);
        mCategoryListAdapter = new CategoryListAdapter(mContext, mData);
        mGridView.setAdapter(mCategoryListAdapter);
        mCategoryListAdapter.notifyDataSetChanged();
        mGridView.setMode(PullToRefreshBase.Mode.BOTH);
        mGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                // 获取当前时间并格式化
                String label = DateUtils.formatDateTime(
                        mContext, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                // 设置刷新文本说明(刷新过程中)
                mGridView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                mGridView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                mGridView.getLoadingLayoutProxy().setReleaseLabel("松开开始刷新数据");
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
                        "最后更新时间:" + label);
                refresh();

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                page++;
                initData2(page);
                int totalPage = Integer.valueOf(total) % 10;
                if (totalPage == 0) {
                    totalPage = Integer.valueOf(total) / 10;
                } else {
                    totalPage = Integer.valueOf(total) / 10 + 1;
                }
                if (page <= totalPage) {// 上一次请求有数据
                    // 自定义上拉header内容
                    mGridView.getLoadingLayoutProxy().setRefreshingLabel(
                            "正在加载更多数据");
                    mGridView.getLoadingLayoutProxy().setPullLabel(
                            "上拉可以加载更多");
                    mGridView.getLoadingLayoutProxy().setReleaseLabel(
                            "松开立即加载更多");

                } else {
                    // 上一次请求已经没有数据了
                    mGridView.getLoadingLayoutProxy().setPullLabel(
                            "已经全部数据加载完毕...");
                    mGridView.getLoadingLayoutProxy().setReleaseLabel(
                            "已经全部数据加载完毕...");
                }

            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ShopDetailsActivity.class);
                intent.putExtra("id", mData.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void initData2(int page) {
        String url = MyApiService.GetShopHomeList;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("page", String.valueOf(page));
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                loadingdialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("code");
                    if ("1".equals(code)) {
                        total = object.optString("total");
                        JSONArray array = object.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.optJSONObject(i);
                            categoryInfoEntity = JSON.parseObject(obj.toString(),CategoryInfoEntity.class);
//                            categoryInfoEntity = new CategoryInfoEntity();
//                            categoryInfoEntity.setId(obj.optString("id"));
//                            categoryInfoEntity.setGoods_name(obj.optString("goods_name"));
//                            categoryInfoEntity.setPath(obj.optString("path"));
//                            categoryInfoEntity.setMoney_need(obj.optString("money_need"));
//                            categoryInfoEntity.setSell_num(obj.optString("sell_num"));
                            mData.add(categoryInfoEntity);
                        }
                        mCategoryListAdapter.notifyDataSetChanged();
                    } else {
                        showToast("没有更多数据了");
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
                loadingdialog.dismiss();
                mGridView.onRefreshComplete();
            }
        });
    }

    private void refresh() {
        mCategoryListAdapter.addAll(mData, true);
        initData2(1);
        loadingdialog.show();
        mCategoryListAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.backBtn)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }
}
