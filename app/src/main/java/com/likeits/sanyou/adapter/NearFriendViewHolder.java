package com.likeits.sanyou.adapter;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.likeits.sanyou.R;
import com.likeits.sanyou.message.widget.DemoHelper;
import com.likeits.sanyou.ui.base.BaseViewHolder;
import com.likeits.sanyou.ui.chat.NearFriendActivity;
import com.likeits.sanyou.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/4.
 */

public class NearFriendViewHolder extends BaseViewHolder<NearFirendInfoEntity> {
    @BindView(R.id.me_header_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.me_header_name)
    TextView tvName;
    @BindView(R.id.me_header_distance)
    TextView tvDistance;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_add01)
    TextView tvAdd01;
    private NearFriendActivity baseActivity;
    private ProgressDialog progressDialog;


    public NearFriendViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setBaseActivity(NearFriendActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    @Override
    public void onBind(final NearFirendInfoEntity nearFirendInfoEntity, int position) {
        final String easemob_id = nearFirendInfoEntity.getEasemob_id();
        ImageLoader.getInstance().displayImage(nearFirendInfoEntity.getHeadimg(), ivAvatar);
        tvName.setText(nearFirendInfoEntity.getNickname());
        tvDistance.setText(nearFirendInfoEntity.getKm() * 1000 + "m");

        if(DemoHelper.getInstance().getContactList().containsKey(easemob_id)){
            //let the user know the contact already in your contact list
            if(EMClient.getInstance().contactManager().getBlackListUsernames().contains(easemob_id)){
                tvAdd01.setVisibility(View.VISIBLE);
                return;
            }
            tvAdd01.setVisibility(View.VISIBLE);
            return;
        }
        tvAdd.setVisibility(View.VISIBLE);
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(context);
                String stri = context.getResources().getString(R.string.Is_sending_a_request);
                progressDialog.setMessage(stri);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                new Thread(new Runnable() {
                    public void run() {

                        try {
                            //demo use a hardcode reason here, you need let user to input if you like
                            String s = context.getResources().getString(R.string.Add_a_friend);
                            EMClient.getInstance().contactManager().addContact(easemob_id, s);
                            baseActivity.runOnUiThread(new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();
                                    String s1 = context.getResources().getString(R.string.send_successful);
                                    Toast.makeText(context, s1, Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (final Exception e) {
                            baseActivity.runOnUiThread(new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();
                                    String s2 = context.getResources().getString(R.string.Request_add_buddy_failure);
                                    Toast.makeText(context, s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }).start();
            }

        });
    }
}
