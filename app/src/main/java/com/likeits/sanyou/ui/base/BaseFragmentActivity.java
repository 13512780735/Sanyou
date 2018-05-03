package com.likeits.sanyou.ui.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hwangjr.rxbus.RxBus;
import com.likeits.sanyou.R;
import com.likeits.sanyou.utils.LoaddingDialog;
import com.likeits.sanyou.utils.MyActivityManager;
import com.likeits.sanyou.utils.ToastUtil;
import com.likeits.sanyou.utils.UtilPreference;

/**
 * Created by Administrator on 2017/12/5.
 */

public class BaseFragmentActivity extends FragmentActivity {
    protected final static int DATA_LOAD_ING = 0x001;
    protected final static int DATA_LOAD_COMPLETE = 0x002;
    protected final static int DATA_LOAD_FAIL = 0x003;

    public static Handler handler = new Handler();

    /**
     * 上下文 当进入activity时必须 mContext = this 方可使用，否则会报空指针
     */
    public Context mContext;

    /**
     * 加载等待效果
     */
    public ProgressDialog progress;
    private LoaddingDialog loaddingDialog;
    protected String ukey;

    /**
     * 初始化创建
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityManager.getInstance().addActivity(this);
        mContext = this;
        loaddingDialog = new LoaddingDialog(mContext);
        RxBus.get().register(this);
        ukey = UtilPreference.getStringValue(this, "ukey");
//        Window window = this.getWindow();
//        // 透明状态栏
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        // 透明导航栏
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
//            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
//        }
    }

    /**
     * 重回前台显示调用
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Activity销毁，关闭加载效果
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
        LoaddingDismiss();
        if (progress != null) {
            progress.dismiss();
        }
    }

    public void LoaddingShow() {
        if (loaddingDialog == null) {
            loaddingDialog = new LoaddingDialog(mContext);
        }

        if (!loaddingDialog.isShowing()) {
            loaddingDialog.show();
        }
    }
    public void LoaddingDismiss() {
        if (loaddingDialog != null && loaddingDialog.isShowing()) {
            loaddingDialog.dismiss();
        }
    }
    /**
     * Activity暂停，关闭加载效果
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (progress != null) {
            progress.dismiss();
        }
    }

    /**
     * Activity停止，关闭加载效果
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (progress != null) {
            progress.dismiss();
        }
    }

    /**
     * 初始化标题和回退
     */
    public void initTitle(String title) {
        if (textView(R.id.tv_header) != null) {
            textView(R.id.tv_header).setText(title);
        }
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
        }
    }

    /**
     * 触发手机返回按钮
     */
    @Override
    public void onBackPressed() {
        MyActivityManager.getInstance().removeActivity(BaseFragmentActivity.this);
    }

    /**
     * 滚动条超时
     *
     * @author mo
     */
    @SuppressWarnings("unused")
    private class ProgressTimeOut extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(30000);
                Message message1 = new Message();
                BaseFragmentActivity.this.disProgressHander.sendMessage(message1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 消息队列方式显示进度
     */
    @SuppressLint("HandlerLeak")
    public Handler progressHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (progress != null) {
                progress.dismiss();
            }
            progress = new ProgressDialog(BaseFragmentActivity.this);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setMessage(msg.obj.toString());
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(true);
            progress.show();
        }
    };

    /**
     * 隐藏消息队列进度的显示
     */
    public Handler disProgressHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (progress != null) {
                progress.dismiss();
            }
        }
    };

    /**
     * 显示字符串消息
     *
     * @param message
     */
    public void showProgress(String message) {
        if (progress != null) {
            progress.dismiss();
        }
        progress = new ProgressDialog(BaseFragmentActivity.this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage(message);
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(true);
        progress.show();
    }

    /**
     * 隐藏字符串消息
     */
    public void disShowProgress() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    /**
     * 提示信息
     *
     */
    public void showErrorMsg(String message) {
        final String str = message;
        BaseActivity.handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), str,
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    /**
     * 提示信息号或请求失败信息
     * <p>
     * showErrorRequestFair
     *
     */
    public void showE404() {
        BaseActivity.handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "手机信号差或服务器维护，请稍候重试。谢谢！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    /**
     * 提示信息
     *
     */
    public void showMsgAndDisProgress(String message) {
        final String str = message;
        BaseActivity.handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), str,
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                disShowProgress();
            }
        });
    }

    /**
     * 文本View
     */
    public TextView textView(int textview) {
        return (TextView) findViewById(textview);
    }

    /**
     * 文本button
     */
    public Button button(int id) {
        return (Button) findViewById(id);
    }

    /**
     * 文本button
     */
    public ImageView imageView(int id) {
        return (ImageView) findViewById(id);
    }

    /**
     * 文本editText
     */
    public EditText editText(int id) {
        return (EditText) findViewById(id);
    }

    /**
     * 显示数据加载状态
     *
     * @param loading
     * @param datas
     * @param type
     */
    public void viewSwitch(View loading, View datas, int type) {
        switch (type) {
            case DATA_LOAD_ING:
                datas.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                break;
            case DATA_LOAD_COMPLETE:
                datas.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                break;
            case DATA_LOAD_FAIL:
                datas.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                break;
        }
    }

    protected void toFinish() {
        finish();
    }

    public void toActivityFinish(Class activity) {
        Intent intent = new Intent(mContext, activity);
        startActivity(intent);
        toFinish();
    }

    public void toActivity(Class activity) {
        Intent intent = new Intent(mContext, activity);
        startActivity(intent);
    }

    public void toActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(mContext, activity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void showToast(String msg) {
        ToastUtil.showS(mContext, msg);
    }
}
