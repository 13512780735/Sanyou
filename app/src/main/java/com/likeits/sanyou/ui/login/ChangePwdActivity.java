package com.likeits.sanyou.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.likeits.sanyou.R;
import com.likeits.sanyou.message.widget.DemoHelper;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.EmptyEntity;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.utils.MyActivityManager;
import com.likeits.sanyou.utils.StringUtil;
import com.likeits.sanyou.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePwdActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.change_pwd_et_oldpwd)
    EditText et_oldpwd;
    @BindView(R.id.change_pwd_et_newpwd)
    EditText et_newpwd;
    @BindView(R.id.change_pwd_et_confirm_pwd)
    EditText et_confirm_pwd;
    private String oldPwd;
    private String newPwd;
    private String confirmPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvHeader.setText("修改密码");
    }

    @OnClick({R.id.forget_pwd_btn, R.id.backBtn})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.forget_pwd_btn:
                sendCode();
                break;
        }
    }

    private void sendCode() {
        oldPwd = et_oldpwd.getText().toString().trim();
        newPwd = et_newpwd.getText().toString().trim();
        confirmPwd = et_confirm_pwd.getText().toString().trim();
        if (StringUtil.isBlank(oldPwd)) {
            ToastUtil.showS(mContext, "原密码不能为空");
            return;
        }
        if (StringUtil.isBlank(newPwd)) {
            ToastUtil.showS(mContext, "新密码不能为空");
            return;
        }
        if (StringUtil.isBlank(confirmPwd)) {
            ToastUtil.showS(mContext, "确认密码不能为空");
            return;
        }
//        String url = MyApiService.ChangePwd;
//        RequestParams params = new RequestParams();
//        params.put("ukey", ukey);
//        params.put("old_pwd", oldPwd);
//        params.put("pwd", newPwd);
//        params.put("rpwd", confirmPwd);
//        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
//            @Override
//            public void success(String response) {
//                try {
//                    JSONObject obj = new JSONObject(response);
//                    String code = obj.optString("code");
//                    if ("1".equals(code)) {
//                        showToast("修改密码成功");
//                        Logout();
//                    }else{
//                        showToast(obj.optString("message"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void failed(Throwable e) {
//
//            }
//        });
        HttpMethods.getInstance().ChangePwd(new MySubscriber<EmptyEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<EmptyEntity> emptyEntityHttpResult) {
                if (emptyEntityHttpResult.getCode() == 1) {
                    showToast("修改密码成功");
                    Logout();
                } else {
                    showToast(emptyEntityHttpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        },ukey,oldPwd,newPwd,confirmPwd);
    }

    private void Logout() {
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
          //  MyActivityManager.getInstance().finishAllActivity();
        }
    }

}
