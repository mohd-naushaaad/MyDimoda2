package com.mydimoda.widget.cropper.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FontsUtil {

	public static void setMedium(Context c, TextView tv) {
		Typeface tfTap = Typeface.createFromAsset(c.getAssets(),
				"Brixton Medium.ttf");
		tv.setTypeface(tfTap);
	}

//	public static void setBold(Context c, Button button) {
//		Typeface tfTap = Typeface.createFromAsset(c.getAssets(),
//				"Brixton Bold.ttf");
//		button.setTypeface(tfTap);
//	}

//	public static void setBold(Context c, EditText et) {
//		Typeface tfTap = Typeface.createFromAsset(c.getAssets(),
//				"Brixton Bold.ttf");
//		et.setTypeface(tfTap);
//	}

    public static void setLight(Context c, TextView tv) {
        Typeface tfTap = Typeface.createFromAsset(c.getAssets(),
                "Brixton Light.ttf");
        tv.setTypeface(tfTap);
    }

    public static void setLight(Context c, EditText editText) {
        Typeface tfTap = Typeface.createFromAsset(c.getAssets(),
                "Brixton Light.ttf");
        editText.setTypeface(tfTap);
    }

    public static void setLight(Context c, Button button) {
        Typeface tfTap = Typeface.createFromAsset(c.getAssets(),
                "Brixton Light.ttf");
        button.setTypeface(tfTap);
    }

    public static void setBoldOblique(Context c, TextView tv) {
        Typeface tfTap = Typeface.createFromAsset(c.getAssets(),
                "Brixton Bold Oblique.ttf");
        tv.setTypeface(tfTap);
    }

    public static void setExistenceLight(Context c, TextView tv) {
        Typeface tfTap = Typeface.createFromAsset(c.getAssets(),
                "Existence-Light.otf");
        tv.setTypeface(tfTap);
    }

    public static void setExistenceLight(Context c, Button button) {
        Typeface tfTap = Typeface.createFromAsset(c.getAssets(),
                "Existence-Light.otf");
        button.setTypeface(tfTap);
    }

    public static void setExistenceLight(Context c, EditText editText) {
        Typeface tfTap = Typeface.createFromAsset(c.getAssets(),
                "Existence-Light.otf");
        editText.setTypeface(tfTap);
    }

    public static void setBookOblique(Context c, EditText editText) {
        Typeface tfTap = Typeface.createFromAsset(c.getAssets(),
                "Brixton Book Oblique.ttf");
        editText.setTypeface(tfTap);
    }

    public static void setExistenceLightAnyView(final Context context, final View root) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                    setExistenceLightAnyView(context, viewGroup.getChildAt(i));
            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(),
                        "Existence-Light.otf"));
        } catch (Exception e) {
            Log.e("FontsUtil", String.format("Error occured when trying to apply %s font for %s view", "existancelight", root));
            e.printStackTrace();
        }
    }
}
