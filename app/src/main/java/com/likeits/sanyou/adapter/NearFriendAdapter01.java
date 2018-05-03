package com.likeits.sanyou.adapter;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMClient;
import com.likeits.sanyou.R;
import com.likeits.sanyou.message.widget.DemoHelper;
import com.likeits.sanyou.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2018/1/19.
 */

public class NearFriendAdapter01 extends BaseQuickAdapter<NearFirendInfoEntity> {
    private ProgressDialog progressDialog;

    public NearFriendAdapter01(int layoutResId, List<NearFirendInfoEntity> data) {
        super(R.layout.layout_near_friend_listview_items, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, NearFirendInfoEntity nearFirendInfoEntity) {
        final String easemob_id = nearFirendInfoEntity.getEasemob_id();
        ImageLoader.getInstance().displayImage(nearFirendInfoEntity.getHeadimg(), (CircleImageView) baseViewHolder.getView(R.id.me_header_avatar));
        baseViewHolder.setText(R.id.me_header_name, nearFirendInfoEntity.getNickname());
        baseViewHolder.setText(R.id.me_header_distance, nearFirendInfoEntity.getKm() * 1000 + "米");
        if (DemoHelper.getInstance().getContactList().containsKey(easemob_id)) {
            //let the user know the contact already in your contact list
            if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(easemob_id)) {
                baseViewHolder.getView(R.id.tv_add01).setVisibility(View.VISIBLE);
                return;
            }
            baseViewHolder.getView(R.id.tv_add01).setVisibility(View.VISIBLE);
            return;
        }else{
        baseViewHolder.getView(R.id.tv_add).setVisibility(View.VISIBLE);
        baseViewHolder.getView(R.id.tv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(mContext);
                String stri = mContext.getResources().getString(R.string.Is_sending_a_request);
                progressDialog.setMessage(stri);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
//                new Thread(new Runnable() {
//                    public void run() {

                        try {
                            //demo use a hardcode reason here, you need let user to input if you like
                            String s = mContext.getResources().getString(R.string.Add_a_friend);
                            EMClient.getInstance().contactManager().addContact(easemob_id, s);
                            // this.runOnUiThread(new Runnable() {
                            //  public void run() {
                            progressDialog.dismiss();
                            String s1 = mContext.getResources().getString(R.string.send_successful);
                            Toast.makeText(mContext, s1, Toast.LENGTH_LONG).show();
                            //}
                            //   });
                        } catch (final Exception e) {
                           // runOnUiThread(new Runnable() {
                             //   public void run() {
                                    progressDialog.dismiss();
                                    String s2 = mContext.getResources().getString(R.string.Request_add_buddy_failure);
                                    Toast.makeText(mContext, s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                               // }
                           // });
                        }
                    }
            //    }).start();
         //   }
        });}
    }
}
