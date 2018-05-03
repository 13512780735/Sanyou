package com.likeits.sanyou.ui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.EmptyEntity;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.utils.StringUtil;
import com.likeits.sanyou.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditAddressActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.ed_edName)
    EditText ed_edName;
    @BindView(R.id.ed_tvPhone)
    EditText ed_tvPhone;
    @BindView(R.id.editText)
    EditText edAddress;
    @BindView(R.id.tv_tvaddress)
    TextView tv_tvaddress;
    private String keys, name, phone, address, address01;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        ButterKnife.bind(this);
        keys = getIntent().getStringExtra("keys");
        tvHeader.setText("收货地址");
        if ("1".equals(keys)) {
            return;
        } else if ("2".equals(keys)) {
            name = getIntent().getStringExtra("name");
            phone = getIntent().getStringExtra("phone");
            address = getIntent().getStringExtra("address");
            address01 = getIntent().getStringExtra("address01");
            provinceId = getIntent().getStringExtra("province");
            cityId = getIntent().getStringExtra("city");
            districtId = getIntent().getStringExtra("district");
            id = getIntent().getStringExtra("id");
        }
        initView();
    }

    private void initView() {
        if ("1".equals(keys)) {
            ed_edName.setText("");
            ed_tvPhone.setText("");
            tv_tvaddress.setText("");
            edAddress.setText("");
        } else if ("2".equals(keys)) {
            ed_edName.setText(name);
            ed_tvPhone.setText(phone);
            tv_tvaddress.setText(address);
            edAddress.setText(address01);
        }
    }

    @OnClick({R.id.backBtn, R.id.tv_save, R.id.ll_lladdress})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
               onBackPressed();
                break;
            case R.id.tv_save:
                if ("1".equals(keys)) {
                    save();//添加保存
                } else if ("2".equals(keys)) {
                    save1();//编辑保存
                }
                break;
            case R.id.ll_lladdress:
                Intent intent01 = new Intent();
                intent01.putExtra("key", "1");
                intent01.setClass(mContext,
                        AddressSelectActivity.class);
                startActivityForResult(intent01, 1);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(mContext, ReceivingAddressActivity.class);
        startActivity(intent);
        finish();
    }

    private void save1() {
        name = ed_edName.getText().toString().trim();
        phone = ed_tvPhone.getText().toString().trim();
        address01 = edAddress.getText().toString().trim();
        HttpMethods.getInstance().GetShopEditaddress(new MySubscriber<EmptyEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<EmptyEntity> httpResult) {
                if (httpResult.getCode() == 1) {
                    showToast(httpResult.getMsg());
                    Intent intent = new Intent();
                    setResult(101, intent);
                    finish();
                } else {
                    showToast(httpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey, id, address01, name, phone, provinceId, cityId, districtId);
    }

    private void save() {
        name = ed_edName.getText().toString().trim();
        phone = ed_tvPhone.getText().toString().trim();
        address01 = edAddress.getText().toString().trim();
        if (StringUtil.isBlank(name)||StringUtil.isBlank(phone)||StringUtil.isBlank(address01)) {
            ToastUtil.showS(mContext, "请完善资料");
            return;
        }if(!StringUtil.isCellPhone(phone)){
            ToastUtil.showS(mContext, "手机格式不正确");
            return;
        }
        HttpMethods.getInstance().GetShopAddAddress(new MySubscriber<EmptyEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<EmptyEntity> httpResult) {
                if (httpResult.getCode() == 1) {
                    showToast(httpResult.getMsg());
                    Intent intent = new Intent();
                    setResult(102, intent);
                    finish();
                } else {
                    showToast(httpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey, address01, name, phone, provinceId, cityId, districtId);
    }

    private String provinceId, cityId, districtId, province, city, district;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (data != null) {
                    provinceId = data.getStringExtra("provinceId");
                    cityId = data.getStringExtra("cityId");
                    districtId = data.getStringExtra("districtId");
                    province = data.getStringExtra("province");
                    Log.d("TAG", "province" + province);
                    city = data.getStringExtra("city");
                    Log.d("TAG", "city" + city);
                    district = data.getStringExtra("district");
                    Log.d("TAG", "district" + district);
                    Log.d("TAG88", province + city + district);
                    tv_tvaddress.setText(province + city + district);
                }
                break;
            default:
                break;
        }
    }
}
