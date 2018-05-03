package com.likeits.sanyou.ui.shop.fargment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.likeits.sanyou.R;
import com.likeits.sanyou.ui.base.BaseFragment;
import com.likeits.sanyou.view.MyListview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopDetail03Fragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ScrollView> {


    private PullToRefreshScrollView mPullToRefreshScrollView;
    private MyListview mListView;
    private String[] iconName = {"用戶...", "用戶...", "用戶...", "用戶...", "用戶...", "用戶..."};
    private List<Map<String, Object>> dataList;
    private SimpleAdapter simpleAdapter;
    @Override
    protected int setContentView() {
        return R.layout.fragment_shop_detail03;
    }

    @Override
    protected void lazyLoad() {
        initView();

    }

    private void initView() {
        mPullToRefreshScrollView = findViewById(R.id.ll_shop_comment_scrollview);
        mListView = findViewById(R.id.ll_shop_comment_listview);
        dataList = new ArrayList<Map<String, Object>>();
        getData();
        String[] from = {"name"};
        final int[] to = {R.id.tv_tvName};
        simpleAdapter = new SimpleAdapter(getActivity(), dataList, R.layout.layout_shop_comment_listview_items, from, to);
        //配置适配器
        mListView.setAdapter(simpleAdapter);
    }
    private List<Map<String, Object>> getData() {
        for (int i = 0; i < iconName.length; i++) {
            Log.d("TAG", "" + iconName.length);
            Map<String, Object> map = new HashMap<String, Object>();
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
}
