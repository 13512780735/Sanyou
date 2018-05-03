package com.likeits.sanyou.ui.information;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hyphenate.chat.EMClient;
import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.InformationDetailsCommentAdapter;
import com.likeits.sanyou.entity.InformaitonDetailsInfoEntity;
import com.likeits.sanyou.entity.InformationDetailsCommentInfoEntity;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.LoaddingDialog;
import com.likeits.sanyou.utils.ToastUtil;
import com.likeits.sanyou.utils.UtilPreference;
import com.likeits.sanyou.utils.richtext.RichText;
import com.likeits.sanyou.view.CircleImageView;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InformationDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.iv_header_left)
    ImageView ivLeft;
    @BindView(R.id.iv_header_right)
    ImageView ivRight;
    @BindView(R.id.information_details_listview)
    PullToRefreshListView mPullToRefreshListView;
    @BindView(R.id.ed_input)
    EditText ed_input;
    @BindView(R.id.tv_send)
    TextView tv_send;
    private View mLoadingLayout;
    ListView mListView;
    private TextView tvTitle, tv_tvName, tv_tvTime, tv_add_friend, tv_like;
    private CircleImageView ivAvatar;
    private ImageView iv_cover;
    private InformationDetailsCommentAdapter mAdapter;
    private String id;
    private List<InformationDetailsCommentInfoEntity> commentData;
    private LoaddingDialog loaddingDialog;
    private String easemob_id;
    private String total;
    int page = 1;
    private InformationDetailsCommentInfoEntity informationDetailsCommentInfoEntity;
    private RichText tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_details);
        ButterKnife.bind(this);
        loaddingDialog = new LoaddingDialog(this);
        id = getIntent().getStringExtra("id");
        commentData = new ArrayList<>();
        initView();
        initData();//资讯详情数据
        initData2(1);//资讯评论数据
        loaddingDialog.show();
    }

    private void initData2(int page) {
        String url = MyApiService.BASE_URL + "?s=/api/news/getcomment";
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("page", String.valueOf(page));
        params.put("id", id);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                loaddingDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.optString("code");
                    if ("1".equals(code)) {
                        total = obj.optString("total");
                        JSONArray array = obj.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);
                            informationDetailsCommentInfoEntity = new InformationDetailsCommentInfoEntity();
                            informationDetailsCommentInfoEntity.setId(object.optString("id"));
                            informationDetailsCommentInfoEntity.setUid(object.optString("uid"));
                            informationDetailsCommentInfoEntity.setEasemob_id(object.optString("easemob_id"));
                            informationDetailsCommentInfoEntity.setNickname(object.optString("nickname"));
                            informationDetailsCommentInfoEntity.setHeadimg(object.optString("headimg"));
                            informationDetailsCommentInfoEntity.setContent(object.optString("content"));
                            informationDetailsCommentInfoEntity.setStatus(object.optString("status"));
                            informationDetailsCommentInfoEntity.setFloor(object.optString("floor"));
                            informationDetailsCommentInfoEntity.setInterval(object.optString("interval"));
                            commentData.add(informationDetailsCommentInfoEntity);
                        }
                        mAdapter.notifyDataSetChanged();
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
                mPullToRefreshListView.onRefreshComplete();
            }
        });

    }

    private void initData() {
        HttpMethods.getInstance().GetNewsTails(new MySubscriber<InformaitonDetailsInfoEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<InformaitonDetailsInfoEntity> httpResult) {

                if (httpResult.getCode() == 1) {
                    easemob_id = httpResult.getData().getEasemob_id();
                    Log.d("TAG888", httpResult.getData().getTitle());
                    tvTitle.setText(httpResult.getData().getTitle());
                    tv_tvName.setText(httpResult.getData().getAuthor());
                    tv_tvTime.setText(httpResult.getData().getInterval());
                    tv_like.setText(httpResult.getData().getLikes());
                    tv_content.setRichText(httpResult.getData().getContent());
                    ImageLoader.getInstance().displayImage(httpResult.getData().getHeadimg(), ivAvatar);
                    ImageLoader.getInstance().displayImage(httpResult.getData().getCover(), iv_cover);

                } else {
                    showToast(httpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey, id);
    }

    private void refresh() {
        mAdapter.addAll(commentData, true);
        initData2(1);
        loaddingDialog.show();
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        tvHeader.setText("资讯详情");
        ivLeft.setImageResource(R.mipmap.icon_red_back);
     //   ivRight.setImageResource(R.mipmap.nav_settings_gray);
        // simpleAdapter = new SimpleAdapter(mContext, dataList, R.layout.layout_imformation_details_listview_items, from, to);
        mListView = mPullToRefreshListView.getRefreshableView();
        mLoadingLayout = getLayoutInflater().inflate(R.layout.layout_informationdetails_headerview, null);
        tvTitle = (TextView) mLoadingLayout.findViewById(R.id.tv_title);
        tv_tvName = (TextView) mLoadingLayout.findViewById(R.id.tv_tvName);
        tv_tvTime = (TextView) mLoadingLayout.findViewById(R.id.tv_tvTime);
        tv_add_friend = (TextView) mLoadingLayout.findViewById(R.id.tv_add_friend);
        tv_like = (TextView) mLoadingLayout.findViewById(R.id.tv_like);
        tv_content = (RichText) mLoadingLayout.findViewById(R.id.tv_content);
        ivAvatar = (CircleImageView) mLoadingLayout.findViewById(R.id.user_avatar);
        iv_cover = (ImageView) mLoadingLayout.findViewById(R.id.iv_cover);
        tvTitle = (TextView) mLoadingLayout.findViewById(R.id.tv_title);
        tvTitle = (TextView) mLoadingLayout.findViewById(R.id.tv_title);
        mListView.addHeaderView(mLoadingLayout);
        tv_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (easemob_id.equals(UtilPreference.getStringValue(mContext, "easemob_id"))) {
                    ToastUtil.showS(mContext, "不能添加自己");
                } else {
                    new Thread(new Runnable() {
                        public void run() {

                            try {
                                String s = getResources().getString(R.string.Add_a_friend);
                                EMClient.getInstance().contactManager().addContact(easemob_id, s);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        //progressDialog.dismiss();
                                        String s1 = getResources().getString(R.string.send_successful);
                                        Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                                    }
                                });
                            } catch (final Exception e) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        // progressDialog.dismiss();
                                        String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                                        Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });
        mAdapter = new InformationDetailsCommentAdapter(mContext, commentData);
        mPullToRefreshListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 获取当前时间并格式化
                String label = DateUtils.formatDateTime(
                        mContext, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                // 设置刷新文本说明(刷新过程中)
                mPullToRefreshListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                mPullToRefreshListView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                mPullToRefreshListView.getLoadingLayoutProxy().setReleaseLabel("松开开始刷新数据");
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
                        "最后更新时间:" + label);
                refresh();

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                page++;
                initData3(page);
                int totalPage = Integer.valueOf(total) % 10;
                if (totalPage == 0) {
                    totalPage = Integer.valueOf(total) / 10;
                } else {
                    totalPage = Integer.valueOf(total) / 10 + 1;
                }
                if (page <= totalPage) {// 上一次请求有数据
                    // 自定义上拉header内容
                    mPullToRefreshListView.getLoadingLayoutProxy().setRefreshingLabel(
                            "正在加载更多数据");
                    mPullToRefreshListView.getLoadingLayoutProxy().setPullLabel(
                            "上拉可以加载更多");
                    mPullToRefreshListView.getLoadingLayoutProxy().setReleaseLabel(
                            "松开立即加载更多");

                } else {
                    // 上一次请求已经没有数据了
                    mPullToRefreshListView.getLoadingLayoutProxy().setPullLabel(
                            "已经全部数据加载完毕...");
                    mPullToRefreshListView.getLoadingLayoutProxy().setReleaseLabel(
                            "已经全部数据加载完毕...");
                }

            }
        });
    }

    private void initData3(int page) {
        String url = MyApiService.BASE_URL + "?s=/api/news/getcomment";
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("page", String.valueOf(page));
        params.put("id", id);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                loaddingDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.optString("code");
                    if ("1".equals(code)) {
                        total = obj.optString("total");
                        JSONArray array = obj.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);
                            informationDetailsCommentInfoEntity = new InformationDetailsCommentInfoEntity();
                            informationDetailsCommentInfoEntity.setId(object.optString("id"));
                            informationDetailsCommentInfoEntity.setUid(object.optString("uid"));
                            informationDetailsCommentInfoEntity.setEasemob_id(object.optString("easemob_id"));
                            informationDetailsCommentInfoEntity.setNickname(object.optString("nickname"));
                            informationDetailsCommentInfoEntity.setHeadimg(object.optString("headimg"));
                            informationDetailsCommentInfoEntity.setContent(object.optString("content"));
                            informationDetailsCommentInfoEntity.setStatus(object.optString("status"));
                            informationDetailsCommentInfoEntity.setFloor(object.optString("floor"));
                            informationDetailsCommentInfoEntity.setInterval(object.optString("interval"));
                            commentData.add(informationDetailsCommentInfoEntity);
                        }
                        mAdapter.notifyDataSetChanged();
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
                mPullToRefreshListView.onRefreshComplete();
            }
        });
    }


    @OnClick({R.id.iv_header_left, R.id.iv_header_right, R.id.tv_send})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_left:
                onBackPressed();
                break;
            case R.id.iv_header_right:
                break;
            case R.id.tv_send:
                send();
                break;
        }
    }

    private void send() {
        final String content = ed_input.getText().toString().trim();
        HttpMethods.getInstance().GetAddComment(new MySubscriber<ArrayList<InformationDetailsCommentInfoEntity>>(this) {
            @Override
            public void onHttpCompleted(HttpResult<ArrayList<InformationDetailsCommentInfoEntity>> httpResult) {
                if (httpResult.getCode() == 1) {
                    ed_input.getText().clear();
                    showToast("发布成功");
                    informationDetailsCommentInfoEntity = new InformationDetailsCommentInfoEntity();
                    informationDetailsCommentInfoEntity.setUid(httpResult.getData().get(0).getUid());
                    informationDetailsCommentInfoEntity.setId(httpResult.getData().get(0).getId());
                    informationDetailsCommentInfoEntity.setEasemob_id(httpResult.getData().get(0).getEasemob_id());
                    informationDetailsCommentInfoEntity.setNickname(httpResult.getData().get(0).getNickname());
                    informationDetailsCommentInfoEntity.setFloor(httpResult.getData().get(0).getFloor());
                    informationDetailsCommentInfoEntity.setHeadimg(httpResult.getData().get(0).getHeadimg());
                    informationDetailsCommentInfoEntity.setContent(httpResult.getData().get(0).getContent());
                    informationDetailsCommentInfoEntity.setInterval(httpResult.getData().get(0).getInterval());
                    informationDetailsCommentInfoEntity.setStatus(httpResult.getData().get(0).getStatus());
                    commentData.add(informationDetailsCommentInfoEntity);
                    mAdapter.notifyDataSetChanged();
                } else {
                    showToast(httpResult.getMsg());
                }

            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey, id, content);
    }

}
