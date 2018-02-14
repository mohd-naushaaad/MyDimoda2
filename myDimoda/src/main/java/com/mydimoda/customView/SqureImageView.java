package com.mydimoda.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Parth on 2/14/2018.
 */


public class SqureImageView extends android.support.v7.widget.AppCompatImageView {

    public SqureImageView(Context context) {
        super(context);
    }

    public SqureImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SqureImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }

}
