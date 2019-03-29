package com.sergey.zhuravlev.auction.client.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

public class WidthSizeBaseCardView extends CardView {

    public WidthSizeBaseCardView(Context context) {
        super(context);
    }

    public WidthSizeBaseCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WidthSizeBaseCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, w, oldw, oldh);
    }
}
