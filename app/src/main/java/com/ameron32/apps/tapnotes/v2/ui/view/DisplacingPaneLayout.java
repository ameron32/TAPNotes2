package com.ameron32.apps.tapnotes.v2.ui.view;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.View;

import com.ameron32.apps.tapnotes.v2.R;

/**
 * Created by klemeilleur on 6/25/2015.
 * UNNECESSARY WITH IMPROVEMENTS TO OverlappingPaneLayout
 */
public class DisplacingPaneLayout extends SlidingPaneLayout implements
      SlidingPaneLayout.PanelSlideListener {

  private View mLeftPane;
  private View mMainPane;
  private int mDefaultPaddingRight;
  private int mOffsetPaddingRight;
  private boolean isEnabled = false;

  public DisplacingPaneLayout(Context context) {
    super(context);
  }

  public DisplacingPaneLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public DisplacingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    if (getChildCount() < 2) {
      // revert to simple framelayout
      return;
    }

    mLeftPane = getChildAt(0);
    mMainPane = getChildAt(1);
    isEnabled = true;

    setPanelSlideListener(this);

    mDefaultPaddingRight = mMainPane.getPaddingRight();
    mOffsetPaddingRight = Math.round(getContext().getResources().getDimension(R.dimen.program_bar_width));
  }

  public void toggleLayout() {
    if (isOpen()) {
      closePane();
    } else {
      openPane();
    }
  }

  @Override
  public void onPanelOpened(View panel) {
    openBuffer();
  }

  @Override
  public void onPanelSlide(View panel, float slideOffset) {

  }

  @Override
  public void onPanelClosed(View panel) {
    closeBuffer();
  }

  private void openBuffer() {
    mMainPane.setPadding(
        mMainPane.getPaddingLeft(),
        mMainPane.getPaddingTop(),
        mMainPane.getPaddingRight() + mOffsetPaddingRight,
        mMainPane.getPaddingBottom());
  }

  private void closeBuffer() {
    mMainPane.setPadding(
        mMainPane.getPaddingLeft(),
        mMainPane.getPaddingTop(),
        mMainPane.getPaddingRight() - mOffsetPaddingRight,
        mMainPane.getPaddingBottom());
  }
}
