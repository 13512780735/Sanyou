package com.pk4pk.baseappmoudle.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mayi.pk4pk.baseappmoudle.R;
import com.pk4pk.baseappmoudle.dialog.LoaddingDialog;
import com.pk4pk.baseappmoudle.runtimepermissions.PermissionsManager;
import com.pk4pk.baseappmoudle.runtimepermissions.PermissionsResultAction;
import com.pk4pk.baseappmoudle.utils.KKToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by mayi on 16/12/26.
 *
 * @Autor CaiWF
 * @Email 401885064@qq.com
 * @TODO
 */

public abstract class KKBaseActivity extends AppCompatActivity {
    protected final String TAG = KKBaseActivity.class.getName();
    protected Context context;
    public LoaddingDialog loaddingDialog;
    public ImageView top_bar_back_img;
    public TextView top_bar_title,top_bar_right_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = KKBaseActivity.this;
        loaddingDialog = new LoaddingDialog(context);
        requestPermissions();
        if (haveEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (haveEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        loaddingDismiss();
    }

    @Subscribe
    public void emptyEvent(){

    }

    protected void initTopRightBar(String title,String right_title){
        top_bar_back_img= (ImageView) findViewById(R.id.top_bar_back_img);
        top_bar_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toFinish();
            }
        });
        top_bar_title= (TextView) findViewById(R.id.top_bar_title);
        top_bar_right_title= (TextView) findViewById(R.id.top_bar_right_tv);
        top_bar_right_title.setText(right_title);
        top_bar_title.setText(title);
    }

    protected void initTopBar(String title){
        top_bar_back_img= (ImageView) findViewById(R.id.top_bar_back_img);
        top_bar_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toFinish();
            }
        });
        top_bar_title= (TextView) findViewById(R.id.top_bar_title);
        top_bar_title.setText(title);
    }

    public void loaddingShow(){
        if(loaddingDialog ==null ){
            loaddingDialog=new LoaddingDialog(context);
        }

        if(!loaddingDialog.isShowing()){
            loaddingDialog.show();
        }
    }

    public void loaddingDismiss() {
        if (loaddingDialog != null && loaddingDialog.isShowing()) {
            loaddingDialog.dismiss();
        }
    }


    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
//                Toast.makeText(context, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }


    protected void toFinish() {
        finish();
    }

    public void toActivityFinish(Class activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
        toFinish();
    }

    public void toActivity(Class activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    public void toActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(context, activity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void showToast(String msg) {
        KKToast.show(context, msg);
    }

    public void showToast(@StringRes int msg) {
        KKToast.show(context, msg);
    }

    public abstract boolean haveEventBus();

}
