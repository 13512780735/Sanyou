package com.likeits.sanyou.ui.shop.fargment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.CategoryDetails;
import com.likeits.sanyou.entity.ShopIndentBean01;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.shop.IndentDetailsActivity;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.ToastUtil;
import com.likeits.sanyou.utils.UtilPreference;
import com.likeits.sanyou.view.AmountView;
import com.likeits.sanyou.view.MyListview;
import com.likeits.sanyou.view.RoundImageView;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopDialogFragment extends DialogFragment implements View.OnClickListener {


    private TextView tvCertain;
    private ImageView ivDelete;
    private MyListview mListView;

    private String[] iconName = {"E20新品4凸镜20W恒流", "E20新品4凸镜20W恒流", "E20新品4凸镜20W恒流"};
    private List<Map<String, Object>> dataList;
    private SimpleAdapter simpleAdapter;
    private AmountView mAmountView;
    private CategoryDetails categoryDetails;
    private String keys;
    private int goods_num = 1;
    private TextView tv_price;
    private Intent intent;
    private Bundle bundle;
    private List<ShopIndentBean01> indentData;
    private RoundImageView kefu_service;
    private String total;
    //private String addressId;

    public ShopDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_shop_dialog, container, false);
        keys = getArguments().getString("keys");
        // addressId =UtilPreference.getStringValue(getActivity(),"addressId");
        categoryDetails = (CategoryDetails) getArguments().getSerializable("categoryDetails");
        indentData=new ArrayList<>();
        initView(view);
        initListener();
        return view;
    }

    private void initListener() {
        tvCertain.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
    }

    private void initView(View view) {
        tvCertain = (TextView) view.findViewById(R.id.tv_certain);
        tv_price = (TextView) view.findViewById(R.id.tv_price);
        ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
        mListView = (MyListview) view.findViewById(R.id.shop_color_listview);
        mAmountView = (AmountView) view.findViewById(R.id.amount_view);
        kefu_service = (RoundImageView) view.findViewById(R.id.kefu_service);
        String role= UtilPreference.getStringValue(getActivity(),"role");
        if ("1".equals(role)) {
            tv_price.setText("¥ "+categoryDetails.getMoney_need());
        }else {
            tv_price.setText("¥ "+categoryDetails.getMoney_vip());
        }
       // tv_price.setText("¥ " + categoryDetails.getMoney_need());
        ImageLoader.getInstance().displayImage(categoryDetails.getPath(),kefu_service);
        dataList = new ArrayList<Map<String, Object>>();
        getData();
        String[] from = {"name"};
        final int[] to = {R.id.tv_color_name};
        simpleAdapter = new SimpleAdapter(getActivity(), dataList, R.layout.layout_shop_cart_listview_items, from, to);
        //配置适配器
        mListView.setAdapter(simpleAdapter);
        mAmountView.setGoods_storage(50);
        mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                // Toast.makeText(getActivity(), "Amount=>  " + amount, Toast.LENGTH_SHORT).show();
                goods_num = amount;
                Log.d("TAG", "goods_num-->" + amount);
            }
        });

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
    public void onStart() {
        super.onStart();

        DisplayMetrics dm = new DisplayMetrics();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;

        window.setAttributes(windowParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_certain:
//                if(StringUtil.isBlank(addressId)){
//                    ToastUtil.showS(getActivity(),"请选择配送地址");
//                    return;
//                }
                if ("1".equals(keys)) {
                    //添加购物车
                    add();
                } else if ("2".equals(keys)) {
                    ShopIndentBean01 shopIndentBean01=new ShopIndentBean01();
                    shopIndentBean01.setId(categoryDetails.getId());
                    shopIndentBean01.setGood_num(goods_num);
                    shopIndentBean01.setGoods_name(categoryDetails.getGoods_name());
                    String role= UtilPreference.getStringValue(getActivity(),"role");
                    if ("1".equals(role)) {
                        shopIndentBean01.setMoney_need(categoryDetails.getMoney_need());
                       total = String.valueOf((Integer.valueOf(categoryDetails.getMoney_need()) * goods_num));
                    }else{
                        shopIndentBean01.setMoney_need(categoryDetails.getMoney_vip());
                        total = String.valueOf((Integer.valueOf(categoryDetails.getMoney_vip()) * goods_num));
                    }

                    shopIndentBean01.setPath(categoryDetails.getPath());
                    indentData.add(shopIndentBean01);
                    intent = new Intent(getActivity(), IndentDetailsActivity.class);
                    intent.putExtra("indentData", (Serializable) indentData);
                    intent.putExtra("total", total);
                    intent.putExtra("goodId", categoryDetails.getId());
                    intent.putExtra("goods_num", String.valueOf(goods_num));
                    intent.putExtra("cart_id", "");
                    startActivity(intent);
                }
                break;
            case R.id.iv_delete:
                getDialog().dismiss();
                break;
        }
    }

    private void buy() {
        String url = MyApiService.GetAddShop;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(getActivity(), "ukey"));
        params.put("goods_id", categoryDetails.getId());
        params.put("goods_num", goods_num);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.optString("code");
                    String message = obj.optString("message");
                    if ("1".equals(code)) {
                        ToastUtil.showS(getActivity(), "下单成功");
                        getDialog().dismiss();
                    } else {
                        ToastUtil.showS(getActivity(), message);
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

    private void add() {
        String url = MyApiService.GetAddShop;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(getActivity(), "ukey"));
        params.put("goods_id", categoryDetails.getId());
        params.put("goods_num", goods_num);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.optString("code");
                    String message = obj.optString("message");
                    if ("1".equals(code)) {
                        ToastUtil.showS(getActivity(), "添加成功");
                        getDialog().dismiss();
                    } else {
                        ToastUtil.showS(getActivity(), message);
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
