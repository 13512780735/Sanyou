package com.likeits.sanyou.ui.me;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.likeits.sanyou.R;
import com.likeits.sanyou.message.widget.DemoHelper;
import com.likeits.sanyou.ui.base.Container;
import com.likeits.sanyou.ui.login.LoginActivity;
import com.likeits.sanyou.utils.DataCleanManager;
import com.likeits.sanyou.utils.MyActivityManager;

import java.text.DecimalFormat;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Setting02Activity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_tvClear)
    TextView tv_tvClear;
    private DecimalFormat df;
    private double y;
    private String cacheSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting02);
        ButterKnife.bind(this);
        Random r = new Random();
        y = r.nextDouble() * (10 - 0) + 0;
        df = new DecimalFormat("#.0");//保留2位小数
        initView();
    }

    private void initView() {
        tvHeader.setText("设置");
       // tv_tvClear.setText(df.format(y)+"M");
        refreshCache();
    }

    @OnClick({R.id.backBtn, R.id.tv_logout, R.id.tv_rlAbout,R.id.tv_rlClear})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_logout:
                logout();
                break;
            case R.id.tv_rlAbout:
                toActivity(AboutActivity.class);
                break;
            case R.id.tv_rlClear:
//                tv_tvClear.setText(0.0+"M");
//                showToast("清除成功！");
                DataCleanManager.cleanApplicationData(mContext);
                refreshCache();
                break;

        }
    }
    public void refreshCache(){
        try {
            cacheSize=  DataCleanManager.getTotalCacheSize(mContext);
            tv_tvClear.setText(cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void logout() {
        if (DemoHelper.getInstance().isLoggedIn()) {
            DemoHelper.getInstance().logout(true, new EMCallBack() {

                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            // show login screen
                            toActivityFinish(LoginActivity.class);
                            MyActivityManager.getInstance().finishAllActivity();

                        }
                    });
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Toast.makeText(mContext, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            toActivityFinish(LoginActivity.class);
            //MyActivityManager.getInstance().finishAllActivity();
        }

    }
}
