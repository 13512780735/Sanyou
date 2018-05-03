package com.likeits.sanyou.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.ShopIndentEntity;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.MainActivity;
import com.likeits.sanyou.ui.base.BaseViewHolder;
import com.likeits.sanyou.ui.shop.ShopIndentActivity;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.MyActivityManager;
import com.likeits.sanyou.utils.UtilPreference;
import com.likeits.sanyou.view.CustomDialog;
import com.likeits.sanyou.view.MyGridView01;
import com.likeits.sanyou.wxapi.PayActivity;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/7.
 */

public class ShopIndentViewHolder extends BaseViewHolder<ShopIndentEntity> {

    @BindView(R.id.ord_name)
    TextView ord_name;//名称
    @BindView(R.id.tv_status)
    TextView tv_status;//交易状态
    //    @BindView(R.id.iv_avatar)
//    ImageView iv_avatar;//商品头像
//    @BindView(R.id.tv_name)
//    TextView tv_name;//商品名称
//    @BindView(R.id.tv_introduct)
//    TextView tv_introduct;//商品描述
//    @BindView(R.id.tv_price)
//    TextView tv_price;//商品价格
//    @BindView(R.id.tv_number)
//    TextView tv_number;//商品数量
    @BindView(R.id.tv_total_money)
    TextView tv_total_money;//商品总计
    @BindView(R.id.mGridView)
    MyGridView01 mGridView;//商品总计

    @BindView(R.id.del_indent)
    TextView tv_delIndent;//删除订单
    @BindView(R.id.tv_logistics)
    TextView tv_logistics;//查看物流
    @BindView(R.id.tv_confirm_payment)
    TextView tv_confirm_payment;//确认收货或付款

    private ShopIndentActivity baseActivity;
    private shopGridViewAdapter mAdapter;
    private List<ShopIndentEntity.ShopBean> shopData;
    private CustomDialog customDialog;


    public void setBaseActivity(ShopIndentActivity baseActivity) {
        this.baseActivity = baseActivity;

    }

    public ShopIndentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(final ShopIndentEntity shopIndentEntity, int position) {
        ord_name.setText("订单号："+shopIndentEntity.getSn()+" >");
        String status = shopIndentEntity.getStatus();
        if ("0".equals(status)) {
            tv_status.setText("待买家付款");
            tv_delIndent.setVisibility(View.VISIBLE);
            tv_delIndent.setText("删除订单");
            tv_confirm_payment.setVisibility(View.VISIBLE);
            tv_confirm_payment.setText("付款");
        } else if ("1".equals(status)) {
            tv_status.setText("待卖家发货");
            //tv_logistics.setVisibility(View.VISIBLE);
            tv_confirm_payment.setVisibility(View.VISIBLE);
            tv_confirm_payment.setText("确认收货");
        } else if ("2".equals(status)) {
            tv_status.setText("卖家已发货");
            tv_logistics.setVisibility(View.VISIBLE);
            tv_confirm_payment.setVisibility(View.VISIBLE);
            tv_confirm_payment.setText("确认收货");
        } else if ("3".equals(status)) {
            tv_status.setText("待评价");
        } else if ("4".equals(status)) {
            tv_status.setText("交易完成");
        }
//        ImageLoader.getInstance().displayImage(shopIndentEntity.getPath(),iv_avatar);
//        tv_name.setText(shopIndentEntity.getGoods_name());
//        tv_introduct.setText(shopIndentEntity.getGoods_introduct());
//        tv_price.setText("¥  "+shopIndentEntity.getMoney_need());
//        tv_number.setText("X "+shopIndentEntity.getGoods_num());
         mAdapter = new shopGridViewAdapter(context, shopIndentEntity.getShop());
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        tv_total_money.setText("共" + shopIndentEntity.getNumber() + "件商品 合计 :¥ " + shopIndentEntity.getTotal() + "（含运费¥ 0.00）");
        tv_delIndent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sn=shopIndentEntity.getSn();
                String url= MyApiService.EditShop;
                RequestParams params=new RequestParams();
                params.put("ukey", UtilPreference.getStringValue(context,"ukey"));
                params.put("sn",sn);
                params.put("status","1");
                HttpUtil.post(url, params, new HttpUtil.RequestListener() {
                    @Override
                    public void success(String response) {
                        Log.d("TAG",response);
                        try {
                            JSONObject obj=new JSONObject(response);
                            String code=obj.optString("code");
                            if("1".equals(code)){
                                Intent intent = new Intent(context, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("keys", "1");
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                                MyActivityManager.getInstance().finishAllActivity();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(Throwable e) {

                    }
                });
            }
        });
        tv_logistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomDialog(context).builder()
                        .setGravity(Gravity.CENTER)//默认居中，可以不设置
                        .setTitle("物流公司"+shopIndentEntity.getCourier(), context.getResources().getColor(R.color.sd_color_black))//可以不设置标题颜色，默认系统颜色
                        .setSubTitle("单号："+shopIndentEntity.getCourier_number())
                        .setCenterButton("我知道了！", context.getResources().getColor(R.color.head_red), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                            }
                        });
                customDialog.show();
            }
        });
        tv_confirm_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sn=shopIndentEntity.getSn();
                if("付款".equals(tv_confirm_payment.getText().toString())){
                    Intent intent=new Intent(context, PayActivity.class);
                    intent.putExtra("sn",sn);
                    intent.putExtra("sum",shopIndentEntity.getTotal());
                    context.startActivity(intent);
                }else if("确认收货".equals(tv_confirm_payment.getText().toString())){
                    confirm(sn);
                }
            }
        });
    }

    private void confirm(String sn) {
        String url= MyApiService.EditShop;
        RequestParams params=new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(context,"ukey"));
        params.put("sn",sn);
        params.put("status","2");
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG",response);
                try {
                    JSONObject obj=new JSONObject(response);
                    String code=obj.optString("code");
                    if("1".equals(code)){
                        Intent intent = new Intent(context, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("keys", "1");
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        MyActivityManager.getInstance().finishAllActivity();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }
        });
    }

}
