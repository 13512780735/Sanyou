package com.likeits.sanyou.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.CollectInfoEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */

public class CollectAdapter extends MyBaseAdapter<CollectInfoEntity> {
    private boolean isShow = true;//是否显示编辑/完成
    private ModifyCountInterface modifyCountInterface;

    public CollectAdapter(Context context, List<CollectInfoEntity> collectInfoEntityList) {
        super(context, collectInfoEntityList);
    }

    /**
     * 改变商品数量接口
     *
     * @param modifyCountInterface
     */
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    /**
     * 是否显示可编辑
     *
     * @param flag
     */
    public void isShow(boolean flag) {
        isShow = flag;
        notifyDataSetChanged();
    }

    @Override
    public View getItemView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_myfoorprint_listview_items, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CollectInfoEntity collectInfoEntity = getItem(position);
        ImageLoader.getInstance().displayImage(collectInfoEntity.getPath(), holder.iv_avatar);
        holder.tv_title.setText(collectInfoEntity.getGoods_name());
        holder.tv_price.setText(collectInfoEntity.getMoney_need());
        holder.tv_commodity_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = new AlertDialog.Builder(context).create();
                alert.setTitle("操作提示");
                alert.setMessage("您确定要将这些商品从收藏夹中移除吗？");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                modifyCountInterface.childDelete(position);//删除 目前只是从item中移除

                            }
                        });
                alert.show();
            }
        });
        //判断是否在编辑状态下
        if (isShow) {
            holder.tv_commodity_delete.setVisibility(View.GONE);
        } else {
            holder.tv_commodity_delete.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    //初始化控件
    class ViewHolder {
        ImageView iv_avatar;
        TextView tv_title, tv_price, tv_commodity_delete;

        public ViewHolder(View itemView) {
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_commodity_delete = (TextView) itemView.findViewById(R.id.tv_commodity_delete);
        }
    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {


        /**
         * 删除子item
         *
         * @param position
         */
        void childDelete(int position);
    }
}
