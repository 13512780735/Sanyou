package com.likeits.sanyou.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.network.HttpMethods;
import com.likeits.sanyou.network.entity.EmptyEntity;
import com.likeits.sanyou.network.entity.HttpResult;
import com.likeits.sanyou.subscriber.MySubscriber;
import com.likeits.sanyou.ui.base.BaseViewHolder;
import com.likeits.sanyou.ui.information.ImformationListActivity;
import com.likeits.sanyou.ui.information.InformationDetailsActivity;
import com.likeits.sanyou.utils.UtilPreference;
import com.likeits.sanyou.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/4.
 */

public class ImformationViewHolder extends BaseViewHolder<ImformationInfoEntity> {

    @BindView(R.id.tv_imformation_title)
    TextView tvImformation_title;
    @BindView(R.id.iv_image)
    ImageView iv_image;
    @BindView(R.id.user_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_like)
    TextView tv_like;
    @BindView(R.id.tv_message)
    TextView tv_message;
    ImformationListActivity baseActivity;
    private String status;
    private int likes;
    private Drawable img1, img2;
    private String msg;

    public void setBaseActivity(ImformationListActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public ImformationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(final ImformationInfoEntity imformationInfoEntity, int position) {
        Log.d("TAG", imformationInfoEntity.toString());
        Log.d("TAG1", imformationInfoEntity.getTitle());
        tvImformation_title.setText(imformationInfoEntity.getTitle());
        ImageLoader.getInstance().displayImage(imformationInfoEntity.getCover(), iv_image);
        ImageLoader.getInstance().displayImage(imformationInfoEntity.getHeadimg(), userAvatar);
        tv_title.setText(imformationInfoEntity.getAuthor());
        tv_time.setText(imformationInfoEntity.getInterval());
        status = imformationInfoEntity.getStatus();
        likes = imformationInfoEntity.getLikes();
        if ("0".equals(status)) {//取消点赞
            img1 = context.getResources().getDrawable(R.mipmap.icon_unlike);
            img1.setBounds(0, 0, img1.getMinimumWidth(), img1.getMinimumHeight());
            tv_like.setCompoundDrawables(img1, null, null, null); //设置左图标
            tv_like.setText(likes + "");
        } else if ("1".equals(status)) { //点赞
            img2 = context.getResources().getDrawable(R.mipmap.icon_like);
            img2.setBounds(0, 0, img2.getMinimumWidth(), img2.getMinimumHeight());
            tv_like.setCompoundDrawables(img2, null, null, null); //设置左图标
            tv_like.setText(likes + "");
        }
        tv_message.setText(imformationInfoEntity.getComment());
        tv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(status)) {
                    img2 = context.getResources().getDrawable(R.mipmap.icon_like);
                    img2.setBounds(0, 0, img2.getMinimumWidth(), img2.getMinimumHeight());
                    tv_like.setCompoundDrawables(img2, null, null, null);
                    likes = likes + 1;
                    tv_like.setText(likes + "");
                    msg="点赞成功";
                    status = "1";
                } else if ("1".equals(status)) {
                    img1 = context.getResources().getDrawable(R.mipmap.icon_unlike);
                    img1.setBounds(0, 0, img1.getMinimumWidth(), img1.getMinimumHeight());
                    tv_like.setCompoundDrawables(img1, null, null, null);
                    likes = likes - 1;
                    tv_like.setText(likes + "");
                    status = "0";
                    msg="取消成功";
                }
                HttpMethods.getInstance().GetNewsLikes(new MySubscriber<EmptyEntity>(baseActivity) {
                    @Override
                    public void onHttpCompleted(HttpResult<EmptyEntity> emptyEntityHttpResult) {
                        if (emptyEntityHttpResult.getCode() == 1) {
                            showToast(msg);
                        } else {
                            showToast(emptyEntityHttpResult.getMsg());
                        }
                    }

                    @Override
                    public void onHttpError(Throwable e) {

                    }
                }, UtilPreference.getStringValue(context, "ukey"), imformationInfoEntity.getId(), status);
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, InformationDetailsActivity.class);
                intent.putExtra("id",imformationInfoEntity.getId());
                context.startActivity(intent);
            }
        });
    }


}
