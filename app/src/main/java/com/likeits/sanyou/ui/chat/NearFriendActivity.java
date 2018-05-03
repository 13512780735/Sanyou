package com.likeits.sanyou.ui.chat;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.NearFirendInfoEntity;
import com.likeits.sanyou.adapter.NearFriendAdapter01;
import com.likeits.sanyou.listeners.EndLessOnScrollListener;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.utils.UtilPreference;
import com.likeits.sanyou.view.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NearFriendActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
//    @BindView(R.id.recyclerView)
//    RecyclerView itemsRecyclerView;
//    @BindView(R.id.SwipeRefreshLayout)
//    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.swipeRecyclerView)
    SwipeRecyclerView mSwipeRefreshLayout;
    private EndLessOnScrollListener endLessOnScrollListener;
    private LinearLayoutManager linearLayoutManager;
    private NearFriendAdapter01 mNearFriendAdapter;
    private List<NearFirendInfoEntity> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_friend);
        ButterKnife.bind(this);
        data = new ArrayList<>();
        initView();
        initSwipeRefresh();
        refreshItems();
        Log.d("TAG212", Double.valueOf(UtilPreference.getStringValue(mContext, "lat")) + "");
        Log.d("TAG222", Double.valueOf(UtilPreference.getStringValue(mContext, "lon")) + "");
    }

    private void initSwipeRefresh() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mSwipeRefreshLayout.getSwipeRefreshLayout()
                    .setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mSwipeRefreshLayout.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        }
        mNearFriendAdapter = new NearFriendAdapter01(R.layout.layout_near_friend_listview_items, data);
        mSwipeRefreshLayout.setAdapter(mNearFriendAdapter);
        mSwipeRefreshLayout.setOnLoadListener(new SwipeRecyclerView.OnLoadListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      //  data.clear();
//                        for (int i = 0; i < pagerSize; i++) {
//                            data.add(String.valueOf(i));
//                        }
                        //refreshItems();
                        mSwipeRefreshLayout.complete();
                      //  mNearFriendAdapter.notifyDataSetChanged();

                    }
                }, 1000);

            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       // refreshItems();
//                        for (int i = 0; i < pagerSize; i++) {
//                            data.add(String.valueOf(i));
//                        }
//
//                        if(data.size() > 20){
//                            mSwipeRefreshLayout.onNoMore("-- the end --");
//                        }else {
//                            mSwipeRefreshLayout.stopLoadingMore();
//                            adapter.notifyDataSetChanged();
//                        }
                        //  mSwipeRefreshLayout.stopLoadingMore();
                        mSwipeRefreshLayout.onNoMore("-- 没有更多数据了 --");
                        mSwipeRefreshLayout.complete();
                      //  mNearFriendAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        //设置自动下拉刷新，切记要在recyclerView.setOnLoadListener()之后调用
        //因为在没有设置监听接口的情况下，setRefreshing(true),调用不到OnLoadListener
        mSwipeRefreshLayout.setRefreshing(false);
    }

//    private void initSwipeRefresh() {
//        mNearFriendAdapter = new NearFriendAdapter();
//        mNearFriendAdapter.setBaseActivity(NearFriendActivity.this);
//        linearLayoutManager = new LinearLayoutManager(mContext);
//        itemsRecyclerView.setLayoutManager(linearLayoutManager);
//        itemsRecyclerView.setAdapter(mNearFriendAdapter);
//
//        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
//                R.color.holo_orange_light, R.color.holo_red_light);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Refresh items
//                refreshItems();
//
//            }
//        });
//
//        endLessOnScrollListener = new EndLessOnScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore() {
//                refreshItems();
//            }
//        };
//        itemsRecyclerView.addOnScrollListener(endLessOnScrollListener);
//    }

    private void refreshItems() {
        HttpMethods.getInstance().GetMemberDistance(new MySubscriber<ArrayList<NearFirendInfoEntity>>(this) {

            @Override
            public void onHttpCompleted(HttpResult<ArrayList<NearFirendInfoEntity>> httpResult) {
                onRefreshComplete();
                if (httpResult.getCode() == 1) {
                    if (httpResult.getData() != null && httpResult.getData().size() > 0) {
                        mNearFriendAdapter.setNewData(httpResult.getData());
                        //  data.add(httpResult.getData());
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
        }, ukey, Double.valueOf(UtilPreference.getStringValue(mContext, "lat")), Double.valueOf(UtilPreference.getStringValue(mContext, "lon")));
    }

    private void initView() {
        tvHeader.setText("附近钓友");

    }


    @OnClick(R.id.backBtn)
    public void Onclick(View v) {
        onBackPressed();
    }

    private void onRefreshComplete() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
