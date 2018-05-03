package com.likeits.sanyou.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.EmptyEntity;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.MainActivity;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.utils.MyActivityManager;
import com.likeits.sanyou.utils.StringUtil;
import com.likeits.sanyou.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WithdrawActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_sum) //金额
            TextView tv_sum;
    @BindView(R.id.tv_band) //银行卡
            TextView tv_band;
    @BindView(R.id.ed_name) //银行卡
            EditText ed_name;
    @BindView(R.id.tv_bank) //银行卡类型
            TextView tv_bank;
    @BindView(R.id.tv_bankcard) //银行卡号
            EditText tv_bankcard;
    private String name;
    private String bankcard;
    private String amount;
    private String  bankid;
    private String bankName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ButterKnife.bind(this);
        amount=getIntent().getExtras().getString("amount");
        initView();
    }

    private void initView() {
        tvHeader.setText("申请提现");
        tv_sum.setText("¥ "+amount);
    }

    @OnClick({R.id.backBtn, R.id.tv_withdraw,R.id.tv_bank})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_bank:
                Intent intent=new Intent(mContext,BankListActivity.class);
                startActivityForResult(intent,101);
                break;
            case R.id.tv_withdraw:
                name = ed_name.getText().toString().trim();
                bankcard = tv_bankcard.getText().toString().trim();
                if (StringUtil.isBlank(name)) {
                    ToastUtil.showS(mContext, "姓名不能为空");
                    return;
                }
                if ((StringUtil.isBlank(bankcard))) {
                    ToastUtil.showS(mContext, "银行卡号不能为空");
                    return;
                }
                if ((StringUtil.isBlank(bankid))) {
                    ToastUtil.showS(mContext, "请选择银行");
                    return;
                }
                offer();
                break;
        }
    }

    private void offer() {
        HttpMethods.getInstance().WithDrawals(new MySubscriber<EmptyEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<EmptyEntity> httpResult) {
                    if(httpResult.getCode()==1){
                        showToast("提现成功");
                        Intent intent = new Intent(mContext, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("keys", "1");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        MyActivityManager.getInstance().finishAllActivity();
                    }else{
                        showToast(httpResult.getMsg());
                    }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey,amount,name,bankid,bankcard);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 101:
                bankid=data.getStringExtra("bankid");
                bankName=data.getStringExtra("bankName");
                tv_bank.setText(bankName);
                break;
        }
    }
}
