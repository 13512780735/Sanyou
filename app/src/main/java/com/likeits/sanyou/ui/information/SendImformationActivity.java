package com.likeits.sanyou.ui.information;

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

public class SendImformationActivity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_imformation);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvHeader.setText("发布资讯");
        tvRight.setText("发布");
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
