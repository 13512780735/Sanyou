package com.likeits.sanyou.ui.device;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.exception.BleException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.likeits.sanyou.R;
import com.likeits.sanyou.bluetooth.BluetoothService;
import com.likeits.sanyou.ui.base.Container;
import com.likeits.sanyou.view.CircleProgressBar;
import com.xw.repo.BubbleSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Device09Activity extends Container implements PullToRefreshBase.OnRefreshListener2<ScrollView> {
    @BindView(R.id.ll_home_scrollview)
    PullToRefreshScrollView mPullToRefreshScrollView;
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.iv_header_left)
    ImageView ivLeft;
    @BindView(R.id.iv_header_right)
    ImageView ivRight;
    @BindView(R.id.circle_progress)
    CircleProgressBar mCircleProgressBar;
    @BindView(R.id.progressBar)
    BubbleSeekBar mSeekBar;
    private String hex;
    private String hex2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device09);
        ButterKnife.bind(this);
        bindService();
        mBluetoothService = getBluetoothService();
        hex = "0x24000000005c5A";
        initView();
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

    private BluetoothService mBluetoothService;
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
    private void initData() {
        write(hex);
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
                        Log.d("TAG","写入成功");
                    }

                    @Override
                    public void onFailure(final BleException exception) {
                    }

                    @Override
                    public void onInitiatedResult(boolean result) {

                    }

                });

    }

    private void initView() {
        tvHeader.setText("成功连接设备....");

        ivRight.setImageResource(R.mipmap.nav_settings);
        ivLeft.setImageResource(R.mipmap.icon_back);
        //圆形图
            mCircleProgressBar.resetProgress();
        mCircleProgressBar.setProgress(75);
        mCircleProgressBar.setProgressDelayed(75);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshScrollView.setOnRefreshListener(this);
        mPullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
                "上次刷新时间");
        mPullToRefreshScrollView.getLoadingLayoutProxy()
                .setPullLabel("下拉刷新");
        mPullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                "松开即可刷新");
        mSeekBar.setProgress(92);
        hex2="0x24000000005c5A";
        mSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                Log.v("拖动过程中的值：", String.valueOf(progress) + ", " + String.valueOf(progressFloat));
                String hex1=Integer.toHexString(Integer.parseInt(String.valueOf(progress)));
                Log.d("TAG,hex2",hex1);
                hex2="0x2400000000"+hex1+"5A";
                Log.d("TAG,hex1",hex);
                write(hex2);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                Log.v("开始滑动时的值：", String.valueOf(progress) + ", " + String.valueOf(progressFloat));
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                Log.v("停止滑动时的值：", String.valueOf(progress) + ", " + String.valueOf(progressFloat));
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

    @OnClick({R.id.iv_header_left,R.id.device_btButton01, R.id.device_btButton02})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_left:
                onBackPressed();
                break;
            case R.id.device_btButton01:
                //hex = "0x24000000005c5A";
                Log.d("TAG,hex",hex2);
                write(hex2);
                findViewById(R.id.device_btButton02).setVisibility(View.VISIBLE);
                findViewById(R.id.device_btButton01).setVisibility(View.GONE);

                break;
            case R.id.device_btButton02:
                hex = "0x240000005F005A";
                Log.d("TAG,hex",hex);
                write(hex);
                findViewById(R.id.device_btButton01).setVisibility(View.VISIBLE);
                findViewById(R.id.device_btButton02).setVisibility(View.GONE);
                break;
        }
    }
}
