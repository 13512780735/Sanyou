package com.likeits.sanyou.ui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.AddressAdapter;
import com.likeits.sanyou.entity.AddressVo;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.base.Container;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressSelectActivity extends Container {
    ListView listView, listView2, listView3;
    private AddressAdapter adapter;
    private JSONObject jsonObject;
    private List<AddressVo> listP = new ArrayList<AddressVo>();// 省
    private List<AddressVo> listS = new ArrayList<AddressVo>();// 市
    private List<AddressVo> listQ = new ArrayList<AddressVo>();// 区
    // 当前数据源
    private List<AddressVo> data = new ArrayList<AddressVo>();
    private String province, city, district;
    private String key;
    private String provinceId, cityId, districtId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_select);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        adapter = new AddressAdapter(this, data);
        initData();
        showProgress("Loading...");
        initView();
    }

    private void initData() {
        String url = MyApiService.GetPCD;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {

            @Override
            public void success(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.getString("code");
                    String message = obj.getString("message");
                    if ("1".equals(code)) {
                        JSONArray array = obj.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            jsonObject = array.getJSONObject(i);
                            AddressVo addressVo = new AddressVo();
                            addressVo.setId(jsonObject.getString("id"));
                            addressVo.setName(jsonObject.getString("name"));
                            listP.add(addressVo);
                        }
                        data.addAll(listP);
                        adapter.notifyDataSetChanged();
                    } else {
                        disShowProgress();
                        ToastUtil.showL(getApplicationContext(), message);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void failed(Throwable e) {
                disShowProgress();
                showErrorMsg("网络请求失败");

            }

            @Override
            public void onFinish() {
                disShowProgress();
            }
        });
    }


    private void initView() {
        listView = (ListView) findViewById(R.id.addressselect_listview);
        listView2 = (ListView) findViewById(R.id.addressselect_listview2);
        listView3 = (ListView) findViewById(R.id.addressselect_listview3);
        listView.setAdapter(adapter);
        listView2.setAdapter(adapter);
        listView3.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                provinceId = data.get(position).getId();
                province = data.get(position).getName();
                initData1();
                data.clear();
                showProgress("Loading...");
                listView.setVisibility(View.GONE);
                listView2.setVisibility(View.VISIBLE);
            }

        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityId = data.get(position).getId();
                city = data.get(position).getName();
                initData2();
                data.clear();
                showProgress("Loading...");
                listView.setVisibility(View.GONE);
                listView2.setVisibility(View.GONE);
                listView3.setVisibility(View.VISIBLE);

            }
        });
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                districtId = data.get(position).getId();
                district = data.get(position).getName();
                if ("1".equals(key)) {
                    Intent intent04 = new Intent();
                    intent04.putExtra("provinceId", provinceId);
                    intent04.putExtra("cityId", cityId);
                    intent04.putExtra("districtId", districtId);
                    intent04.putExtra("province", province);
                    intent04.putExtra("city", city);
                    intent04.putExtra("district", district);
                    setResult(1, intent04);
                    finish();
                }

            }
        });
    }
    private void initData1() {
        String url = MyApiService.GetPCD;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("pid", provinceId);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.getString("code");
                    String message = obj.getString("message");
                    if ("1".equals(code)) {
                        JSONArray array = obj.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            jsonObject = array.getJSONObject(i);
                            AddressVo addressVo = new AddressVo();
                            addressVo.setId(jsonObject.getString("id"));
                            addressVo.setName(jsonObject.getString("name"));
                            listS.add(addressVo);
                        }
                        data.addAll(listS);
                        adapter.notifyDataSetChanged();
                        disShowProgress();
                    } else {
                        disShowProgress();
                        ToastUtil.showL(getApplicationContext(), message);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                disShowProgress();
                ToastUtil.showS(mContext, "网络请求错误");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                disShowProgress();
            }
        });
    }
    private void initData2() {
        String url = MyApiService.GetPCD;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("cid", cityId);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.getString("code");
                    String message = obj.getString("message");
                    if ("1".equals(code)) {
                        JSONArray array = obj.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            jsonObject = array.getJSONObject(i);
                            AddressVo addressVo = new AddressVo();
                            addressVo.setId(jsonObject.getString("id"));
                            addressVo.setName(jsonObject.getString("name"));
                            listQ.add(addressVo);
                        }
                        data.addAll(listQ);
                        adapter.notifyDataSetChanged();
                        disShowProgress();
                    } else {
                        disShowProgress();
                        ToastUtil.showL(getApplicationContext(), message);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                disShowProgress();
                ToastUtil.showS(mContext, "网络请求错误");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                disShowProgress();
            }
        });
    }



    @OnClick(R.id.backBtn)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
        }

    }
}
