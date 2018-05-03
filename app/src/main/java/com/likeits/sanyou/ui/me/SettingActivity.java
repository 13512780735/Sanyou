package com.likeits.sanyou.ui.me;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.UserInfoEntity;
import com.likeits.sanyou.message.widget.DemoHelper;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.EmptyEntity;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.ui.login.ChangePwdActivity;
import com.likeits.sanyou.ui.login.LoginActivity;
import com.likeits.sanyou.utils.MyActivityManager;
import com.likeits.sanyou.utils.PhotoUtils;
import com.likeits.sanyou.utils.ToastUtils;
import com.likeits.sanyou.utils.UtilPreference;
import com.likeits.sanyou.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_Avatar)
    RelativeLayout rlAvatar;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.rl_rlnickName)
    LinearLayout llnickName;
    @BindView(R.id.ed_ednickName)
    EditText edNickName;
    @BindView(R.id.rl_rlQQ)
    LinearLayout llQQ;
    @BindView(R.id.edQQ)
    EditText edQQ;
    @BindView(R.id.rl_rlGender)
    LinearLayout llGender;
    @BindView(R.id.tv_tvGender)
    TextView tvGender;
    @BindView(R.id.rl_rlBirthday)
    LinearLayout llBirthday;
    @BindView(R.id.tv_tvBirthday)
    TextView tvBirthday;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.rl_rlAddress)
    LinearLayout llAddress;
    @BindView(R.id.rl_change_pwd)
    LinearLayout rl_change_pwd;
    @BindView(R.id.ed_edAddress)
    EditText edAddress;
    @BindView(R.id.bt_logout)
    Button btLogout;
    /**
     * 头像获取
     */
    private View layoutMenu;
    private ListView popMenuList;
    private PopupWindow popMenu;
    private String areaId;
    private PopupWindow mPopupWindow;
    private View mpopview;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    /**
     * 日期
     */
    final int DATE_DIALOG = 1;
    int mYear, mMonth, mDay;

    // 性别选择
    private View layoutGender;
    private PopupWindow popGender;
    private ListView popGenderList;
    private List<String> listGender;
    private String strItem;
    private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initData();

    }

    private void initData() {
        HttpMethods.getInstance().GetInfo(new MySubscriber<UserInfoEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<UserInfoEntity> userInfoEntityHttpResult) {
                if(userInfoEntityHttpResult.getCode()==1){
                    UtilPreference.saveString(mContext, "nickname", userInfoEntityHttpResult.getData().getNickname());
                    UtilPreference.saveString(mContext, "headimg", userInfoEntityHttpResult.getData().getHeadimg());
                    UtilPreference.saveString(mContext, "qq", userInfoEntityHttpResult.getData().getQq());
                    UtilPreference.saveString(mContext, "sex", userInfoEntityHttpResult.getData().getSex());//0:男  1：女
                    UtilPreference.saveString(mContext, "birthday", userInfoEntityHttpResult.getData().getBirthday());
                    UtilPreference.saveString(mContext, "address",userInfoEntityHttpResult.getData().getAddress());
                    initView();
                }else {
                    showToast(userInfoEntityHttpResult.getMsg());
                    initView();
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        },ukey);
    }

    private void initView() {
        tvHeader.setText("设置");
        tvRight.setText("保存");
        ImageLoader.getInstance().displayImage(UtilPreference.getStringValue(mContext, "headimg"), ivAvatar);
        edNickName.setText(UtilPreference.getStringValue(mContext, "nickname"));
        edQQ.setText(UtilPreference.getStringValue(mContext, "qq"));
        sex = UtilPreference.getStringValue(mContext, "sex");
        if ("0".equals(sex)) {
            tvGender.setText("男");
        } else {
            tvGender.setText("女");
        }
        tvBirthday.setText(UtilPreference.getStringValue(mContext, "birthday"));
        edAddress.setText(UtilPreference.getStringValue(mContext, "address"));
        tvPhone.setText(UtilPreference.getStringValue(mContext, "phone"));
    }

    @OnClick({R.id.backBtn, R.id.tv_right, R.id.rl_Avatar, R.id.rl_rlnickName, R.id.rl_rlQQ, R.id.rl_rlGender, R.id.rl_rlBirthday, R.id.rl_rlAddress, R.id.bt_logout,R.id.rl_change_pwd})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_right:

                SaveInfo();
                break;
            case R.id.rl_Avatar:
                selectAvatar();
                break;
            case R.id.rl_rlnickName:
                break;
            case R.id.rl_rlQQ:
                break;
            case R.id.rl_rlGender:
                selectGneder();
                break;
            case R.id.rl_rlAddress:
                break;
            case R.id.rl_rlBirthday:
                showDialog(DATE_DIALOG);
                break;
            case R.id.rl_change_pwd:
                //修改密码
                toActivity(ChangePwdActivity.class);
                break;
            case R.id.bt_logout:
                toActivityFinish(LoginActivity.class);
                logout();
                break;

        }
    }

    private void SaveInfo() {
        final String nickName=edNickName.getText().toString().trim();
        final String qq=edQQ.getText().toString().trim();
         String sex1=tvGender.getText().toString().trim();
        if("男".equals(sex1)){
            sex="0";
        }else sex="1";
        String birthday=tvBirthday.getText().toString().trim();
        String address=edAddress.getText().toString().trim();
        HttpMethods.getInstance().EdidInfo(new MySubscriber<EmptyEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<EmptyEntity> emptyEntityHttpResult) {
                if(emptyEntityHttpResult.getCode()==1){
                    showToast("保存成功");
                    onBackPressed();
                    finish();
                }else {
                    showToast(emptyEntityHttpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        },ukey,nickName,qq,sex,birthday,address);
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
            MyActivityManager.getInstance().finishAllActivity();
        }

    }

    @SuppressLint("InflateParams")
    private void selectGneder() {
        if (popGender != null && popGender.isShowing()) {
            popGender.dismiss();
        } else {
            layoutGender = getLayoutInflater().inflate(
                    R.layout.operationinto_popmenulist, null);
            popGenderList = (ListView) layoutGender.findViewById(R.id.menulist);
            listGender = new ArrayList<String>();
            listGender.add("选择性别");
            listGender.add("男");
            listGender.add("女");
            // 创建ArrayAdapter
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    mContext,
                    android.R.layout.simple_list_item_1, listGender);
            // 绑定适配器
            popGenderList.setAdapter(arrayAdapter);
            Log.d("TAG", "listLeft:" + popGenderList);

            // 点击listview中item的处理
            popGenderList
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            strItem = listGender.get(position);
                            if (position == 0) {
                                return;
                            } else {
                                tvGender.setText(strItem);
                                if ("男".equals(strItem)) {
                                    sex = "0";
                                } else if ("女".equals(strItem)) {
                                    sex = "1";
                                }
                            }
                            // 隐藏弹出窗口
                            if (popGender != null && popGender.isShowing()) {
                                popGender.dismiss();
                            }
                        }
                    });

            // 创建弹出窗口
            // 窗口内容为layoutLeft，里面包含一个ListView
            // 窗口宽度跟tvLeft一样
            popGender = new PopupWindow(layoutGender, llGender.getWidth(),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popGender.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.mid_filter_bg));
            popGender.setAnimationStyle(R.style.PopupAnimation);

            popGender.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popGender.setTouchable(true); // 设置popupwindow可点击
            popGender.setOutsideTouchable(true); // 设置popupwindow外部可点击
            popGender.setFocusable(true); // 获取焦点
            popGender.update();
            // 设置popupwindow的位置（相对tvLeft的位置）
            @SuppressWarnings("unused")
            int topBarHeight = llGender.getBottom();
            popGender.showAsDropDown(tvGender, 0, 10);
            popGender.setTouchInterceptor(new View.OnTouchListener() {

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // 如果点击了popupwindow的外部，popupwindow也会消失
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        popGender.dismiss();
                        return true;
                    }
                    return false;
                }
            });

        }
    }

    /**
     * 头像获取
     */
    private void selectAvatar() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mpopview = inflater.inflate(R.layout.layout_choose_photo, null);
        mPopupWindow = new PopupWindow(mpopview, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.mid_filter_bg));

        mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_setting, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mPopupWindow.setOutsideTouchable(false);
        //mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setTouchable(true); // 设置popupwindow可点击
        mPopupWindow.setOutsideTouchable(true); // 设置popupwindow外部可点击
        mPopupWindow.setFocusable(true); // 获取焦点
        mPopupWindow.update();

        Button mbuttonTakePhoto = (Button) mpopview
                .findViewById(R.id.button_take_photo);
        Button mbuttonChoicePhoto = (Button) mpopview
                .findViewById(R.id.button_choice_photo);
        Button mbuttonChoicecannce = (Button) mpopview
                .findViewById(R.id.button_choice_cancer);

        // 相册上传
        mbuttonChoicePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();

                autoObtainStoragePermission();
            }
        });

        // 拍照上传
        mbuttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                autoObtainCameraPermission();
            }
        });

        mbuttonChoicecannce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 如果点击了popupwindow的外部，popupwindow也会消失
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtils.showShort(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(mContext, "com.likeits.sanyou.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtils.showShort(this, "设备没有SD卡！");
            }
        }
    }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            switch (requestCode) {
                case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (hasSdcard()) {
                            imageUri = Uri.fromFile(fileUri);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                imageUri = FileProvider.getUriForFile(mContext, "com.likeits.sanyou.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                            PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                        } else {
                            ToastUtils.showShort(this, "设备没有SD卡！");
                        }
                    } else {

                        ToastUtils.showShort(this, "请允许打开相机！！");
                    }
                    break;


                }
                case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                    } else {

                        ToastUtils.showShort(this, "请允许打操作SDCard！！");
                    }
                    break;
            }
        }

    private static final int output_X = 480;
    private static final int output_Y = 480;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    Log.d("TAG321", imageUri.getPath());
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.likeits.sanyou.fileprovider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                        Log.d("TAG123", newUri.getPath());
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    Log.d("TAG555", cropImageUri.toString());
                    if (bitmap != null) {
                        showImages(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bytes = baos.toByteArray();
                        String base64Token = Base64.encodeToString(bytes, Base64.DEFAULT);//  编码后
                        Log.d("TAG666", base64Token);
                        upLoad(base64Token);
                    }
                    break;
            }
        }
    }


    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    private void showImages(Bitmap bitmap) {
        ivAvatar.setImageBitmap(bitmap);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(SettingActivity.this, R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        display();
                    }
                }, 2017, 01, 01);
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        tvBirthday.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));
    }

    private void upLoad(String base64Token) {
        HttpMethods.getInstance().UploadImg(new MySubscriber<EmptyEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<EmptyEntity> emptyEntityHttpResult) {
                if (emptyEntityHttpResult.getCode() == 1) {
                    showToast("上传成功");

                } else {
                    showToast(emptyEntityHttpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey, base64Token);
    }
}
