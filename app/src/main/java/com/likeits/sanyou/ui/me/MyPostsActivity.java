package com.likeits.sanyou.ui.me;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.MyPostsAdapter;
import com.likeits.sanyou.adapter.ReceivingAddressAdapter;
import com.likeits.sanyou.entity.CenterPosterBean;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.network.entity.EmptyEntity;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseActivity;
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

public class MyPostsActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.mListView)
    PullToRefreshListView mListView;
    int page = 1;
    private List<CenterPosterBean> mData;
    private MyPostsAdapter mAdapter;
    private String total;
    private LoaddingDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
        ButterKnife.bind(this);
        loading=new LoaddingDialog(mContext);
        mData = new ArrayList<>();
        initView();
       // initData(page);

    }

    private void initData(int page) {
        String url= MyApiService.MyListNews;
        RequestParams params=new RequestParams();
        params.put("ukey",ukey);
        params.put("page",String.valueOf(page));
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                loading.dismiss();
                try {
                    JSONObject object=new JSONObject(response);
                    String code=object.optString("code");
                    if("1".equals(code)){
                        total=object.optString("total");
                        JSONArray array=object.optJSONArray("data");
                        for(int i=0;i<array.length();i++){
                            JSONObject obj=array.optJSONObject(i);
                            CenterPosterBean centerPosterBean= JSON.parseObject(obj.toString(),CenterPosterBean.class);
                            mData.add(centerPosterBean);
                        }
                        Log.d("TAG",mData+"");
                        mAdapter.notifyDataSetChanged();
                    }else{
                        showToast(object.optString("message"));
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
                loading.dismiss();
                mListView.onRefreshComplete();
            }
        });

    }

    private void initView() {
        tvHeader.setText("我的帖子");
        mAdapter = new MyPostsAdapter(mContext, mData);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 获取当前时间并格式化
                String label = DateUtils.formatDateTime(
                        mContext, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                // 设置刷新文本说明(刷新过程中)
                mListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                mListView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                mListView.getLoadingLayoutProxy().setReleaseLabel("松开开始刷新数据");
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
                    mListView.getLoadingLayoutProxy().setRefreshingLabel(
                            "正在加载更多数据");
                    mListView.getLoadingLayoutProxy().setPullLabel(
                            "上拉可以加载更多");
                    mListView.getLoadingLayoutProxy().setReleaseLabel(
                            "松开立即加载更多");

                } else {
                    // 上一次请求已经没有数据了
                    mListView.getLoadingLayoutProxy().setPullLabel(
                            "已经全部数据加载完毕...");
                    mListView.getLoadingLayoutProxy().setReleaseLabel(
                            "已经全部数据加载完毕...");
                }

            }
        });
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(mContext, ShopDetailsActivity.class);
//                intent.putExtra("id", mData.get(position).getId());
//                startActivity(intent);
//            }
//        });
        mAdapter.setOnDelClickListener(new ReceivingAddressAdapter.onDelClickListener() {
            @Override
            public void onDelClick(int i) {
                HttpMethods.getInstance().DelNews(new MySubscriber<EmptyEntity>(MyPostsActivity.this) {
                    @Override
                    public void onHttpCompleted(HttpResult<EmptyEntity> httpResult) {
                        if(httpResult.getCode()==1){
                            showToast(httpResult.getMsg());
                            refresh();
                        }else{
                            showToast(httpResult.getMsg());
                        }
                    }

                    @Override
                    public void onHttpError(Throwable e) {

                    }
                },ukey,mData.get(i).getId());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        mAdapter.addAll(mData, true);
        initData(1);
        loading.show();
        mAdapter.notifyDataSetChanged();
    }

    private void initData2(int page) {
        String url= MyApiService.MyListNews;
        RequestParams params=new RequestParams();
        params.put("ukey",ukey);
        params.put("page",String.valueOf(page));
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    String code=object.optString("code");
                    if("1".equals(code)){
                        total=object.optString("total");
                        JSONArray array=object.optJSONArray("data");
                        for(int i=0;i<array.length();i++){
                            JSONObject obj=array.optJSONObject(i);
                            CenterPosterBean centerPosterBean= JSON.parseObject(obj.toString(),CenterPosterBean.class);
                            mData.add(centerPosterBean);
                        }
                        Log.d("TAG",mData+"");
                        mAdapter.notifyDataSetChanged();
                    }else{
                        showToast(object.optString("message"));
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
                mListView.onRefreshComplete();
            }
        });
    }

    @OnClick({R.id.iv_header_left, R.id.iv_edit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_left:
                onBackPressed();
                break;
            case R.id.iv_edit:
                Bundle bundle=new Bundle();
                bundle.putString("keys","2");
                toActivity(PostsActivity.class,bundle);
                break;
        }
    }
}
