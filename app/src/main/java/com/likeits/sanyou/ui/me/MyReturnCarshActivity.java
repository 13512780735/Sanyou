package com.likeits.sanyou.ui.me;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.CarshRetrunAdapter;
import com.likeits.sanyou.entity.CashBackEntity;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.LoaddingDialog;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyReturnCarshActivity extends BaseActivity {

    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_withdraw)
    TextView tv_withdraw;
    @BindView(R.id.tv_With_Cashback01)
    TextView tv_With_Cashback01;
    @BindView(R.id.tv_TotalConsumption01)
    TextView tv_TotalConsumption01;
    @BindView(R.id.tv_tvMonth)
    TextView tv_tvMonth;
    @BindView(R.id.tv_tvTime)
    TextView tv_tvTime;
    @BindView(R.id.myListView)
    PullToRefreshListView refreshListView;
    private ListView mListView;
    private ArrayList<CashBackEntity> listData;
    private CarshRetrunAdapter mAdapter;
    int page = 1;
    private String total;
    private LoaddingDialog loadingdialog;
    String amount = "0.0";
    String consumption = "0";
    private String cashbackmonth = null;
    private String eachday = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_return_carsh);
        ButterKnife.bind(this);
        loadingdialog = new LoaddingDialog(mContext);
        listData = new ArrayList<>();
        initData(page);
        loadingdialog.show();
        initView();
    }

    private void initData(int page) {
        String url = MyApiService.GetCashBack;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("page", String.valueOf(page));
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG", response);
                loadingdialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("code");
                    if ("1".equals(code)) {
                        total = object.optString("total");
                        amount = object.optJSONObject("data").optString("amount");
                        consumption = object.optJSONObject("data").optString("consumption");
                        eachday = object.optJSONObject("data").optString("eachday");
                        cashbackmonth = object.optJSONObject("data").optString("cashbackmonth");
                        Log.d("TAG", amount + consumption + cashbackmonth + eachday);
                        JSONArray array = object.optJSONObject("data").optJSONArray("cashback");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.optJSONObject(i);
                            CashBackEntity cashBackEntity = new CashBackEntity();
                            cashBackEntity.setGoods_id(obj.optString("goods_id"));
                            cashBackEntity.setGoods_name(obj.optString("goods_name"));
                            cashBackEntity.setMoney(obj.optString("money"));
                            cashBackEntity.setSn(obj.optString("sn"));
                            cashBackEntity.setTime(obj.optString("time"));
                            listData.add(cashBackEntity);
                        }
                        Log.d("TAG", listData + "");
                        tv_With_Cashback01.setText(amount);
                        tv_TotalConsumption01.setText(consumption);
                        tv_tvMonth.setText(cashbackmonth);
                        tv_tvTime.setText(eachday);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        tv_With_Cashback01.setText(amount);
                        tv_TotalConsumption01.setText(consumption);
                        tv_tvMonth.setText(cashbackmonth);
                        tv_tvTime.setText(eachday);
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
                refreshListView.onRefreshComplete();
            }
        });
    }

    private void initView() {
        tvHeader.setText("我的返现");
        mAdapter = new CarshRetrunAdapter(mContext, listData);
        refreshListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 获取当前时间并格式化
                String label = DateUtils.formatDateTime(
                        mContext, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                // 设置刷新文本说明(刷新过程中)
                refreshListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                refreshListView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                refreshListView.getLoadingLayoutProxy().setReleaseLabel("松开开始刷新数据");
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
                        "最后更新时间:" + label);
                refresh();

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
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
                    refreshListView.getLoadingLayoutProxy().setRefreshingLabel(
                            "正在加载更多数据");
                    refreshListView.getLoadingLayoutProxy().setPullLabel(
                            "上拉可以加载更多");
                    refreshListView.getLoadingLayoutProxy().setReleaseLabel(
                            "松开立即加载更多");

                } else {
                    // 上一次请求已经没有数据了
                    refreshListView.getLoadingLayoutProxy().setPullLabel(
                            "已经全部数据加载完毕...");
                    refreshListView.getLoadingLayoutProxy().setReleaseLabel(
                            "已经全部数据加载完毕...");
                }

            }
        });
    }

    private void refresh() {
        page = 1;
        mAdapter.addAll(listData, true);
        initData2(page);
        loadingdialog.show();
        mAdapter.notifyDataSetChanged();
    }

    private void initData2(int page) {
        String url = MyApiService.GetCashBack;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("page", String.valueOf(page));
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG", response);
                loadingdialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("code");
                    if ("1".equals(code)) {
                        total = object.optString("total");
                        amount = object.optJSONObject("data").optString("amount");
                        consumption = object.optJSONObject("data").optString("consumption");
                        eachday = object.optJSONObject("data").optString("eachday");
                        cashbackmonth = object.optJSONObject("data").optString("cashbackmonth");
                        Log.d("TAG", amount + consumption + cashbackmonth + eachday);
                        JSONArray array = object.optJSONObject("data").optJSONArray("cashback");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.optJSONObject(i);
                            CashBackEntity cashBackEntity = new CashBackEntity();
                            cashBackEntity.setGoods_id(obj.optString("goods_id"));
                            cashBackEntity.setGoods_name(obj.optString("goods_name"));
                            cashBackEntity.setMoney(obj.optString("money"));
                            cashBackEntity.setSn(obj.optString("sn"));
                            cashBackEntity.setTime(obj.optString("time"));
                            listData.add(cashBackEntity);
                        }
                        Log.d("TAG", listData + "");
                        tv_With_Cashback01.setText(amount);
                        tv_TotalConsumption01.setText(consumption);
                        tv_tvMonth.setText(cashbackmonth);
                        tv_tvTime.setText(eachday);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        tv_With_Cashback01.setText(amount);
                        tv_TotalConsumption01.setText(consumption);
                        tv_tvMonth.setText(cashbackmonth);
                        tv_tvTime.setText(eachday);
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
                refreshListView.onRefreshComplete();
            }
        });
    }

    @OnClick({R.id.backBtn, R.id.tv_withdraw})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_withdraw:
                if (listData.size() == 0) {
                    tv_withdraw.setEnabled(false);
                    showToast("沒金額可提！");
                    return;
                }
                Bundle bundel = new Bundle();
                bundel.putString("amount", amount);
                toActivity(WithdrawActivity.class, bundel);
                break;
        }
    }
}