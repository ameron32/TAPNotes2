package com.ameron32.apps.tapnotes.v2.ui.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AnimatorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.IDualLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by klemeilleur on 6/12/2015.
 */
public class AnimatingPaneLayout
    extends FrameLayout
    implements IDualLayout {

  /*
   * TODO Improve animation when isDisplacement = true [AKA Tablet devices]
   */

  @Nullable private View mLeftPane;
  @Nullable private View mMainPane;
  @NonNull  private @AnimatorRes int zoomAnimatorStart;
  @NonNull  private @AnimatorRes int zoomAnimatorEnd;
  @NonNull  private @AnimatorRes int leftAnimatorStart;
  @NonNull  private @AnimatorRes int leftAnimatorEnd;
  @NonNull  private @AnimatorRes int rightAnimatorStart;
  @NonNull  private @AnimatorRes int rightAnimatorEnd;
//  @NonNull  private @AnimatorRes int nullAnimator;
  @NonNull  private boolean isEnabled = false;
  @NonNull  private boolean isDisplacement = false;
  @NonNull  private AnimationOptions leftToTopOptions;
  @NonNull  private AnimationOptions mainToBottomOptions;
  @NonNull  private AnimationOptions leftToBottomOptions;
  @NonNull  private AnimationOptions mainToTopOptions;

  @NonNull  private int mDefaultPaddingLeft;
  @NonNull  private int mOffsetPaddingLeft;

  @Nullable private PanelListener mDefaultPanelListener;
  @NonNull  private List<PanelListener> mListeners;


  public AnimatingPaneLayout(Context context) {
    super(context);
  }

  public AnimatingPaneLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    initAttrs(context, attrs);
  }

  public AnimatingPaneLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initAttrs(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public AnimatingPaneLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initAttrs(context, attrs);
  }

  private void initAttrs(Context context, AttributeSet attrs) {
    // assign the animators
    zoomAnimatorStart = R.animator.zoom_out_animator;
    zoomAnimatorEnd = R.animator.zoom_in_animator;
    leftAnimatorStart = R.animator.dodge_left_to_back;
    leftAnimatorEnd = R.animator.dodge_return_left_to_back;
    rightAnimatorStart = R.animator.dodge_right;
    rightAnimatorEnd = R.animator.dodge_return_right;
//    nullAnimator = R.animator.null_delay_only;
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
    isDisplacement = getContext().getResources().getBoolean(R.bool.dual_layout_uses_displacement);
    mListeners = new ArrayList<>();

    mDefaultPaddingLeft = mMainPane.getPaddingLeft();
    mOffsetPaddingLeft = Math.round(getContext().getResources().getDimension(R.dimen.program_bar_width));

    if (isDisplacement) {
      mDefaultPanelListener = new PanelListener() {

        @Override
        public void onPanelOpened() {
          openBuffer();
        }

        @Override
        public void onPanelClosed() {
          closeBuffer();
        }

        private void openBuffer() {
          mMainPane.setPadding(
              mDefaultPaddingLeft + mOffsetPaddingLeft,
              mMainPane.getPaddingTop(),
              mMainPane.getPaddingRight(),
              mMainPane.getPaddingBottom());
        }

        private void closeBuffer() {
          mMainPane.setPadding(
              mDefaultPaddingLeft - mOffsetPaddingLeft,
              mMainPane.getPaddingTop(),
              mMainPane.getPaddingRight(),
              mMainPane.getPaddingBottom());
        }
      };
      addPanelListener(mDefaultPanelListener);
    }
  }

  public void addPanelListener(PanelListener listener) {
    if (!this.mListeners.contains(listener)) {
      this.mListeners.add(listener);
    }
  }

  public void removePanelListener(PanelListener listener) {
    if (this.mListeners.contains(listener)) {
      this.mListeners.remove(listener);
    }
  }

  private void dispatchOnPanelOpened() {
    if (this.mListeners.isEmpty()) {
      return;
    }

    for (PanelListener l : this.mListeners) {
      l.onPanelOpened();
    }
  }

  private void dispatchOnPanelClosed() {
    if (this.mListeners.isEmpty()) {
      return;
    }

    for (PanelListener l : this.mListeners) {
      l.onPanelClosed();
    }
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    reset();
  }

  private void reset() {
    leftToTopOptions = loadAnimationOptions(Pane.Left, Position.Bottom);
    mainToBottomOptions = loadAnimationOptions(Pane.Main, Position.Top);
    leftToBottomOptions = loadAnimationOptions(Pane.Left, Position.Top);
    mainToTopOptions = loadAnimationOptions(Pane.Main, Position.Bottom);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    leftToTopOptions = mainToBottomOptions = leftToBottomOptions = mainToTopOptions = null;
  }

  @Override
  public void toggleLayout() {
    if (isAnimating() || !isEnabled) {
      return;
    }

    performAnimation();
  }

  private void performAnimation() {
    setAnimating(true);
    if (!isLeftPaneOnTop()) {
      animateViewToPositionWithAnimator(leftToTopOptions);
      animateViewToPositionWithAnimator(mainToBottomOptions);
    } else {
      animateViewToPositionWithAnimator(leftToBottomOptions);
      animateViewToPositionWithAnimator(mainToTopOptions);
    }
  }

  class AnimationOptions {
    public View view;
    public Pane pane;
    public AnimatorSet firstSet, secondSet;
    public Animator zoomOut, zoomIn, start, end;
    public Animator.AnimatorListener switchActions;
    public Animator.AnimatorListener animationComplete;
  }

  private AnimationOptions loadAnimationOptions(Pane pane, Position position) {
    final AnimationOptions options = new AnimationOptions();
    options.pane = pane;

    options.firstSet = new AnimatorSet();
    options.secondSet = new AnimatorSet();

    options.zoomOut = AnimatorInflater.loadAnimator(getContext(), zoomAnimatorStart);
    options.zoomIn = AnimatorInflater.loadAnimator(getContext(), zoomAnimatorEnd);
    switch(pane) {
      case Left:
        options.start = AnimatorInflater.loadAnimator(getContext(), leftAnimatorStart);
        options.end = AnimatorInflater.loadAnimator(getContext(), leftAnimatorEnd);
        options.view = mLeftPane;
        break;
      case Main:
        options.start = AnimatorInflater.loadAnimator(getContext(), rightAnimatorStart);
        options.end = AnimatorInflater.loadAnimator(getContext(), rightAnimatorEnd);
        options.view = mMainPane;
    }

    options.animationComplete = new AnimationComplete();
    if (position == Position.Top && options.view != null) {
      options.switchActions = new SwitchActions(options.view, options.secondSet,
          new Runnable() {

        @Override
        public void run() {
          sendViewToBack(options.view);
        }
      });
    } else {
      options.switchActions = new SwitchActions(options.view, options.secondSet, null);
    }

    return options;
  }

  private void animateViewToPositionWithAnimator(final AnimationOptions options) {
    if (options == null) {
      setAnimating(false);
      return;
    }

    options.zoomOut.setTarget(options.view);
    options.zoomIn.setTarget(options.view);
    options.start.setTarget(options.view);
    options.end.setTarget(options.view);

    options.firstSet.play(options.zoomOut);
    options.firstSet.play(options.start).with(options.zoomOut);
    options.secondSet.play(options.end);
    options.secondSet.play(options.zoomIn).with(options.end);

    options.firstSet.addListener(options.switchActions);
    options.secondSet.addListener(options.animationComplete);

    options.firstSet.start();
  }



  private enum Position { Top, Bottom }
  private enum Pane { Left, Main }

  private final AtomicBoolean isAnimating = new AtomicBoolean(false);
  private final AtomicBoolean isLeftPaneOnTop = new AtomicBoolean(false);

  private boolean isAnimating() {
    return isAnimating.get();
  }

  private void setAnimating(boolean state) {
    if (state != isAnimating()) {
      isAnimating.set(state);
    }
  }

  private void setLeftPaneOnTop(boolean state) {
    if (state != isLeftPaneOnTop()) {
      isLeftPaneOnTop.set(state);
    }
  }

  private boolean isLeftPaneOnTop() {
    return isLeftPaneOnTop.get();
  }

  /*
   * TODO replace "alternating" functionality with LEFT swap TOP and BOTTOM despite other child views
   */
  public void sendViewToBack(final View child) {
    final ViewGroup parent = (ViewGroup) child.getParent();
    if (null != parent) {
      parent.removeView(child);
      parent.addView(child, 0);
      if (child.getId() == mLeftPane.getId()) {
        setLeftPaneOnTop(false);
      } else {
        setLeftPaneOnTop(true);
      }
    }
  }

  public interface PanelListener {
    void onPanelOpened();
    void onPanelClosed();
  }

  class SwitchActions implements Animator.AnimatorListener {

    private static final float SCALE_AMOUNT = 0.95f;

    private final View mView;
    private final AnimatorSet mSet;
    private final Runnable mRunnable;

    public SwitchActions(View view, AnimatorSet set, Runnable runnable) {
      mView = view;
      mSet = set;
      mRunnable = runnable;
    }

    @Override    public void onAnimationStart(Animator animation) {    }

    @Override
    public void onAnimationEnd(Animator animation) {
      if (mView == null || mSet == null) {
        return;
      }

      mView.setScaleX(SCALE_AMOUNT);
      mView.setScaleY(SCALE_AMOUNT);
      if (mRunnable != null) {
        mRunnable.run();
      }
      mSet.start();
    }

    @Override    public void onAnimationCancel(Animator animation) {    }

    @Override    public void onAnimationRepeat(Animator animation) {    }
  }

  class AnimationComplete implements Animator.AnimatorListener {
    @Override    public void onAnimationStart(Animator animation) {    }

    @Override
    public void onAnimationEnd(Animator animation) {
      setAnimating(false);
      if (isLeftPaneOnTop()) { dispatchOnPanelOpened(); }
        else { dispatchOnPanelClosed(); }
      reset();
    }

    @Override    public void onAnimationCancel(Animator animation) {    }

    @Override    public void onAnimationRepeat(Animator animation) {    }
  }
}
