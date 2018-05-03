package com.likeits.sanyou.ui.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.likeits.sanyou.R;
import com.likeits.sanyou.app.MyApplication;
import com.likeits.sanyou.message.utils.UserInfoCacheSvc;
import com.likeits.sanyou.message.widget.DemoHelper;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.EmptyEntity;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.MainActivity;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.utils.AndroidWorkaround;
import com.likeits.sanyou.utils.MyActivityManager;
import com.likeits.sanyou.utils.StringUtil;
import com.likeits.sanyou.utils.ToastUtil;
import com.likeits.sanyou.utils.UtilPreference;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.register_et_phone)
    EditText edPhone;
    @BindView(R.id.register_et_code)
    EditText edCode;
    @BindView(R.id.register_et_pwd)
    EditText edPwd;
    @BindView(R.id.register_et_confirm_pwd)
    EditText edConfirmPwd;
    @BindView(R.id.send_code_btn)
    TextView tvSendCode;
    @BindView(R.id.register_btn)
    TextView tvRegister;
    @BindView(R.id.web_layout01)
    LinearLayout llLogin;
    @BindView(R.id.tv_header)
    TextView tvHeader;
    private String mobile, code, password, confirmPwd;
    TimeCount time = new TimeCount(60000, 1000);
    private String pass1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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


        ButterKnife.bind(this);
        pass1 = "8df3a661beafd7e669137d9185699296";
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
        tvHeader.setText("注册");
    }

    @OnClick({R.id.send_code_btn, R.id.register_btn, R.id.web_layout01, R.id.backBtn})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.send_code_btn:
                sendCode();
                break;
            case R.id.register_btn:
                register();
                break;
            case R.id.web_layout01:
                toActivityFinish(UserAgreementActivity.class);
                break;
        }
    }

    private void sendCode() {
        mobile = edPhone.getText().toString().trim();
        if (StringUtil.isBlank(mobile)) {
            ToastUtil.showS(mContext, "手机号不能为空");
            return;
        }
        if (!(StringUtil.isCellPhone(mobile))) {
            ToastUtil.showS(mContext, "请输入正确的手机号码");
            return;
        } else {
            SMSSDK.getVerificationCode("86", mobile);
            time.start();
        }
    }

    private void register() {
        mobile = edPhone.getText().toString().trim();
        code = edCode.getText().toString().trim();
        password = edPwd.getText().toString().trim();
        confirmPwd = edConfirmPwd.getText().toString().trim();

        if (StringUtil.isBlank(code)) {
            ToastUtil.showS(mContext, "验证码不能为空");
            return;
        }
        if (StringUtil.isBlank(password) || StringUtil.isBlank(confirmPwd)) {
            ToastUtil.showS(mContext, "密码不能为空");
            return;
        }
        SMSSDK.submitVerificationCode("86", mobile, code);
        //Register();//测试不需要验证码
//        showProgress("Loading...");
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            tvSendCode.setText("获取验证码");
            tvSendCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            tvSendCode.setClickable(false);//防止重复点击
            tvSendCode.setText(millisUntilFinished / 1000 + "s");
        }
    }

    Handler mHandler = new

            Handler() {
                public void handleMessage(Message msg) {

                    // TODO Auto-generated method stub
                    super.handleMessage(msg);
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    Log.e("event", "event=" + event);
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        System.out.println("--------result" + event);
                        //短信注册成功后，返回MainActivity,然后提示新好友
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                            //Toast.makeText(getApplicationContext(), "提交验证码成功"+data.toString(), Toast.LENGTH_SHORT).show();
//                            showProgress("Loading...");
                            Register();
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //已经验证
                            Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();


                        }

                    } else {
                        int status = 0;
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");
                            status = object.optInt("status");
                            if (!TextUtils.isEmpty(des)) {
                                Toast.makeText(mContext, des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }
                    }


                }
            };

    private void Register() {
        HttpMethods.getInstance().RegisterUser(new MySubscriber<EmptyEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult httpResult) {
                if (httpResult.getCode() == 1) {
                    showToast("注册成功");
                    try {
                        JSONObject obj = new JSONObject(httpResult.getData().toString());
                        UtilPreference.saveString(mContext, "ukey", obj.optString("ukey"));
                        UtilPreference.saveString(mContext, "easemob_id", obj.optString("easemob_id"));
                        UtilPreference.saveString(mContext, "phone", mobile);
                        UtilPreference.saveString(mContext, "pwd", password);
                        UtilPreference.saveString(mContext, "nickname", obj.optString("nickname"));
                        UtilPreference.saveString(mContext, "headimg", obj.optString("headimg"));
                        UtilPreference.saveString(mContext, "qq", obj.optString("qq"));
                        UtilPreference.saveInt(mContext, "sex", obj.optInt("sex"));//0:男  1：女
                        UtilPreference.saveString(mContext, "birthday", obj.optString("birthday"));
                        UtilPreference.saveString(mContext, "address", obj.optString("address"));
                        UserInfoCacheSvc.createOrUpdate(obj.optString("easemob_id"), obj.getString("nickname"), obj.getString("headimg"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    signin();
                    //toActivityFinish(MainActivity.class);
                } else {
                    showToast(httpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, mobile, password);
//    ile, password);
    }

    private void signin() {
        EMClient.getInstance().login(mobile, pass1, new EMCallBack() {
            @Override
            public void onSuccess() {
                disShowProgress();
                Log.d("TAG", "EM登录成功");
                Log.d("TAG", "currentUsername-->" + mobile);
                Log.d("TAG", "passwd1-->" + pass1);
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        MyApplication.currentUserNick.trim());
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
//                toActivityFinish(UploadImgActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("keys", "2");
                toActivity(MainActivity.class,bundle);
                finish();
            }

            @Override
            public void onError(int i, String s) {
                disShowProgress();
                //  Toast.makeText(mContext,"EM登录失败",Toast.LENGTH_LONG).show();
                // ToastUtil.showS(mContext, "EM登录失败");
                Log.d("TAG", "EM登录失败");
                Log.d("TAG", s);
                Log.d("TAG", "currentUsername-->" + mobile);
                Log.d("TAG", "passwd1-->" + pass1);
                showErrorMsg("登录失败");
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
