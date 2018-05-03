package com.likeits.sanyou.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.hyphenate.EMConnectionListener;
import com.likeits.sanyou.R;
import com.likeits.sanyou.message.widget.DemoHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import java.util.Iterator;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;

public class MyApplication extends Application {

    public static MyApplication mContext;
    private static MyApplication instance;
    //  private UserInfo userInfo = null;
    public final String PREF_USERNAME = "username";
    public static String currentUserNick = "";
    private EMConnectionListener connectionListener;
    public static MyApplication getInstance() {
        if (mContext == null) {
            return new MyApplication();
        } else {
            return mContext;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        instance = this;
        initUpdate();//腾讯bugly更新
        initHuanXin();
     //   initRedPacket();
        initMob();
        // 初始化环信SDK
        //initEasemob();
        // 图片加载工具初始化
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_public_nophoto)
                .showImageOnFail(R.mipmap.ic_public_nophoto)
                .cacheInMemory(true).cacheOnDisc(true).build();
        // 图片加载工具配置
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)// 缓存一百张图片
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
//        //初始化
//       Beta.autoCheckUpgrade = true;//设置自动检查
//        Bugly.init(mContext, "6441c3da7a", false);
    }
    private boolean isInit;

    private void initUpdate() {
        Bugly.init(getApplicationContext(), "eccf1615b8", false);
        Beta.autoCheckUpgrade = true;
        Beta.upgradeCheckPeriod = 60 * 60 * 1000;
        Beta.largeIconId = R.mipmap.ic_launcher;

    }
    private void initHuanXin() {
        DemoHelper.getInstance().init(mContext);
        //设置全局监听
        //setGlobalListeners();
    }

    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @param pid 进程的id
     * @return 返回进程的名字
     */
    private String getAppName(int pid) {
        String processName = null;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    processName = info.processName;
                    // 返回当前进程名
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }


    private void initMob() {
        ShareSDK.initSDK(mContext);
        SMSSDK.initSDK(mContext, "22ad2e1151879", "177b59d8b56ae5c63145d9824f661020");
    }


    public static MyApplication getInstance(Context appContext) {
        return instance;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
//    public UserInfo getUserInfo() {
//        if (userInfo == null)
//            init();
//        return userInfo;
//    }
}
//    private void init() {
//        userInfo = Storage.getObject(AppConfig.USER_INFO, UserInfo.class);
//
//    }

/**
 * 登录信息的保存
 *
 * @param user
 */
//    public static void doLogin(UserInfo user) {
//        Storage.saveObject(AppConfig.USER_INFO, user);
//        Storage.saveObject(AppConfig.USER_INFO, user);
//        Preferences
//                .saveString(AppConfig.USER_ID, user.getUkey(), getInstance());
//        MyApplication.getInstance().init();
//    }

//    /**
//     * 清除登录信息(退出账号)
//     */
//    public static void doLogout() {
//        Storage.clearObject(AppConfig.USER_INFO);
//        MyApplication.getInstance().init();
//    }
//}