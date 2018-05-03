package com.likeits.sanyou.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.likeits.sanyou.R;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.utils.AndroidWorkaround;
import com.likeits.sanyou.utils.MyActivityManager;

import java.util.Timer;
import java.util.TimerTask;

public class GuideActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        MyActivityManager.getInstance().addActivity(this);
        Window window = this.getWindow();
        // 透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        // 透明导航栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }
        final Intent intent = new Intent(this, LoginActivity.class);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(task, 3000);// 此处的Delay可以是3*1000，代表三秒
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
