package com.likeits.sanyou.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.adapter.BankListAdapter;
import com.likeits.sanyou.entity.BankInfo;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseActivity;
import com.likeits.sanyou.view.MyListview;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BankListActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.myListView)
    MyListview myListView;
    private ArrayList<BankInfo> bankData;
    private BankListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
        ButterKnife.bind(this);
        bankData = new ArrayList<>();
        initView();
        initData();
    }

    private void initData() {
        HttpMethods.getInstance().GetBank(new MySubscriber<ArrayList<BankInfo>>(this) {
            @Override
            public void onHttpCompleted(HttpResult<ArrayList<BankInfo>> httpResult) {
                if (httpResult.getCode() == 1) {
                    bankData = httpResult.getData();
                    mAdapter = new BankListAdapter(mContext, bankData);
                    myListView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    httpResult.getMsg();
                }
            }

            @Override
            public void onHttpError(Throwable e) {

            }
        }, ukey);
    }

    private void initView() {
        tvHeader.setText("选择银行");
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bankid = bankData.get(position).getId();
                String bankName = bankData.get(position).getBank();
                Intent intent = getIntent();
                intent.putExtra("bankid", bankid);
                intent.putExtra("bankName", bankName);
                setResult(101, intent);
                finish();
            }
        });
    }

    @OnClick({R.id.backBtn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }
}
