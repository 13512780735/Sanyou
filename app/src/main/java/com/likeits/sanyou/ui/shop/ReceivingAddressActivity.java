package com.likeits.sanyou.ui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.ReceivingAddressAdapter;
import com.likeits.sanyou.entity.AddressBean;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.EmptyEntity;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.utils.UtilPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceivingAddressActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.address_listview)
    ListView mListView;
    private String key;
    private ReceivingAddressAdapter mAdapter;
    List<AddressBean> addressData;
    private String name01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving_address);
        ButterKnife.bind(this);
        key = getIntent().getStringExtra("key");
        tvHeader.setText("选择收货地址");
        tvRight.setText("添加");
        addressData = new ArrayList<>();
        initData();
    }

    private void initData() {
        HttpMethods.getInstance().GetAddress(new MySubscriber<ArrayList<AddressBean>>(this) {
            @Override
            public void onHttpCompleted(HttpResult<ArrayList<AddressBean>> httpResult) {
                if (httpResult.getCode() == 1) {
                    addressData = httpResult.getData();
                    Log.d("TAG223",addressData.toString());
                    initView();
                }else{
                    showToast(httpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey);
    }

    private String province, name, phone, address, address01, city, district, province_name, city_name, district_name, addressId;

    private void initView() {
        tvRight.setTextColor(getResources().getColor(R.color.white));
        mAdapter = new ReceivingAddressAdapter(mContext, addressData);
        Log.d("TAG858",addressData.toString());
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("1".equals(key)) {
                    Intent intent = new Intent();
                    address01 = addressData.get(position).getDizhi();
                    addressId=addressData.get(position).getId();
                    name01=addressData.get(position).getName();
                    Log.d("TAG222",address01);
                    intent.putExtra("address", address01);
                    intent.putExtra("addressId", addressId);
                    intent.putExtra("name01", name01);
                    setResult(1, intent);
                    finish();
                }
            }
        });
        mAdapter.setOnDelClickListener(new ReceivingAddressAdapter.onDelClickListener() {
            @Override
            public void onDelClick(int position) {
                addressId = addressData.get(position).getId();
                del(addressId,position);
            }
        });
        mAdapter.setOnEditClickListener(new ReceivingAddressAdapter.onEditClickListener() {
            @Override
            public void onEditClick(int position) {
                name = addressData.get(position).getName();
                phone = addressData.get(position).getPhone();
                address01 = addressData.get(position).getAddress();
                province = addressData.get(position).getProvince();
                city = addressData.get(position).getCity();
                district = addressData.get(position).getName();
                province_name = addressData.get(position).getProvince_name();
                city_name = addressData.get(position).getCity_name();
                district_name = addressData.get(position).getDistrict_name();
                address = province_name + city_name + district_name;
                addressId = addressData.get(position).getId();
                Intent intent = new Intent(mContext, EditAddressActivity.class);
                intent.putExtra("keys", "2");
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                intent.putExtra("address01", address01);
                intent.putExtra("province", province);
                intent.putExtra("city", city);
                intent.putExtra("address", address);
                intent.putExtra("district", district);
                intent.putExtra("id", addressId);
                startActivityForResult(intent, 101);
            }
        });
        mAdapter.setOnCk_choseClickListener(new ReceivingAddressAdapter.onCk_choseClickListener() {
            @Override
            public void onCk_choseClick(int position) {
                addressId = addressData.get(position).getId();
                address01 = addressData.get(position).getDizhi();
                String id = String.valueOf(position);
                check(addressId,address01,id);
            }
        });
    }

    private void check(String addressId, final String address01, final String id) {
        HttpMethods.getInstance().GetCheckAddress(new MySubscriber<EmptyEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<EmptyEntity> httpResult) {
                if(httpResult.getCode()==1){
                    UtilPreference.saveString(mContext,"addressId", ReceivingAddressActivity.this.addressId);
                    UtilPreference.saveString(mContext,"address",address01);
                    refresh();
                    showToast(httpResult.getMsg());

                }else{
                    showToast(httpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        },ukey, addressId,"1");
    }

    private void del(final String id, final int position) {
        HttpMethods.getInstance().GetDelAddress(new MySubscriber<EmptyEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<EmptyEntity> httpResult) {
                if (httpResult.getCode() == 1) {
                    showToast(httpResult.getMsg());
                    addressData.remove(position);
                    refresh();
                } else {
                    showToast(httpResult.getMsg());
                }


            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey,id, "0");
    }

    @OnClick({R.id.backBtn, R.id.tv_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_right:
                Intent intent = new Intent(mContext, EditAddressActivity.class);
                intent.putExtra("keys", "1");
                startActivityForResult(intent, 102);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
            case 102:
                refresh();
                break;
            default:
                break;
        }
    }

    private void refresh() {
        if(addressData==null){
            return;
        }else{
            addressData.clear();
            initData();
            mAdapter.notifyDataSetChanged();
        }
    }
}
