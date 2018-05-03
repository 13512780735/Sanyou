package com.likeits.sanyou.ui.me;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.likeits.sanyou.R;
import com.likeits.sanyou.ui.base.Container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyFootprintActivity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.myFootPrint_listView)
    ListView mListView;

    /**
     * 模拟数据
     *
     * @param savedInstanceState
     */
    private List<Map<String, Object>> dataList02;
    // 图片封装为一个数组
    private int[] icon = {R.mipmap.icon_test04, R.mipmap.icon_test04, R.mipmap.icon_test04};
    private String[] title = {"顶点钓鱼灯夜钓灯，紫光蓝光黄白四光源大容量钓鱼灯", "顶点钓鱼灯夜钓灯，紫光蓝光黄白四光源大容量钓鱼灯", "顶点钓鱼灯夜钓灯，紫光蓝光黄白四光源大容量钓鱼灯"};
    private String[] price = {"¥ 680.00", "¥ 680.00", "¥ 680.00"};
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_footprint);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvHeader.setText("足迹");
        tvRight.setText("编辑");
        dataList = new ArrayList<Map<String, Object>>();
        getData();
        String[] from = {"img", "title", "price"};
        int[] to = {R.id.iv_avatar, R.id.tv_title,
                R.id.tv_price};
        simpleAdapter = new SimpleAdapter(mContext, dataList, R.layout.layout_myfoorprint_listview_items, from, to);
        //配置适配器
        mListView.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
    }

    private List<Map<String, Object>> getData() {
        for (int i = 0; i < icon.length; i++) {
            Log.d("TAG", "" + icon.length);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", icon[i]);
            map.put("title", title[i]);
            map.put("price", price[i]);
            dataList.add(map);
        }
        return dataList;
    }

    @OnClick({R.id.backBtn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }
}
