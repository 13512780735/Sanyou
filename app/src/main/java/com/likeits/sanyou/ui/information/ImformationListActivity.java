package com.likeits.sanyou.ui.information;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.ImformationInfoEntity;
import com.likeits.sanyou.adapter.ImformationListAdapter;
import com.likeits.sanyou.listeners.EndLessOnScrollListener;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImformationListActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.iv_header_left)
    ImageView ivLeft;
    @BindView(R.id.iv_header_right)
    ImageView ivRight;
    @BindView(R.id.iv_edit)
    ImageView iv_edit;
    @BindView(R.id.recyclerView)
    RecyclerView itemsRecyclerView;
    @BindView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    public int page = 1;
    private ImformationListAdapter mImformationListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private EndLessOnScrollListener endLessOnScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imformation_list);
        ButterKnife.bind(this);
        initView();
        initSwipeRefresh();
        refreshItems();
    }

    private void initSwipeRefresh() {
        mImformationListAdapter = new ImformationListAdapter();
        mImformationListAdapter.setBaseActivity(ImformationListActivity.this);
        linearLayoutManager = new LinearLayoutManager(mContext);
        itemsRecyclerView.setLayoutManager(linearLayoutManager);
        itemsRecyclerView.setAdapter(mImformationListAdapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
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
        HttpMethods.getInstance().GetNewsList(new MySubscriber<ArrayList<ImformationInfoEntity>>(this) {

            @Override
            public void onHttpCompleted(HttpResult<ArrayList<ImformationInfoEntity>> httpResult) {
                onRefreshComplete();
                if (httpResult.getCode() == 1) {
                    if (httpResult.getData() != null&& httpResult.getData().size() > 0) {
                        if (page == 1) {
                            mImformationListAdapter.setDatas(httpResult.getData());
                        } else {
                            mImformationListAdapter.addDatas(httpResult.getData());
                        }
                    } else {
                        showToast("没有更多数据了");
                    }
                } else {
                    showToast("没有更多数据了");
                }
            }

            @Override
            public void onHttpError(Throwable e) {
                onRefreshComplete();
            }
        }, ukey, String.valueOf(page), "1");
    }

    private void initView() {
        tvHeader.setText("资讯");
        ivLeft.setImageResource(R.mipmap.icon_red_back);
       // ivRight.setImageResource(R.mipmap.nav_settings_gray);
    }


    @OnClick({R.id.iv_header_left, R.id.iv_header_right, R.id.iv_edit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_left:
                onBackPressed();
                break;
            case R.id.iv_header_right:
                break;
            case R.id.iv_edit:
                toActivity(SendImformationActivity.class);
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


