package com.likeits.sanyou.ui.shop;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.ShopIndentAdapter;
import com.likeits.sanyou.entity.ShopIndentEntity;
import com.likeits.sanyou.listeners.EndLessOnScrollListener;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.ui.me.MyShoppingCartActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopIndentActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.iv_header_left)
    ImageView ivLeft;
    @BindView(R.id.iv_header_right)
    ImageView ivRight;
    @BindView(R.id.recyclerView)
    RecyclerView itemsRecyclerView;
    @BindView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    public int page = 1;
    private ShopIndentAdapter mShopIndentAdapter;
    private LinearLayoutManager linearLayoutManager;
    private EndLessOnScrollListener endLessOnScrollListener;
    private String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving);
        ButterKnife.bind(this);
        Bundle bundle=getIntent().getExtras();
        status=bundle.getString("status");
        initView();
        initSwipeRefresh();
        refreshItems();
    }

    private void initView() {
        if("".equals(status)){
            tvHeader.setText("全部订单");
        }else if("0".equals(status)){
            tvHeader.setText("待付款");
        }else if("1".equals(status)){
            tvHeader.setText("待发货");
        }else if("2".equals(status)){
            tvHeader.setText("待收货");
        }else if("3".equals(status)){
            tvHeader.setText("待评价");
        }
        ivLeft.setImageResource(R.mipmap.icon_red_back);
        ivRight.setImageResource(R.mipmap.me_icon_shopping_cart);
    }

    private void initSwipeRefresh() {
        mShopIndentAdapter = new ShopIndentAdapter();
        mShopIndentAdapter.setBaseActivity(ShopIndentActivity.this);
        linearLayoutManager = new LinearLayoutManager(mContext);
        itemsRecyclerView.setLayoutManager(linearLayoutManager);
        itemsRecyclerView.setAdapter(mShopIndentAdapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                refreshItems();

            }
        });

        endLessOnScrollListener = new EndLessOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                page++;
                refreshItems();
            }
        };
        itemsRecyclerView.addOnScrollListener(endLessOnScrollListener);
    }

    private void refreshItems() {
        HttpMethods.getInstance().GetShop(new MySubscriber<ArrayList<ShopIndentEntity>>(this) {

            @Override
            public void onHttpCompleted(HttpResult<ArrayList<ShopIndentEntity>> httpResult) {
                onRefreshComplete();
                if (httpResult.getCode() == 1) {
                    if (httpResult.getData() != null&& httpResult.getData().size() > 0) {
                        if (page == 1) {
                            mShopIndentAdapter.setDatas(httpResult.getData());
                        } else {
                            mShopIndentAdapter.addDatas(httpResult.getData());
                        }
                    } else {
                        showToast("没有更多数据了");
                    }
                } else {
                    // showToast("没有更多数据了");
                }
            }

            @Override
            public void onHttpError(Throwable e) {
                onRefreshComplete();
            }
        }, ukey,status,String.valueOf(page));
    }


    @OnClick({R.id.iv_header_left, R.id.iv_header_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_left:
                onBackPressed();
                break;
            case R.id.iv_header_right:
                toActivity(MyShoppingCartActivity.class);
                break;
        }
    }
    private void onRefreshComplete() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (endLessOnScrollListener != null) {
            endLessOnScrollListener.onLoadMoreFinish();
        }
    }
}
