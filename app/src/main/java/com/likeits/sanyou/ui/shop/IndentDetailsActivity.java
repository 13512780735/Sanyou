package com.likeits.sanyou.ui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.IndentDetailsAdapter;
import com.likeits.sanyou.entity.CategoryDetails;
import com.likeits.sanyou.entity.ShopIndentBean01;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.base.Container;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.ToastUtil;
import com.likeits.sanyou.view.MyListview;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IndentDetailsActivity extends Container {

    private String keys;
    private CategoryDetails categoryDetails;
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_address)
    TextView tv_address;
//    @BindView(R.id.iv_avatar)
//    ImageView iv_avatar;
//    @BindView(R.id.tv_name01)
//    TextView tv_name01;
//    @BindView(R.id.tv_price)
//    TextView tv_price;
//    @BindView(R.id.tv_number)
//    TextView tv_number;
    @BindView(R.id.tv_number01)
    TextView tv_number01;
    @BindView(R.id.tv_total_money)
    TextView tv_total_money;
    @BindView(R.id.tv_offer)
    TextView tv_offer;
    @BindView(R.id.myListView)
    MyListview myListView;
    private JSONObject object;
    private String address;
    private String addressId;
    private String name;
    private String goods_num;
    private List<ShopIndentBean01> indentData;
    private IndentDetailsAdapter mAdapter;
    private String total;
    private String goodId;
    private String cart_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indent_details);
        ButterKnife.bind(this);
        indentData= (List<ShopIndentBean01>) getIntent().getSerializableExtra("indentData");
        total= getIntent().getStringExtra("total");
        goodId= getIntent().getStringExtra("goodId");
        goods_num= getIntent().getStringExtra("goods_num");
        cart_id= getIntent().getStringExtra("cart_id");
        //Log.d("TAG","total-->"+total+"goodId-->"+goodId+"goods_num-->"+goods_num+"cart_id-->"+cart_id+"path-->"+indentData.get(0).getPath());
        initAddress();
        initView();
    }

    private void initAddress() {
        String url = MyApiService.GETADDRESS;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.optString("code");
                    if ("1".equals(code)) {
                        JSONArray array = obj.optJSONArray("data");
                         object=array.optJSONObject(0);
                        name=object.optString("name");
                        address=object.optString("dizhi");
                        tv_name.setText("收货人："+name);
                        tv_address.setText("收货地址："+address);
                        addressId=object.optString("id");
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
    private void initView() {
        tvHeader.setText("确认订单");
        mAdapter=new IndentDetailsAdapter(mContext,indentData);
        myListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
//        ImageLoader.getInstance().displayImage(categoryDetails.getPath(),iv_avatar);
//        tv_name01.setText(categoryDetails.getGoods_name());
//        tv_price.setText("¥  "+categoryDetails.getMoney_need());
//        tv_number.setText("X "+goods_num);
        tv_number01.setText("共"+indentData.size()+"件");
        tv_total_money.setText("合计："+"¥ "+total);
    }
    @OnClick({R.id.backBtn,R.id.tv_offer,R.id.ll_address})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_offer:
                offer();
                break;
            case R.id.ll_address:
                Intent intent01 = new Intent();
                intent01.putExtra("key", "1");
                intent01.setClass(mContext,
                        ReceivingAddressActivity.class);
                startActivityForResult(intent01, 1);
                break;
        }
    }

    private void offer() {
        String url = MyApiService.AddShop;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("goods_id", goodId);
        params.put("cart_id", cart_id);
        params.put("goods_num", goods_num);
        params.put("address_id", addressId);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.optString("code");
                    String message = obj.optString("message");
                    if ("1".equals(code)) {
                        ToastUtil.showS(mContext, "下单成功");
                        String  sn=obj.optJSONObject("data").optString("sn");
                        Intent intent=new Intent(mContext, ShopIndentActivity.class);
                        //intent.putExtra("sn",sn);
                        intent.putExtra("status","0");
                         startActivity(intent);
                    } else {
                        ToastUtil.showS(mContext, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }
        });
    }    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (data != null) {
                    address = data.getStringExtra("address");
                    addressId = data.getStringExtra("addressId");
                    name=data.getStringExtra("name01");
                    Log.d("TAG333",address);
                    Log.d("TAG444",addressId);
                    tv_address.setText("配送至:  "+address);
                    tv_name.setText("收货人："+name);
                    tv_address.setText("收货地址："+address);
                }
                break;
            default:
                break;
        }
    }
}
