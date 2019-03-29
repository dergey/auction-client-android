package com.sergey.zhuravlev.auction.client.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

public class HeightSizeBaseCardView extends CardView {

    public HeightSizeBaseCardView(Context context) {
        super(context);
    }

    public HeightSizeBaseCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeightSizeBaseCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, h, oldw, oldh);
    }
}
