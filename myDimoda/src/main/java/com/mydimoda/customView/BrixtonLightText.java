package com.mydimoda.customView;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Parth on 2/8/2018.
 */

public class BrixtonLightText extends androidx.appcompat.widget.AppCompatTextView {
    public BrixtonLightText(Context context) {
        super(context);
        init();
    }

    public BrixtonLightText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public BrixtonLightText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public void init() {
        Typeface tfTap = Typeface.createFromAsset(getContext().getAssets(),
                "Brixton Light.ttf");
        setTypeface(tfTap);
    }
}
