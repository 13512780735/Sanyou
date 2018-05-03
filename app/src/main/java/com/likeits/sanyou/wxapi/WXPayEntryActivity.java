package com.likeits.sanyou.wxapi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.likeits.sanyou.R;
import com.likeits.sanyou.ui.MainActivity;
import com.likeits.sanyou.ui.base.Container;
import com.likeits.sanyou.ui.shop.ShopIndentActivity;
import com.likeits.sanyou.utils.MyActivityManager;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Container implements IWXAPIEventHandler {

    private IWXAPI api;
    String payKey;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        // String WX_APPID = "wx307bf5fa134ffacd";
        String WX_APPID = "wx3a8eff0e88bd0527";
        MyActivityManager.getInstance().addActivity(this);
        api = WXAPIFactory.createWXAPI(this, WX_APPID);
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Toast.makeText(getApplicationContext(), "onReq", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int code = resp.errCode;
            Log.d("TAg", code + "");
            if (code == 0) {
                new AlertDialog.Builder(this).setMessage("支付订单成功！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(WXPayEntryActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("keys", "1");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        MyActivityManager.getInstance().finishAllActivity();
                    }
                }).setTitle("微信支付结果").setCancelable(false).show();

            } else if (code == -2) {
                // Toast.makeText(this, "取消支付", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(this).setMessage("取消支付").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(WXPayEntryActivity.this, ShopIndentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("status", "0");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                }).setTitle("微信支付结果").setCancelable(false).show();

            } else {
                // Toast.makeText(this, "支付出错：" + resp.errStr, Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(this).setMessage("交易出错").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(WXPayEntryActivity.this, ShopIndentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("status", "0");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                }).setTitle("微信支付结果").setCancelable(false).show();
            }

        }

    }


}
