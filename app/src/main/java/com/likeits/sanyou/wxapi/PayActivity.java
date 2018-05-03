package com.likeits.sanyou.wxapi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.likeits.sanyou.R;
import com.likeits.sanyou.alipay.PayResult;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.MainActivity;
import com.likeits.sanyou.ui.base.Container;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.MyActivityManager;
import com.likeits.sanyou.utils.ToastUtil;
import com.likeits.sanyou.utils.UtilPreference;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PayActivity extends Container implements OnClickListener {
    @BindView(R.id.backBtn)
    Button btBack;
    @BindView(R.id.tv_header)
    TextView tvTitle;
    @BindView(R.id.ck_ddhk_weixin_sum)
    TextView tvWeixin;
    @BindView(R.id.ck_ddhk_zhifubao_sum)
    TextView tvZfb;
    @BindView(R.id.ddhk_pay_weixin)
    RelativeLayout rlweixin;
    @BindView(R.id.ddhk_pay_zhifubao)
    RelativeLayout rlZfb;

    String id;
    String sum;
    String sn;
    private IWXAPI api;
    private String key;


    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PayActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("keys", "1");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        MyActivityManager.getInstance().finishAllActivity();

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "resultStatus-->" + resultStatus);

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        MyActivityManager.getInstance().addActivity(this);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        sum = intent.getStringExtra("sum");
        sn = intent.getStringExtra("sn");
        String WX_APPID = "wx3a8eff0e88bd0527";
        api = WXAPIFactory.createWXAPI(this, WX_APPID, false);
        api.registerApp(WX_APPID);
        initView();
    }

    private void initView() {
        btBack.setOnClickListener(this);
        rlweixin.setOnClickListener(this);
        rlZfb.setOnClickListener(this);
        tvTitle.setText("支付订单");
        tvWeixin.setText("共计：¥" + sum + "元");
        tvZfb.setText("共计：¥" + sum + "元");
        Log.d("TAG", "sn-->" + sn);
        Log.d("TAG", "id-->" + id);
        Log.d("TAG", "ukey-->" + ukey);
        Log.d("TAG", "sum-->" + sum);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            //微信支付
            case R.id.ddhk_pay_weixin:
                inidata();
                showProgress("Loading...");
                break;
            //支付宝支付
            case R.id.ddhk_pay_zhifubao:
             //  ToastUtil.showS(mContext, "暂未开通此服务");
                  initData1();
                showProgress("Loading...");

        }
    }

    private void alipay(String data) {

       final String payInfo = data;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    private void initData1() {
        String url = MyApiService.Alipay;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("sn", sn);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                disShowProgress();
                Log.d("TAG", "api-->" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.optString("code");
                    String message = obj.optString("message");
                    if ("1".equals(code)) {
                       String data = obj.optString("data");
                        alipay(data);
                    } else {
                        ToastUtil.showS(mContext, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                disShowProgress();
                ToastUtil.showS(mContext, "网络请求失败");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                disShowProgress();
            }
        });
    }


    private void inidata() {
        //ToastUtil.showS(this, "点击了");
        String url = MyApiService.WXPay;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(mContext, "ukey"));
        params.put("sn", sn);
       ToastUtil.showL(mContext, "ukey-->" + UtilPreference.getStringValue(mContext, "ukey") + "  sn-->" + sn);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                disShowProgress();
                //ToastUtil.showS(mContext, response.toString());
                Log.d("TAG", "respone-->" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.getString("code");
                    String message = obj.getString("message");
                    if ("1".equals(code)) {
                        JSONObject data = obj.getJSONObject("data");
                        String appId = data.optString("appid");
                        String partnerId = data.optString("mch_id");
                        String prepayId = data.optString("prepay_id");
                        String nonceStr = data.optString("nonce_str");
                        String packageValue = "Sign=Wxpay";
                        long timeMills = System.currentTimeMillis() / 1000;
                        String timeStamp = String.valueOf(timeMills);
                        String stringA =
                                "appid=" + appId
                                        + "&noncestr=" + nonceStr
                                        + "&package=" + packageValue
                                        + "&partnerid=" + partnerId
                                        + "&prepayid=" + prepayId
                                        + "&timestamp=" + timeStamp;
                     //   String key = "9ijy7876yuio987yhjkfjdklkjhy6543";
                        //String key = "177b59d8b56ae5c63145d9824f661020";
                        String key = "177b59d8b56ae5c63145d9824f661020";
                        String stringSignTemp = stringA + "&key=" + key;
                        String sign = MD5.getMessageDigest(stringSignTemp.getBytes()).toUpperCase();
                        Log.d("TAG", "sign-->" + sign);
                        Log.d("TAG", "appId-->" + appId+"partnerId-->" + partnerId+"prepayId-->" + prepayId+"nonceStr-->" + nonceStr+"packageValue-->" + packageValue);
                        sendPayred(appId, partnerId, prepayId, nonceStr, packageValue, sign, timeStamp);
                    }
                } catch (
                        JSONException e)

                {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                disShowProgress();
                ToastUtil.showS(mContext, "网络请求失败！");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                disShowProgress();
            }
        });
    }

    private void sendPayred(String appId, String partnerId, String prepayId, String nonceStr, String packageValue, String sign, String timeStamp) {
        PayReq request = new PayReq();
        request.appId = appId;
        request.partnerId = partnerId;
        request.prepayId = prepayId;
        request.nonceStr = nonceStr;
        request.packageValue = packageValue;
        request.sign = sign;
        request.timeStamp = timeStamp;
        api.sendReq(request);
    }


}
