package com.likeits.sanyou.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserAgreementActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
        ButterKnife.bind(this);
        tvHeader.setText("用户协议");
    }
    @OnClick(R.id.backBtn)
    public void Onclick(View v){
        switch (v.getId()){
            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }
}
