package com.likeits.sanyou.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.UserInfoEntity;
import com.likeits.sanyou.message.utils.UserInfoCacheSvc;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.base.BaseFragment;
import com.likeits.sanyou.ui.me.AboutActivity;
import com.likeits.sanyou.ui.me.MyCollectActivity;
import com.likeits.sanyou.ui.me.MyFootprintActivity;
import com.likeits.sanyou.ui.me.MyOpinionActivity;
import com.likeits.sanyou.ui.me.MyPostsActivity;
import com.likeits.sanyou.ui.me.MyReturnCarshActivity;
import com.likeits.sanyou.ui.me.MyShoppingCartActivity;
import com.likeits.sanyou.ui.me.MyfriendActivity;
import com.likeits.sanyou.ui.me.Setting02Activity;
import com.likeits.sanyou.ui.me.SettingActivity;
import com.likeits.sanyou.ui.shop.ShopIndentActivity;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.LoaddingDialog;
import com.likeits.sanyou.utils.ToastUtil;
import com.likeits.sanyou.view.CircleImageView;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment04 extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ScrollView>, View.OnClickListener {

    private PullToRefreshScrollView mPullToRefreshScrollView;
    private RelativeLayout rlPhoto, rlAllOrder;
    private RelativeLayout tvFriend, tvReturnCarsh, tvShoppingCart, tvCollect, tvFootprint, tvOpinion, tvAbout;
    private TextView tvPayment, tvDelivery, tvReceiving, tvComment;
    private ImageView ivSetting;
    private CircleImageView iv_userAvatar;
    private Bundle bundle;
    private TextView tv_tvName;
    private RelativeLayout rl_friend, rl_Collect,rl_Posts;
    private TextView tv_tvFriend01, tv_tvCollect01,tv_tvPosts;
    private String collect;
    private UserInfoEntity mUserInfoEntity;
    private LoaddingDialog loaddingDialog;
    private TextView tv_grade;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_fragment04;
    }

    @Override
    protected void lazyLoad() {
        loaddingDialog = new LoaddingDialog(getContext());
        initData();
       // loaddingDialog.show();
        initView();
    }

    private void initData() {
        String url = MyApiService.GetInfo;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
               // loaddingDialog.dismiss();
                Log.d("TAG", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.optString("code");
                    if ("1".equals(code)) {
                        mUserInfoEntity = JSON.parseObject(String.valueOf(obj.optJSONObject("data")), UserInfoEntity.class);
                        tv_tvFriend01.setText(mUserInfoEntity.getFriends());
                        tv_tvCollect01.setText(mUserInfoEntity.getCollections());
                        tv_tvPosts.setText(mUserInfoEntity.getArticles());
                        ImageLoader.getInstance().displayImage(mUserInfoEntity.getHeadimg(), iv_userAvatar);
                        tv_tvName.setText(mUserInfoEntity.getNickname());
                        if("1".equals(mUserInfoEntity.getRole())){
                            tv_grade.setText("普通客户");
                        }else{
                            tv_grade.setText(mUserInfoEntity.getRolename());
                        }
                        UserInfoCacheSvc.createOrUpdate(mUserInfoEntity.getEasemob_id(), mUserInfoEntity.getNickname(), mUserInfoEntity.getHeadimg());
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
               // loaddingDialog.dismiss();
            }

            @Override
            public void onFinish() {
                super.onFinish();
               // loaddingDialog.dismiss();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG","565");
        initData();
    }

    private void initView() {
        mPullToRefreshScrollView = findViewById(R.id.ll_home_scrollview);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshScrollView.setOnRefreshListener(this);
        mPullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
                "上次刷新时间");
        mPullToRefreshScrollView.getLoadingLayoutProxy()
                .setPullLabel("下拉刷新");
        mPullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                "松开即可刷新");
        /**
         * 点击事件初始化
         */

        rlPhoto = findViewById(R.id.bt_rlPhoto);//设置
        iv_userAvatar = findViewById(R.id.iv_userAvatar);//设置
        ivSetting = findViewById(R.id.iv_ivSetting);//设置
        rlAllOrder = findViewById(R.id.rl_allOrder);//全部订单
        tvPayment = findViewById(R.id.tv_tvPayment);//待付款
        tvDelivery = findViewById(R.id.tv_tvDelivery);//待发货
        tvReceiving = findViewById(R.id.tv_tvReceiving);//待收货
        tvComment = findViewById(R.id.tv_tvComment);//待评价
        tvFriend = findViewById(R.id.tv_rlFriend);//我的钓友
        tvReturnCarsh = findViewById(R.id.tv_rlReturnCarsh);//我的返现
        tvShoppingCart = findViewById(R.id.tv_rlShoppingCart);//我的购物车
        tvCollect = findViewById(R.id.tv_rlCollect);//我的收藏
        tvFootprint = findViewById(R.id.tv_rlFootprint);//我的足迹
        tvOpinion = findViewById(R.id.tv_rlOpinion);//意见反馈
        tvAbout = findViewById(R.id.rl_About);//关于
        tv_tvName = findViewById(R.id.tv_tvName);//名字
        rl_friend = findViewById(R.id.rl_friend);//好友
        rl_Collect = findViewById(R.id.rl_Collect);//收藏
        rl_Posts = findViewById(R.id.rl_Posts);//帖子
        tv_tvFriend01 = findViewById(R.id.tv_tvFriend01);//好友数
        tv_tvCollect01 = findViewById(R.id.tv_tvCollect01);//收藏数
        tv_tvPosts = findViewById(R.id.tv_tvPosts);//帖子数
        tv_grade = findViewById(R.id.tv_grade);//帖子数

        /**
         * 點擊事件
         */
        rlPhoto.setOnClickListener(this);
        ivSetting.setOnClickListener(this);
        rlAllOrder.setOnClickListener(this);
        tvPayment.setOnClickListener(this);
        tvDelivery.setOnClickListener(this);
        tvReceiving.setOnClickListener(this);
        tvComment.setOnClickListener(this);
        tvFriend.setOnClickListener(this);
        tvReturnCarsh.setOnClickListener(this);
        tvShoppingCart.setOnClickListener(this);
        tvFootprint.setOnClickListener(this);
        tvOpinion.setOnClickListener(this);
        tvAbout.setOnClickListener(this);
        rl_friend.setOnClickListener(this);

        tvCollect.setOnClickListener(this);
        rl_Collect.setOnClickListener(this);
        rl_Posts.setOnClickListener(this);

    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        mPullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        mPullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_rlPhoto:
                toActivity(SettingActivity.class);
                break;
            case R.id.iv_ivSetting:
                toActivity(Setting02Activity.class);
                break;
            case R.id.rl_allOrder:
                bundle = new Bundle();
                bundle.putString("status", "");
                toActivity(ShopIndentActivity.class, bundle);
                break;
            case R.id.tv_tvPayment:
                bundle = new Bundle();
                bundle.putString("status", "0");
                toActivity(ShopIndentActivity.class, bundle);
                break;
            case R.id.tv_tvDelivery:
                bundle = new Bundle();
                bundle.putString("status", "1");
                toActivity(ShopIndentActivity.class, bundle);
                break;
            case R.id.tv_tvReceiving:
                bundle = new Bundle();
                bundle.putString("status", "2");
                toActivity(ShopIndentActivity.class, bundle);
                break;
            case R.id.tv_tvComment:
                // toActivity(CommentActivity.class);
                bundle=new Bundle();
                bundle.putString("status","4");
                toActivity(ShopIndentActivity.class,bundle);
                ToastUtil.showS(getActivity(), "待开发");
                break;
            case R.id.tv_rlFriend:
            case R.id.rl_friend:
                toActivity(MyfriendActivity.class);
                break;
            case R.id.tv_rlReturnCarsh:
                toActivity(MyReturnCarshActivity.class);
                break;
            case R.id.tv_rlShoppingCart:
                toActivity(MyShoppingCartActivity.class);
                break;
            case R.id.tv_rlCollect:
            case R.id.rl_Collect:
                bundle = new Bundle();
                bundle.putSerializable("mUserInfoEntity", mUserInfoEntity);
                toActivity(MyCollectActivity.class, bundle);
                break;
            case R.id.tv_rlFootprint:
                toActivity(MyFootprintActivity.class);
                break;
            case R.id.rl_Posts:
                toActivity(MyPostsActivity.class);
                break;
            case R.id.tv_rlOpinion:
                toActivity(MyOpinionActivity.class);
                break;
            case R.id.rl_About:
                toActivity(AboutActivity.class);
                break;
        }
    }
}
