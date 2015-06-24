package com.ameron32.apps.tapnotes.v2.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * Created by klemeilleur on 6/18/2015.
 */
public class RoundedDrawableUtil {

  /*
   * Thank to Chris Banes
   */
  public static Drawable getDrawableAsRounded(Context context, @DrawableRes int drawable) {
    final Bitmap bitmap = //
        BitmapFactory.decodeResource(context.getResources(), drawable);

    final RoundedBitmapDrawable circularBitmapDrawable = //
        RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);

    circularBitmapDrawable.setCornerRadius(bitmap.getWidth());

    return circularBitmapDrawable;
  }
}
