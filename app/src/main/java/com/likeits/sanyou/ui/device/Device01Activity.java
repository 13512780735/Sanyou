package com.likeits.sanyou.ui.device;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.likeits.sanyou.R;
import com.likeits.sanyou.bluetooth.BluetoothService;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.base.Container;
import com.likeits.sanyou.utils.HttpUtil;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 灯光4-2
 */
public class Device01Activity extends Container implements RadioGroup.OnCheckedChangeListener, PullToRefreshBase.OnRefreshListener2<ScrollView> {
    /**
     * 燈光亮度
     */

    @BindView(R.id.device_rbGroup01)
    RadioGroup rbGroup01;
    @BindView(R.id.device_rbButton01)
    RadioButton rbButton01;
    @BindView(R.id.device_rbButton02)
    RadioButton rbButton02;
    /**
     * 光源
     */
    @BindView(R.id.device_light_rbGroup02)
    RadioGroup rbGroupLight01;
    @BindView(R.id.device_light_rbButton01)
    RadioButton rbButtonLight01;
    @BindView(R.id.device_light_rbButton02)
    RadioButton rbButtonLight02;
    @BindView(R.id.device_light_rbButton03)
    RadioButton rbButtonLight03;
    @BindView(R.id.device_light_rbButton04)
    RadioButton rbButtonLight04;

    @BindView(R.id.ll_home_scrollview)
    PullToRefreshScrollView mPullToRefreshScrollView;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_time)
    TextView tvTime;//时间显示
    private String hex;
    private BluetoothService mBluetoothService;
    private TextView view;
    private int time;
    private String lightId;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device01);
        ButterKnife.bind(this);
        address = getIntent().getStringExtra("address");
        bindService();
        mBluetoothService = getBluetoothService();
        hex = "0x24000000B1005A";
        lightId = "2";
        initView();

    }

    private void initData() {
        write(hex);
        notify1();
    }

    private void refresh() {

    }

    public BluetoothService getBluetoothService() {
        return mBluetoothService;
    }

    private void bindService() {
        Intent bindIntent = new Intent(this, BluetoothService.class);
        this.bindService(bindIntent, mFhrSCon, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothService != null)
            mBluetoothService.closeConnect();
        unbindService();
    }

    private void unbindService() {
        this.unbindService(mFhrSCon);
    }

    private ServiceConnection mFhrSCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothService = ((BluetoothService.BluetoothBinder) service).getService();
            mBluetoothService.setConnectCallback(callback);
            initData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBluetoothService = null;
        }
    };

    private BluetoothService.Callback2 callback = new BluetoothService.Callback2() {

        @Override
        public void onDisConnected() {
            finish();
        }
    };

    private void initView() {
        tvHeader.setText("成功连接设备....");
        //tv_right.setText("解除绑定");
        tv_right.setTextColor(this.getResources().getColor(R.color.white));
        rbGroup01.setOnCheckedChangeListener(this);
        rbGroupLight01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.device_light_rbButton01:
                        lightId = "1";
                        rbButton01.setChecked(true);
                        rbButton02.setChecked(false);
                        hex = "0x24000000A1005A";
                        initData();
                        break;
                    case R.id.device_light_rbButton02:
                        lightId = "2";
                        rbButton01.setChecked(true);
                        rbButton02.setChecked(false);
                        hex = "0x24000000B1005A";
                        initData();
                        break;
                    case R.id.device_light_rbButton03:
                        lightId = "3";
                        rbButton01.setChecked(true);
                        rbButton02.setChecked(false);
                        hex = "0x24000000C1005A";
                        initData();
                        break;
                    case R.id.device_light_rbButton04:
                        lightId = "4";
                        rbButton01.setChecked(true);
                        rbButton02.setChecked(false);
                        hex = "0x24000000D1005A";
                        initData();
                        break;
                }
            }

        });
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshScrollView.setOnRefreshListener(this);
        mPullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
                "上次刷新时间");
        mPullToRefreshScrollView.getLoadingLayoutProxy()
                .setPullLabel("下拉刷新");
        mPullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                "松开即可刷新");


    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        //  rbGroup01.indexOfChild(rbGroup01.findViewById(checkedId));
        switch (checkedId) {
            case R.id.device_rbButton01:
                if ("1".equals(lightId)) {
                    hex = "0x24000000A1005A";
                    initData();
                } else if ("2".equals(lightId)) {
                    hex = "0x24000000B1005A";
                    initData();
                } else if ("3".equals(lightId)) {
                    hex = "0x24000000C1005A";
                    initData();
                } else if ("4".equals(lightId)) {
                    hex = "0x24000000D1005A";
                    initData();
                }

                break;
            case R.id.device_rbButton02:
                if ("1".equals(lightId)) {
                    hex = "0x24000000A3005A";
                    initData();
                } else if ("2".equals(lightId)) {
                    hex = "0x24000000B3005A";
                    initData();
                } else if ("3".equals(lightId)) {
                    hex = "0x24000000C3005A";
                    initData();
                } else if ("4".equals(lightId)) {
                    hex = "0x24000000D3005A";
                    initData();
                }
                break;
        }
    }

    private void notify1() {
        mBluetoothService.notify("0000fff0-0000-1000-8000-00805f9b34fb",
                "0000fff3-0000-1000-8000-00805f9b34fb", new BleCharacterCallback() {
                    @Override
                    public void onSuccess(BluetoothGattCharacteristic characteristic) {
                        Log.d("TAG123", String.valueOf(HexUtil.encodeHex(characteristic.getValue())));
                        String time1 = String.valueOf(HexUtil.encodeHex(characteristic.getValue()));
                        String time2 = time1.substring(0, 8);
                        String time3 = time2.substring(6, 8);
                        time = Integer.parseInt(time3, 16);
                        Log.d("TAG212", time + "");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(10);
                                    tvTime.setText(time + "小时");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(BleException exception) {

                    }

                    @Override
                    public void onInitiatedResult(boolean result) {

                    }
                });
    }

    private void write(String hex) {
        Log.d("TAG3", hex);
        mBluetoothService.write(
                "0000fff0-0000-1000-8000-00805f9b34fb",
                "0000fff2-0000-1000-8000-00805f9b34fb",
                hex,
                new BleCharacterCallback() {

                    @Override
                    public void onSuccess(final BluetoothGattCharacteristic characteristic) {
                        Log.d("TAG", "写入成功");
                    }

                    @Override
                    public void onFailure(final BleException exception) {
                    }

                    @Override
                    public void onInitiatedResult(boolean result) {

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

    @OnClick({R.id.backBtn, R.id.device_btButton01, R.id.device_btButton02, R.id.tv_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_right:
                unbindDevice();
                break;
            case R.id.device_btButton01:
                if ("1".equals(lightId)) {
                    hex = "0x24000000A1005A";
                    initData();
                } else if ("2".equals(lightId)) {
                    hex = "0x24000000B1005A";
                    initData();
                } else if ("3".equals(lightId)) {
                    hex = "0x24000000C1005A";
                    initData();
                } else if ("4".equals(lightId)) {
                    hex = "0x24000000D1005A";
                    initData();
                }
                findViewById(R.id.device_btButton02).setVisibility(View.VISIBLE);
                findViewById(R.id.device_btButton01).setVisibility(View.GONE);

                break;
            case R.id.device_btButton02:
                hex = "0x240000005F005A";
                write(hex);
                findViewById(R.id.device_btButton01).setVisibility(View.VISIBLE);
                findViewById(R.id.device_btButton02).setVisibility(View.GONE);
                break;
        }
    }

    private void unbindDevice() {
        String url = MyApiService.EditDevice;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("device", address);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.optString("code");
                    if ("1".equals(code)) {
//                        Intent intent = getIntent();
//                        setResult(101, intent);
                        finish();
                    } else {
                        Log.d("TAG", obj.optString("message"));
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
