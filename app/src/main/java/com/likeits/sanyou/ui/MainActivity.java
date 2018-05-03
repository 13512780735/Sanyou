package com.likeits.sanyou.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.easemob.redpacket.utils.RedPacketUtil;
import com.easemob.redpacketsdk.constant.RPConstant;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.HomeViewPagerAdapter;
import com.likeits.sanyou.fragment.MainFragment01;
import com.likeits.sanyou.message.utils.SharePrefConstant;
import com.likeits.sanyou.message.utils.UserInfoCacheSvc;
import com.likeits.sanyou.message.widget.DemoHelper;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.login.LoginActivity;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.MyActivityManager;
import com.likeits.sanyou.utils.ToastUtil;
import com.likeits.sanyou.utils.UtilPreference;
import com.likeits.sanyou.view.NoScrollViewPager;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {
    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    @BindView(R.id.home_viewpager)
    NoScrollViewPager mViewPager;
    @BindView(R.id.rbHome)
    RadioButton mRbHome;
    @BindView(R.id.rbChat)
    RadioButton mRbChat;
    @BindView(R.id.rbContact)
    RadioButton mRbContact;
    @BindView(R.id.rbMe)
    RadioButton mRbMe;
    @BindView(R.id.rgTools)
    RadioGroup mRgTools;

    private HomeViewPagerAdapter adapter;
    private String ukey;
    private MainActivity mContext;
    private String keys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        MyActivityManager.getInstance().addActivity(this);
        mContext = this;
        ButterKnife.bind(this);
        keys=getIntent().getExtras().getString("keys");
        ukey = UtilPreference.getStringValue(MainActivity.this, "ukey");
        Log.d("TAG222", UtilPreference.getStringValue(MainActivity.this, "ukey"));
        addGeoFence();//高德定位
        initView();
        mViewPager.setOffscreenPageLimit(1);
        if("1".equals(keys)){
            mViewPager.setCurrentItem(3);
        }
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }

    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    private void addGeoFence() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        double latitude = aMapLocation.getLatitude();//获取纬度
                        double longitude = aMapLocation.getLongitude();//获取经度
                        String city = aMapLocation.getCity();
                        UtilPreference.saveString(mContext, "lat", String.valueOf(latitude));
                        UtilPreference.saveString(mContext, "lon", String.valueOf(longitude));
                        UtilPreference.saveString(mContext, "city", city.substring(0, city.length() - 1));
                        Log.d("TAG989", city + String.valueOf(longitude) + String.valueOf(latitude) + aMapLocation.getProvince());
                        upLoadLocation(latitude, longitude);
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        UtilPreference.saveString(mContext, "city", "中山");
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(10000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    private void upLoadLocation(double latitude, double longitude) {
        String url = MyApiService.BASE_URL + "?s=/api/member/adddistance";
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(mContext, "ukey"));
        params.put("lat", latitude);
        params.put("lon", longitude);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.optString("code");
                    if ("1".equals(code)) {
                        Log.d("TAG", obj.optString("msg"));
                    } else {
                        //ToastUtil.showS(mContext, obj.optString("msg"));
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


    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        //Log.d("TAG","您的账号已在其他设备登录！");
                        ToastUtil.showS(MainActivity.this, "您的账号已在其他设备登录！");
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.putExtra("online", "1");
                        startActivity(intent);
                        finish();
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)) ;
                            //连接不到聊天服务器
                        else {
                            //当前网络不可用，请检查网络设置
                            //
                        }
                    }
                }
            });
        }
    }

    private void initView() {
        adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(0);
        mRgTools.setOnCheckedChangeListener(this);
        MainFragment01 fragment = new MainFragment01();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mRgTools.check(mRgTools.getChildAt(position).getId());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        //  mViewPager.setCurrentItem(radioGroup.indexOfChild(radioGroup.findViewById(checkedId)), false);
        switch (checkedId) {
            case R.id.rbHome:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.rbChat:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.rbContact:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.rbMe:
                mViewPager.setCurrentItem(3);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                // 先将头像和昵称保存在本地缓存
                try {
                    String chatUserId = message.getStringAttribute(SharePrefConstant.ChatUserId);
                    String avatarUrl = message.getStringAttribute(SharePrefConstant.ChatUserPic);
                    String nickName = message.getStringAttribute(SharePrefConstant.ChatUserNick);
                    Log.d("TAG", "chatUserId-->" + chatUserId + "avatarUrl-->" + avatarUrl + "nickName-->" + nickName);
                    UserInfoCacheSvc.createOrUpdate(chatUserId, nickName, avatarUrl);

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            //   refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
            for (EMMessage message : messages) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();//获取自定义action
                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
                    RedPacketUtil.receiveRedPacketAckMessage(message);
                }
            }
            //end of red packet code
            //  refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };
    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;
                    return true;
                } else {
                    //MyActivityManager.getInstance().moveTaskToBack(mContext);// 不退出，后台运行
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}


