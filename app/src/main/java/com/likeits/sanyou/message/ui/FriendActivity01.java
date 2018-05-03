package com.likeits.sanyou.message.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.message.adapter.FriendAdapter;
import com.likeits.sanyou.message.model.FriendBean;
import com.likeits.sanyou.message.utils.UserInfoCacheSvc;
import com.likeits.sanyou.message.utils.pingyin.CharacterParser;
import com.likeits.sanyou.message.utils.pingyin.PinyinComparator;
import com.likeits.sanyou.message.utils.pingyin.SideBar;
import com.likeits.sanyou.ui.base.Container;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendActivity01 extends Container {
    @BindView(R.id.iv_header_left)
    ImageView ivLeft;
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.listview)
    ListView mListView;
    /**
     * 右侧好友指示 Bar
     */
    @BindView(R.id.sidrbar)
    SideBar mSidBar;
    /**
     * 中部展示的字母提示
     */
    @BindView(R.id.dialog)
    TextView  dialog;
    private List<FriendBean> dataLsit;
    private List<FriendBean> sourceDataList;

    /**
     * 好友列表的 adapter
     */
    private FriendAdapter adapter;



    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private LayoutInflater infalter;
    private FriendBean mFriendBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend01);
        ButterKnife.bind(this);
        dataLsit = new ArrayList<>();
        sourceDataList = new ArrayList<>();
        characterParser = CharacterParser.getInstance();
        pinyinComparator = PinyinComparator.getInstance();
        initData();
        showProgress("Loading...");
       // initView();
        ivLeft.setImageResource(R.drawable.header_back);
        tvHeader.setText("好友列表");
        if(dataLsit==null){
            mSidBar.setVisibility(View.GONE);
        }else{
            mSidBar.setVisibility(View.VISIBLE);
        }

    }
/**
 *   好友列表数据
 */

    private void initData() {
        /*String url = AppConfig.LIKEIT_GET_EASE_FRIEND;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                disShowProgress();
                Log.d("TAG", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("code");
                    String message = object.optString("message");
                    if ("1".equals(code)) {
                        JSONArray array = object.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.optJSONObject(i);
                      *//*      mSortModel = new SortModel();
                            mSortModel.setEasemob_id(obj.optString("easemob_id"));
                            mSortModel.setNickname(obj.optString("nickname"));
                            mSortModel.setHeadimg(obj.optString("headimg"));
                            mSortModel.setUid(obj.optString("uid"));
                            mDateList.add(mSortModel);*//*
                            mFriendBean = new FriendBean();
                            mFriendBean.setName(obj.optString("nickname"));
                            mFriendBean.setHeadimg(obj.optString("headimg"));
                            mFriendBean.setUid(obj.optString("uid"));
                            mFriendBean.setEasemob_id(obj.optString("easemob_id"));
                            dataLsit.add(mFriendBean);
                        }
                        if (dataLsit != null && dataLsit.size() > 0) {
                            sourceDataList = filledData(dataLsit); //过滤数据为有字母的字段  现在有字母 别的数据没有
                        }
                        for (int i = 0; i < dataLsit.size(); i++) {
                            sourceDataList.get(i).setName(dataLsit.get(i).getName());
                            sourceDataList.get(i).setHeadimg(dataLsit.get(i).getHeadimg());
                            sourceDataList.get(i).setUid(dataLsit.get(i).getUid());
                            sourceDataList.get(i).setEasemob_id(dataLsit.get(i).getEasemob_id());
                        }
                        dataLsit = null; //释放资源
                        Log.d("TAG33w",sourceDataList.toString());
                        initView();
                    }else{
                        ToastUtil.showS(mContext,"暂无数据");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
                //mListView.onRefreshComplete();
            }
        });*/
    }

    private void initView() {
        mSidBar.setTextView(dialog);
        //设置右侧触摸监听
        mSidBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }

            }
        });

        // 根据a-z进行排序源数据
       // Collections.sort(sourceDataList, pinyinComparator);

        Log.d("TAGss",sourceDataList.toString());
       // adapter = new FriendAdapter(mContext, sourceDataList);
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
       mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (sourceDataList != null) {
                    String nickname = sourceDataList.get(position).getName();
                    String headimg = sourceDataList.get(position ).getHeadimg();
                    String easemob_id = sourceDataList.get(position ).getEasemob_id();
                    String uid = sourceDataList.get(position ).getUid();
                    // demo中直接进入聊天页面，实际一般是进入用户详情页
                    // demo中直接进入聊天页面，实际一般是进入用户详情页
                    UserInfoCacheSvc.createOrUpdate(easemob_id, nickname, headimg);
                    startActivity(new Intent(mContext, ChatActivity.class).putExtra("userId", easemob_id));
                }
            }

        });


    }


//    private void refresh1() {
//        adapter.addAll(userData, true);
//        initData();
//        showProgress("Loading...");
//        adapter.notifyDataSetChanged();
//    }



    public TextView getDialog() {
        return dialog;
    }

    public void setDialog(TextView dialog) {
        this.dialog = dialog;
    }
    /**
     * 为ListView填充数据
     *
     * @param
     * @return
     */
    private List<FriendBean> filledData(List<FriendBean> lsit) {
        List<FriendBean> mFriendList = new ArrayList<FriendBean>();
        Log.d("TAGssdsd",lsit.toString());
        for (int i = 0; i < lsit.size(); i++) {
            FriendBean friendModel = new FriendBean();
            friendModel.setName(lsit.get(i).getName());
            Log.d("TAGName",lsit.get(i).getName());
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(lsit.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                friendModel.setLetters(sortString.toUpperCase());
            } else {
                friendModel.setLetters("#");
            }

            mFriendList.add(friendModel);
        }
        return mFriendList;

    }

    @OnClick({R.id.iv_header_left, R.id.iv_header_right})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_left:
                onBackPressed();
                break;
            case R.id.iv_header_right:
                //onBackPressed();
                //  startActivity(new Intent(mContext, AddContactActivity.class));
                break;
        }
    }
}
