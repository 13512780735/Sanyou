package com.likeits.sanyou.ui.me;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.CollectAdapter;
import com.likeits.sanyou.entity.CollectInfoEntity;
import com.likeits.sanyou.entity.UserInfoEntity;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.EmptyEntity;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyCollectActivity extends BaseActivity implements CollectAdapter.ModifyCountInterface {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.myCollect_listView)
    ListView mListView;
    List<CollectInfoEntity> collectData;
    private CollectAdapter mAdapter;
    private boolean flag = false;
    private String id;
    private UserInfoEntity mUserInfoEntity;
    private String collect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        ButterKnife.bind(this);
        mUserInfoEntity = (UserInfoEntity) getIntent().getExtras().getSerializable("mUserInfoEntity");
        collectData = new ArrayList<>();
        initView();
        initData();

    }

    private void initData() {
        HttpMethods.getInstance().GetShopCollectionList(new MySubscriber<ArrayList<CollectInfoEntity>>(this) {
            @Override
            public void onHttpCompleted(HttpResult<ArrayList<CollectInfoEntity>> httpResult) {
                if (httpResult.getCode() == 1) {
                    collectData = httpResult.getData();
                    Log.d("TAG", collectData.get(0).getGoods_name());
                    // mAdapter.notifyDataSetChanged();
                    mAdapter = new CollectAdapter(MyCollectActivity.this, collectData);
                    mAdapter.setModifyCountInterface(MyCollectActivity.this);
                    mListView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey);
    }

    private void initView() {
        tvHeader.setText("我的收藏夹");
        tvRight.setText("编辑");
        collect = mUserInfoEntity.getCollections();
        if("0".equals(collect)){
            showToast("暂未有商品收藏");
        }
    }


    @OnClick({R.id.backBtn, R.id.tv_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_right:
                flag = !flag;
                if ("0".equals(collect)) {
                    tvRight.setEnabled(false);
                    ToastUtil.showS(mContext, "暂未有商品收藏");
                } else {
                    if (flag) {
                        tvRight.setText("完成");
                        mAdapter.isShow(false);
                    } else {
                        tvRight.setText("编辑");
                        mAdapter.isShow(true);
                    }
                }
                break;
        }

    }


    @Override
    public void childDelete(int position) {
        id = collectData.get(position).getId();
        remove();
        collectData.remove(position);
        mAdapter.notifyDataSetChanged();

    }

    private void remove() {
        HttpMethods.getInstance().GetShopCollection(new MySubscriber<EmptyEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<EmptyEntity> httpResult) {
                if (httpResult.getCode() == 1) {
                    showToast("删除成功");

                } else {
                    showToast("删除失败");
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey, id, "0");
    }
}
