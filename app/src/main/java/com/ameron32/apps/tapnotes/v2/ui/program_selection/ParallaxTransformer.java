package com.ameron32.apps.tapnotes.v2.ui.program_selection;

import android.graphics.Matrix;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 *
 * @author brendanw
 *  * Reminders about position:
 * (1) When a page fills the screen its position is 0
 * (2) When a page is drawn off the right side of the screen, its position is 1
 * (3) When a page is drawn off the left side of the screen, its position is -1
 */
public class ParallaxTransformer implements ViewPager.PageTransformer {

    private static final String TAG = ParallaxTransformer.class.getSimpleName();

    @Override
    public void transformPage(View view, float position) {

        RelativeLayout root = (RelativeLayout) ((FrameLayout) view)
                .getChildAt(0);
//        Button previewBtn = (Button) root.findViewById(R.id.preview_btn);
//        Button wallpaperBtn = (Button) root.findViewById(R.id.set_wallpaper_btn);
//        previewBtn.setAlpha(1.0f - (float)Math.sqrt(Math.abs(position)));
//        wallpaperBtn.setAlpha(1.0f - (float)Math.sqrt(Math.abs(position)));

        ImageView imageView = (ImageView) root.getChildAt(0);
        if (imageView == null || imageView.getDrawable() == null)
            return;

        if (position < -1) {
        } else if (position <= 1) {

            Matrix matrix = new Matrix();
            matrix.reset();

            float viewWidth = imageView.getWidth();
            float viewHeight = imageView.getHeight();

            float intrinsicWidth = imageView.getDrawable().getIntrinsicWidth();
            float intrinsicHeight = imageView.getDrawable().getIntrinsicHeight();

            float newWidth = viewWidth;
            float newHeight = viewHeight;

            float scale = 0.0f;

			/*
			 * if the difference in width is proportionally greater than difference in
			 * height (ex: 2.5 > 1.9) then scale the bitmap by the height factor
			 */
            if (intrinsicWidth / viewWidth > intrinsicHeight / viewHeight) {
                scale = viewHeight / intrinsicHeight;
                matrix.setScale(scale, scale);
                newWidth = intrinsicWidth * scale;
            }
			/*
			 * else, scale the bitmap by the width factor
			 */
            else {
                scale = viewWidth / intrinsicWidth;
                matrix.setScale(scale, scale);
                newHeight = intrinsicHeight * scale;
            }

			/* offset to center the view horizontally */
            float xOffset = (viewWidth - newWidth) / 2;

			/*
			 * strategy:
			 * 1) get pixels moved relative to the view frame (X)
			 * 2) move the bitmap 0.5f * X
			 */
            float xPos = -position * viewWidth * 0.5f + xOffset;

            // IGNORE BELOW THIS LINE
            float yPos = (viewHeight - newHeight) / 2;
            matrix.postTranslate(xPos, yPos);
            imageView.setScaleType(ImageView.ScaleType.MATRIX);
            imageView.setImageMatrix(matrix);
        } else {
        }
    }
}
