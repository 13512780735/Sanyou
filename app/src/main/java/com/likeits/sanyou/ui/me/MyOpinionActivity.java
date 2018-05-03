package com.likeits.sanyou.ui.me;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.EmptyEntity;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.utils.StringUtil;
import com.likeits.sanyou.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOpinionActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.editText)
    EditText edContent;
    @BindView(R.id.ed_edPhone)
    EditText edPhone;
    @BindView(R.id.ed_edEmail)
    EditText edEmail;
    private String tvContent,tvPhone,tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_opinion);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvHeader.setText("意见反馈");
        tvRight.setText("提交");
    }

    @OnClick({R.id.backBtn,R.id.tv_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_right:
                commit();
                break;
        }
    }

    private void commit() {
        tvContent=edContent.getText().toString();
        tvPhone=edPhone.getText().toString().trim();
        tvEmail=edEmail.getText().toString().trim();
        if (StringUtil.isBlank(tvContent) ||StringUtil.isBlank(tvPhone)||StringUtil.isBlank(tvEmail)) {
            ToastUtil.showS(mContext, "资料未填全");
            return;
        }
        HttpMethods.getInstance().FeedBack(new MySubscriber<EmptyEntity>(this) {
            @Override
            public void onHttpCompleted(HttpResult<EmptyEntity> emptyEntityHttpResult) {
                if(emptyEntityHttpResult.getCode()==1){
                    showToast("提交反馈成功");
                }else{
                    showToast(emptyEntityHttpResult.getMsg());
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        },ukey,tvPhone,tvEmail,tvContent);
    }
}
