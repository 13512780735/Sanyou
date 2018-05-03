package com.likeits.sanyou.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.likeits.sanyou.R;
import com.likeits.sanyou.ui.base.BaseViewHolder;
import com.likeits.sanyou.ui.base.KKBaseAdapter;
import com.likeits.sanyou.ui.information.ImformationListActivity;

/**
 * Created by Administrator on 2017/12/4.
 */

public class ImformationListAdapter extends KKBaseAdapter<ImformationInfoEntity> {
    @Override
    public BaseViewHolder<ImformationInfoEntity> onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_imfotmation_listview_items, parent, false);
        ImformationViewHolder imformationViewHolder = new ImformationViewHolder(itemView);
        imformationViewHolder.setBaseActivity(baseActivity);
        return imformationViewHolder;
    }

    ImformationListActivity baseActivity;

    public void setBaseActivity(ImformationListActivity baseActivity) {
        this.baseActivity = baseActivity;
    }
}
