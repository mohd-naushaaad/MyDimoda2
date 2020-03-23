package com.mydimoda.customView;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Parth on 2/13/2018.
 */

public class Existence_Light_TextView extends androidx.appcompat.widget.AppCompatTextView {
    public Existence_Light_TextView(Context context) {
        super(context);
        init();
    }

    public Existence_Light_TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public Existence_Light_TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public void init() {
        Typeface tfTap = Typeface.createFromAsset(getContext().getAssets(),
                "Existence-Light.otf");
        setTypeface(tfTap);
    }
}

