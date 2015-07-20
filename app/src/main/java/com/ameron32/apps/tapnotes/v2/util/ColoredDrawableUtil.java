package com.ameron32.apps.tapnotes.v2.util;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.TypedValue;

/**
 * Created by klemeilleur on 7/20/2015.
 */
public class ColoredDrawableUtil {

  public static void setDrawableColor(Activity activity, Drawable drawable, @AttrRes int attr, @ColorRes int color) {
    final Drawable d = DrawableCompat.wrap(drawable);
    final int c = getColorFromAttribute(activity, attr, color);
    DrawableCompat.setTint(d, c);
  }

  private static int getColorFromAttribute(Activity activity, @AttrRes int attr, @ColorRes int defaultColor) {
    final TypedValue typedValue = new TypedValue();
    activity.getTheme()
        .resolveAttribute(attr, typedValue, true);
    final int[] accentColor = new int[] { attr };
    final int indexOfAttrColor = 0;
    final TypedArray a = activity
        .obtainStyledAttributes(typedValue.data, accentColor);
    final int color = a.getColor(indexOfAttrColor, defaultColor);
    a.recycle();
    return color;
  }
}
