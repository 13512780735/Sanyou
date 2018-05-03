package com.likeits.sanyou.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.fragment.MainFragment01;
import com.likeits.sanyou.message.utils.UserInfoCacheSvc;
import com.likeits.sanyou.network.api_service.MyApiService;
import com.likeits.sanyou.ui.chat.AddFriendActivity;
import com.likeits.sanyou.ui.chat.adapter.ChatViewPageAdatpter;
import com.likeits.sanyou.utils.HttpUtil;
import com.likeits.sanyou.utils.MyActivityManager;
import com.likeits.sanyou.utils.ToastUtil;
import com.likeits.sanyou.utils.UtilPreference;
import com.likeits.sanyou.view.NoScrollViewPager;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyfriendActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.iv_header_right)
    ImageView ivRight;
    @BindView(R.id.iv_header_left)
    ImageView ivLeft;
    @BindView(R.id.rgTools)
    RadioGroup mRgTools;
    @BindView(R.id.line_message)
    View lineMessage;
    @BindView(R.id.line_contact)
    View lineContact;
    @BindView(R.id.chat_viewpager)
    NoScrollViewPager mViewPager;
    private ChatViewPageAdatpter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfriend);
        MyActivityManager.getInstance().addActivity(this);
        ButterKnife.bind(this);
     initData();

    }
    @Override
    public void onResume() {
        super.onResume();
      //  initData();//好友列表获取别存储昵称头像
    }
    private void initData() {
        String url = MyApiService.BASE_URL + "?s=/api/index/get_ease_friend";
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(MyfriendActivity.this, "ukey"));
        HttpUtil.get(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("code");
                    String message = object.optString("message");
                    if ("1".equals(code)) {
                        JSONArray array = object.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.optJSONObject(i);
                            UserInfoCacheSvc.createOrUpdate(obj.optString("easemob_id"), obj.getString("nickname"), obj.getString("headimg"));
                        }
                    } else {
                        ToastUtil.showS(MyfriendActivity.this, "暂无数据");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                initView();
            }

            @Override
            public void failed(Throwable e) {
                initView();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                initView();
            }
        });
    }

    private void initView() {
        tvHeader.setText("本地钓友");
        ivLeft.setImageResource(R.mipmap.icon_red_back);
        ivRight.setImageResource(R.mipmap.icon_chat_add);
        adapter = new ChatViewPageAdatpter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(1);
        mViewPager.setOffscreenPageLimit(0);
        mRgTools.setOnCheckedChangeListener(this);
        MainFragment01 fragment = new MainFragment01();

    }


    @OnClick({R.id.iv_header_right, R.id.iv_header_left})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_left:
                onBackPressed();
                break;
            case R.id.iv_header_right:
                Intent intent = new Intent(MyfriendActivity.this, AddFriendActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mRgTools.check(mRgTools.getChildAt(position).getId());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        //  mViewPager.setCurrentItem(radioGroup.indexOfChild(radioGroup.findViewById(checkedId)), false);
        switch (checkedId) {
            case R.id.radio_message:
                mViewPager.setCurrentItem(0);
                lineMessage.setVisibility(View.VISIBLE);
                lineContact.setVisibility(View.INVISIBLE);
                break;
            case R.id.radio_contact:
                mViewPager.setCurrentItem(1);
                lineMessage.setVisibility(View.INVISIBLE);
                lineContact.setVisibility(View.VISIBLE);
                break;
        }
    }


}
