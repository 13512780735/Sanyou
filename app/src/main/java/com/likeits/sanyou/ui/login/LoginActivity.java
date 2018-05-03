package com.likeits.sanyou.ui.login;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.likeits.sanyou.R;
import com.likeits.sanyou.app.MyApplication;
import com.likeits.sanyou.entity.LoginUserInfoEntity;
import com.likeits.sanyou.message.db.DemoDBManager;
import com.likeits.sanyou.message.utils.UserInfoCacheSvc;
import com.likeits.sanyou.message.widget.DemoHelper;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.MainActivity;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.utils.AndroidWorkaround;
import com.likeits.sanyou.utils.LoaddingDialog;
import com.likeits.sanyou.utils.MyActivityManager;
import com.likeits.sanyou.utils.StringUtil;
import com.likeits.sanyou.utils.ToastUtil;
import com.likeits.sanyou.utils.UtilPreference;
import com.tencent.bugly.beta.Beta;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_et_phone)
    EditText edPhone;
    @BindView(R.id.login_et_pwd)
    EditText edpwd;
    @BindView(R.id.login_tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R.id.login_tv_register)
    TextView tvRegister;
    @BindView(R.id.login_tv_login)
    TextView tvLogin;
    private String phone;
    private String pwd;
    private String pass1;
    private LoaddingDialog loaddingDialog;
    private boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        loaddingDialog = new LoaddingDialog(this);
        loaddingDialog.setCanceledOnTouchOutside(false);

        loaddingDialog.setCancelable(false);
        pass1 = "8df3a661beafd7e669137d9185699296";
        openPermission();
        phone = UtilPreference.getStringValue(mContext, "phone");
        pwd = UtilPreference.getStringValue(mContext, "pwd");
        edPhone.setText(phone);
        edpwd.setText(pwd);

        check = UtilPreference.getBooleanValue(mContext, "check");
        checkVersion();
//        if (!check) {
//           checkVersion();
//            check = false;
//            UtilPreference.saveBoolean(mContext, "check", check);
//        } else {
//            return;
 //       }
    }

    private void checkVersion() {
        Beta.checkUpgrade();//检查版本号
        //  Beta.autoCheckUpgrade = false;//设置不自动检查
    }
    private void openPermission() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE + Manifest.permission.CAMERA + Manifest.permission.WRITE_EXTERNAL_STORAGE
                + Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(mContext,"请授予下面权限",Toast.LENGTH_SHORT).show();
            List<PermissionItem> permissions = new ArrayList<PermissionItem>();
            permissions.add(new PermissionItem(Manifest.permission.CALL_PHONE, "电话", R.drawable.permission_ic_phone));
            permissions.add(new PermissionItem(Manifest.permission.CAMERA, "照相", R.drawable.permission_ic_camera));
            permissions.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "定位", R.drawable.permission_ic_location));
            permissions.add(new PermissionItem(Manifest.permission.ACCESS_COARSE_LOCATION, "定位", R.drawable.permission_ic_location));
            permissions.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, "录音", R.drawable.permission_ic_micro_phone));
            permissions.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "储存空间", R.drawable.permission_ic_storage));
            HiPermission.create(mContext)
                    .permissions(permissions)
                    .msg("为了您正常使用三友钓鱼应用，需要以下权限")
                    .animStyle(R.style.PermissionAnimModal)
                    .checkMutiPermission(new PermissionCallback() {
                        @Override
                        public void onClose() {
                            Log.i(TAG, "onClose");
                            ToastUtil.showS(mContext, "权限被拒绝");
                        }

                        @Override
                        public void onFinish() {
                            //ToastUtil.showS(mContext,"权限已被开启");
                        }

                        @Override
                        public void onDeny(String permission, int position) {
                            Log.i(TAG, "onDeny");
                        }

                        @Override
                        public void onGuarantee(String permission, int position) {
                            Log.i(TAG, "onGuarantee");
                        }
                    });
            return;
        }
    }

    @OnClick({R.id.login_tv_forget_pwd, R.id.login_tv_register, R.id.login_tv_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_tv_login:
                login();
                break;
            case R.id.login_tv_register:
                toActivity(RegisterActivity.class);
                break;
            case R.id.login_tv_forget_pwd:
                toActivity(ForgetPwdActivity.class);
                break;
        }
    }

    private void login() {
        phone = edPhone.getText().toString().trim();
        pwd = edpwd.getText().toString().trim();
        if (StringUtil.isBlank(phone)) {
            ToastUtil.showS(mContext, "手机号不能为空");
            return;
        }
        if (!StringUtil.isCellPhone(phone)) {
            ToastUtil.showS(mContext, "请输入正确的手机号");
            return;
        }
        if (StringUtil.isBlank(pwd)) {
            ToastUtil.showS(mContext, "密码不能为空");
            return;
        }
        Logining();

    }

    private void Logining() {
        //loaddingDialog.show();
        HttpMethods.getInstance().Login(new MySubscriber<LoginUserInfoEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<LoginUserInfoEntity> loginUserInfoEntityHttpResult) {
                if (loginUserInfoEntityHttpResult.getCode() == 1) {
                    UtilPreference.saveString(mContext, "ukey", loginUserInfoEntityHttpResult.getData().getUkey());
                    UtilPreference.saveString(mContext, "easemob_id", loginUserInfoEntityHttpResult.getData().getEasemob_id());
                    UtilPreference.saveString(mContext, "phone", phone);
                    UtilPreference.saveString(mContext, "pwd", pwd);
                    UtilPreference.saveString(mContext, "nickname", loginUserInfoEntityHttpResult.getData().getNickname());
                    UtilPreference.saveString(mContext, "headimg", loginUserInfoEntityHttpResult.getData().getHeadimg());
                    UtilPreference.saveString(mContext, "qq", loginUserInfoEntityHttpResult.getData().getQq());
                    UtilPreference.saveString(mContext, "sex", loginUserInfoEntityHttpResult.getData().getSex());//0:男  1：女
                    UtilPreference.saveString(mContext, "birthday", loginUserInfoEntityHttpResult.getData().getBirthday());
                    UtilPreference.saveString(mContext, "address", loginUserInfoEntityHttpResult.getData().getAddress());
                    UtilPreference.saveString(mContext, "collections", loginUserInfoEntityHttpResult.getData().getCollections());
                    UtilPreference.saveString(mContext, "friends", loginUserInfoEntityHttpResult.getData().getFriends());
                    UtilPreference.saveString(mContext, "articles", loginUserInfoEntityHttpResult.getData().getArticles());
                    UtilPreference.saveString(mContext, "role", loginUserInfoEntityHttpResult.getData().getRole());
                    UserInfoCacheSvc.createOrUpdate(loginUserInfoEntityHttpResult.getData().getEasemob_id(), loginUserInfoEntityHttpResult.getData().getNickname(), loginUserInfoEntityHttpResult.getData().getHeadimg());
                    logout();

                    DemoDBManager.getInstance().closeDB();
                    DemoHelper.getInstance().setCurrentUserName(phone);
                    //  toActivityFinish(MainActivity.class);
                } else {
                    loaddingDialog.dismiss();
                    showToast(loginUserInfoEntityHttpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, phone, pwd);
    }

    private void logout() {
        DemoHelper.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d("TAG", "EM退出成功");
                        signin();

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
    }

    private void signin() {
        EMClient.getInstance().login(phone, pass1, new EMCallBack() {
            @Override
            public void onSuccess() {
                loaddingDialog.dismiss();
                Log.d("TAG", "EM登录成功");
                Log.d("TAG", "currentUsername-->" + phone);
                Log.d("TAG", "passwd1-->" + pass1);
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        MyApplication.currentUserNick.trim());
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
//                toActivityFinish(UploadImgActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("keys", "2");
                toActivity(MainActivity.class, bundle);
                finish();
            }

            @Override
            public void onError(int i, String s) {
                loaddingDialog.dismiss();
                //  Toast.makeText(mContext,"EM登录失败",Toast.LENGTH_LONG).show();
                // ToastUtil.showS(mContext, "EM登录失败");
                Log.d("TAG", "EM登录失败");
                Log.d("TAG", s);
                Log.d("TAG", "currentUsername-->" + phone);
                Log.d("TAG", "passwd1-->" + pass1);
                showErrorMsg("登录失败");
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
