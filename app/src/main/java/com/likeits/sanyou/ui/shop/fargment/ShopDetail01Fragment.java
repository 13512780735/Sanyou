package com.likeits.sanyou.ui.shop.fargment;


import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.CategoryDetails;
import com.likeits.sanyou.ui.base.BaseFragment;
import com.likeits.sanyou.ui.shop.ReceivingAddressActivity;
import com.likeits.sanyou.utils.LoaddingDialog;
import com.likeits.sanyou.utils.StringUtil;
import com.likeits.sanyou.utils.UtilPreference;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopDetail01Fragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ScrollView> {


    private TextView tvOrdPrice, tv_title, tv_newPrice, tv_express, tv_sell, tv_address;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private LoaddingDialog loadingDialog;
    private CategoryDetails categoryDetails;
    private ImageView shop_avatar;
    private RelativeLayout rl_address;
    private String address;
    private String addressId;


    @Override
    protected int setContentView() {
        return R.layout.fragment_shop_detail01;
    }

    @Override
    protected void lazyLoad() {
        loadingDialog = new LoaddingDialog(getActivity());
        categoryDetails = (CategoryDetails) getArguments().getSerializable("categoryDetails");
        Log.d("TAG11", categoryDetails.getGoods_name());
        address=UtilPreference.getStringValue(getActivity(),"address");
        initView();
    }


    private void initView() {
        tvOrdPrice = findViewById(R.id.tv_old_price);//原价
        tv_title = findViewById(R.id.tv_title);//标题
        tv_newPrice = findViewById(R.id.tv_newPrice);//新价格
        tv_express = findViewById(R.id.tv_express);//快递
        shop_avatar = findViewById(R.id.shop_avatar);//图片
        tv_sell = findViewById(R.id.tv_sell);//销量
        rl_address = findViewById(R.id.rl_address);
        tv_address = findViewById(R.id.tv_tvaddress);//地址
        mPullToRefreshScrollView = findViewById(R.id.ll_shop_details_scrollview);
        tvOrdPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshScrollView.setOnRefreshListener(this);
        mPullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
                "上次刷新时间");
        mPullToRefreshScrollView.getLoadingLayoutProxy()
                .setPullLabel("下拉刷新");
        mPullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                "松开即可刷新");
        int W=getActivity().getWindowManager().getDefaultDisplay().getWidth();//获取屏幕高度
        ViewGroup.LayoutParams para;
        para = shop_avatar.getLayoutParams();
        para.height = W;
        para.width = W;
        shop_avatar.setLayoutParams(para);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,W);
//        shop_avatar.setLayoutParams(params );
        ImageLoader.getInstance().displayImage(categoryDetails.getPath(), shop_avatar);
        tv_title.setText(categoryDetails.getGoods_name());
        String role= UtilPreference.getStringValue(context,"role");
        if ("1".equals(role)) {
            tv_newPrice.setText("¥ "+categoryDetails.getMoney_need());
        }else {
            tv_newPrice.setText("¥ "+categoryDetails.getMoney_vip());
        }
        tv_sell.setText("销量:" + categoryDetails.getSell_num());

        if(StringUtil.isBlank(address)){
            tv_address.setText("配送至:  "+"请选择配送地址");
        }else{
            tv_address.setText("配送至:  "+address);
        }
        rl_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent01 = new Intent();
                intent01.putExtra("key", "1");
                intent01.setClass(getActivity(),
                        ReceivingAddressActivity.class);
                startActivityForResult(intent01, 1);
            }
        });
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (data != null) {
                    address = data.getStringExtra("address");
                    addressId = data.getStringExtra("addressId");
                    Log.d("TAG333",address);
                    Log.d("TAG444",addressId);
                    tv_address.setText("配送至:  "+address);
                }
                break;
            default:
                break;
        }
    }
}
