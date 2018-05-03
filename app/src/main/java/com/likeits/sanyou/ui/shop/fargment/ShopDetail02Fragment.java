package com.likeits.sanyou.ui.shop.fargment;


import android.support.v4.app.Fragment;
import android.util.Log;

import com.likeits.sanyou.R;
import com.likeits.sanyou.entity.CategoryDetails;
import com.likeits.sanyou.ui.base.BaseFragment;
import com.likeits.sanyou.utils.richtext.RichText;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopDetail02Fragment extends BaseFragment {


  private RichText tvDetails;
    private CategoryDetails categoryDetails;

    @Override
    protected int setContentView() {
        return R.layout.fragment_shop_detail02;
    }

    @Override
    protected void lazyLoad() {
        categoryDetails= (CategoryDetails) getArguments().getSerializable("categoryDetails");
        Log.d("TAG22",categoryDetails.getGoods_detail());
        initView();
    }

    private void initView() {
        tvDetails=findViewById(R.id.tv_details);
     tvDetails.setRichText(categoryDetails.getGoods_detail());
    }


}
