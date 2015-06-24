package com.ameron32.apps.tapnotes.v2.ui.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AnimatorRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ameron32.apps.tapnotes.v2.R;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by klemeilleur on 6/12/2015.
 */
public class AnimatingPaneLayout extends FrameLayout {

  private View mLeftPane;
  private View mMainPane;
  private @AnimatorRes int zoomAnimatorStart;
  private @AnimatorRes int zoomAnimatorEnd;
  private @AnimatorRes int leftAnimatorStart;
  private @AnimatorRes int leftAnimatorEnd;
  private @AnimatorRes int rightAnimatorStart;
  private @AnimatorRes int rightAnimatorEnd;
  private @AnimatorRes int nullAnimator;
  private boolean isEnabled = false;
  private AnimationOptions leftToTopOptions;
  private AnimationOptions mainToBottomOptions;
  private AnimationOptions leftToBottomOptions;
  private AnimationOptions mainToTopOptions;


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
    nullAnimator = R.animator.null_delay_only;
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
    options.firstSet.play(options.start).after(options.zoomOut);
    options.secondSet.play(options.end);
    options.secondSet.play(options.zoomIn).after(options.end);

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
      reset();
    }

    @Override    public void onAnimationCancel(Animator animation) {    }

    @Override    public void onAnimationRepeat(Animator animation) {    }
  }
}
