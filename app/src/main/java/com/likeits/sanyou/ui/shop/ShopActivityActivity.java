package com.likeits.sanyou.ui.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.ui.base.Container;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopActivityActivity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvHeader.setText("商城活动");
    }
    @OnClick({R.id.backBtn})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }
}
