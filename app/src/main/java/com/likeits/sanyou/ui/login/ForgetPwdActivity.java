package com.likeits.sanyou.ui.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.likeits.sanyou.R;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.EmptyEntity;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.utils.AndroidWorkaround;
import com.likeits.sanyou.utils.StringUtil;
import com.likeits.sanyou.utils.ToastUtil;
import com.likeits.sanyou.utils.UtilPreference;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

public class ForgetPwdActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.forget_pwd_et_phone)
    EditText edPhone;
    @BindView(R.id.forget_pwd_et_code)
    EditText edCode;
    @BindView(R.id.forget_pwd_et_pwd)
    EditText edPwd;
    @BindView(R.id.forget_pwd_et_confirm_pwd)
    EditText edConfirmPwd;
    @BindView(R.id.send_code_btn)
    TextView tvSendCode;
    @BindView(R.id.forget_pwd_btn)
    TextView tvRegister;
    @BindView(R.id.web_layout01)
    LinearLayout llLogin;
    private String phone, code, pwd, confirmPwd;
    TimeCount time = new TimeCount(60000, 1000);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
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
        initView();
    }

    private void initView() {
        tvHeader.setText("忘记密码");
    }

    @OnClick({R.id.send_code_btn, R.id.forget_pwd_btn, R.id.web_layout01,R.id.backBtn})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.send_code_btn:
                sendCode();
                break;
            case R.id.forget_pwd_btn:
                commit();
                break;
            case R.id.web_layout01:
                toActivityFinish(LoginActivity.class);
                break;
        }
    }

    private void sendCode() {
        phone = edPhone.getText().toString().trim();
        if (StringUtil.isBlank(phone)) {
            ToastUtil.showS(mContext, "手机号不能为空");
            return;
        }
        if (!(StringUtil.isCellPhone(phone))) {
            ToastUtil.showS(mContext, "请输入正确的手机号码");
            return;
        } else {
            SMSSDK.getVerificationCode("86", phone);
            time.start();
        }
    }

    private void commit() {
        phone = edPhone.getText().toString().trim();
        code = edCode.getText().toString().trim();
        pwd = edPwd.getText().toString().trim();
        confirmPwd = edConfirmPwd.getText().toString().trim();

        if (StringUtil.isBlank(code)) {
            ToastUtil.showS(mContext, "验证码不能为空");
            return;
        }
        if (StringUtil.isBlank(pwd) || StringUtil.isBlank(confirmPwd)) {
            ToastUtil.showS(mContext, "密码不能为空");
            return;
        }
        SMSSDK.submitVerificationCode("86", phone, code);
       // Commit();
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
                            Commit();
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
    private void Commit() {
        HttpMethods.getInstance().ResetPwd(new MySubscriber<EmptyEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<EmptyEntity> emptyEntityHttpResult) {
                if(emptyEntityHttpResult.getCode()==1){
                    showToast("修改成功");
                    UtilPreference.saveString(mContext,"phone",phone);
                    UtilPreference.saveString(mContext,"password",pwd);
                    finish();
                }else {
                    showToast(emptyEntityHttpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        },phone,pwd,confirmPwd);}
}
