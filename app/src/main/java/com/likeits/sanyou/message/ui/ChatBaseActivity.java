package com.likeits.sanyou.message.ui;

import android.os.Bundle;

import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.likeits.sanyou.utils.MyActivityManager;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2017/8/17.
 */

public class ChatBaseActivity extends EaseBaseActivity {
    private ChatBaseActivity mContext;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mContext = this;
//        setMiuiStatusBarDarkMode(this, true);
//        Window window = this.getWindow();
//        // 透明状态栏
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        // 透明导航栏
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
//            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
//        }
//        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
//            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
//        }
        MyActivityManager.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // umeng
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // umeng
        MobclickAgent.onPause(this);
    }
}
