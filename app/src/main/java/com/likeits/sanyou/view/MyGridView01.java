package com.likeits.sanyou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2017/7/25.
 */

public class MyGridView01 extends GridView {
    public MyGridView01(Context context) {
        super(context);
    }

    public MyGridView01(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView01(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 1, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
