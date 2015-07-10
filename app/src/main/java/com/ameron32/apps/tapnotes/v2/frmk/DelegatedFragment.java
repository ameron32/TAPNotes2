package com.ameron32.apps.tapnotes.v2.frmk;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by klemeilleur on 7/6/2015.
 *
 * This extension of Support Fragment v7 distributes many of the most important lifecycle calls to a delegate. To use, extend FragmentDelegate and
 */
public abstract class DelegatedFragment extends Fragment {

  // NOTE: delegate.onCreateView overrides fragment.onCreateView

  private FragmentDelegate mDelegate;
  private View storeView;

  protected abstract FragmentDelegate createDelegate();

  protected FragmentDelegate getDelegate() {
    return mDelegate;
  }

  /**
   * Called when a fragment is being created as part of a view layout
   * inflation, typically from setting the content view of an activity.  This
   * may be called immediately after the fragment is created from a <fragment>
   * tag in a layout file.  Note this is <em>before</em> the fragment's
   * {@link #onAttach(Activity)} has been called; all you should do here is
   * parse the attributes and save them away.
   * <p>
   * <p>This is called every time the fragment is inflated, even if it is
   * being inflated into a new instance with saved state.  It typically makes
   * sense to re-parse the parameters each time, to allow them to change with
   * different configurations.</p>
   * <p>
   * <p>Here is a typical implementation of a fragment that can take parameters
   * both through attributes supplied here as well from {@link #getArguments()}:</p>
   * <p>
   * {@sample development/samples/ApiDemos/src/com/example/android/apis/app/FragmentArguments.java
   * fragment}
   * <p>
   * <p>Note that parsing the XML attributes uses a "styleable" resource.  The
   * declaration for the styleable used here is:</p>
   * <p>
   * {@sample development/samples/ApiDemos/res/values/attrs.xml fragment_arguments}
   * <p>
   * <p>The fragment can then be declared within its activity's content layout
   * through a tag like this:</p>
   * <p>
   * {@sample development/samples/ApiDemos/res/layout/fragment_arguments.xml from_attributes}
   * <p>
   * <p>This fragment can also be created dynamically from arguments given
   * at runtime in the arguments Bundle; here is an example of doing so at
   * creation of the containing activity:</p>
   * <p>
   * {@sample development/samples/ApiDemos/src/com/example/android/apis/app/FragmentArguments.java
   * create}
   *
   * @param activity           The Activity that is inflating this fragment.
   * @param attrs              The attributes at the tag where the fragment is
   *                           being created.
   * @param savedInstanceState If the fragment is being re-created from
   */
  @Override
  public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
    if (mDelegate == null) {
      mDelegate = createDelegate();
    }
    super.onInflate(activity, attrs, savedInstanceState);
    getDelegate().onInflate(activity, attrs, savedInstanceState);
  }

  /**
   * Called when a fragment is first attached to its activity.
   * {@link #onCreate(Bundle)} will be called after this.
   *
   * @param activity
   */
  @Override
  public void onAttach(Activity activity) {
    if (mDelegate == null) {
      mDelegate = createDelegate();
    }
    super.onAttach(activity);
    getDelegate().onAttach(activity);
  }

  /**
   * Called to do initial creation of a fragment.  This is called after
   * {@link #onAttach(Activity)} and before
   * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
   * <p>
   * <p>Note that this can be called while the fragment's activity is
   * still in the process of being created.  As such, you can not rely
   * on things like the activity's content view hierarchy being initialized
   * at this point.  If you want to do work once the activity itself is
   * created, see {@link #onActivityCreated(Bundle)}.
   *
   * @param savedInstanceState If the fragment is being re-created from
   *                           a previous saved state, this is the state.
   */
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    if (mDelegate == null) {
      mDelegate = createDelegate();
    }
    super.onCreate(savedInstanceState);
    getDelegate().onCreate(savedInstanceState);
  }

  /**
   * Called to have the fragment instantiate its user interface view.
   * This is optional, and non-graphical fragments can return null (which
   * is the default implementation).  This will be called between
   * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
   * <p>
   * <p>If you return a View from here, you will later be called in
   * {@link #onDestroyView} when the view is being released.
   *
   * @param inflater           The LayoutInflater object that can be used to inflate
   *                           any views in the fragment,
   * @param container          If non-null, this is the parent view that the fragment's
   *                           UI should be attached to.  The fragment should not add the view itself,
   *                           but this can be used to generate the LayoutParams of the view.
   * @param savedInstanceState If non-null, this fragment is being re-constructed
   *                           from a previous saved state as given here.
   * @return Return the View for the fragment's UI, or null.
   */
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View superView = super.onCreateView(inflater, container, savedInstanceState);
    final View delegateView = getDelegate().onCreateView(inflater, container, savedInstanceState);
    storeView = (delegateView == null) ? superView : delegateView;
    return storeView;
  }

  /**
   * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
   * has returned, but before any saved state has been restored in to the view.
   * This gives subclasses a chance to initialize themselves once
   * they know their view hierarchy has been completely created.  The fragment's
   * view hierarchy is not however attached to its parent at this point.
   *
   * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
   * @param savedInstanceState If non-null, this fragment is being re-constructed
   */
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getDelegate().onViewCreated(view, savedInstanceState);
  }

  /**
   * Get the root view for the fragment's layout (the one returned by {@link #onCreateView}),
   * if provided.
   *
   * @return The fragment's root view, or null if it has no layout.
   */
  @Nullable
  @Override
  public View getView() {
    return storeView;
  }

  /**
   * Called when the fragment's activity has been created and this
   * fragment's view hierarchy instantiated.  It can be used to do final
   * initialization once these pieces are in place, such as retrieving
   * views or restoring state.  It is also useful for fragments that use
   * {@link #setRetainInstance(boolean)} to retain their instance,
   * as this callback tells the fragment when it is fully associated with
   * the new activity instance.  This is called after {@link #onCreateView}
   * and before {@link #onViewStateRestored(Bundle)}.
   *
   * @param savedInstanceState If the fragment is being re-created from
   *                           a previous saved state, this is the state.
   */
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    getDelegate().onActivityCreated(savedInstanceState);
  }

  /**
   * Called when all saved state has been restored into the view hierarchy
   * of the fragment.  This can be used to do initialization based on saved
   * state that you are letting the view hierarchy track itself, such as
   * whether check box widgets are currently checked.  This is called
   * after {@link #onActivityCreated(Bundle)} and before
   * {@link #onStart()}.
   *
   * @param savedInstanceState If the fragment is being re-created from
   *                           a previous saved state, this is the state.
   */
  @Override
  public void onViewStateRestored(Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    getDelegate().onViewStateRestored(savedInstanceState);
  }

  /**
   * Called when the Fragment is visible to the user.  This is generally
   * tied to {@link Activity#onStart() Activity.onStart} of the containing
   * Activity's lifecycle.
   */
  @Override
  public void onStart() {
    super.onStart();
    getDelegate().onStart();
  }

  /**
   * Called when the fragment is visible to the user and actively running.
   * This is generally
   * tied to {@link Activity#onResume() Activity.onResume} of the containing
   * Activity's lifecycle.
   */
  @Override
  public void onResume() {
    super.onResume();
    getDelegate().onResume();
  }

  /**
   * Called to ask the fragment to save its current dynamic state, so it
   * can later be reconstructed in a new instance of its process is
   * restarted.  If a new instance of the fragment later needs to be
   * created, the data you place in the Bundle here will be available
   * in the Bundle given to {@link #onCreate(Bundle)},
   * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
   * {@link #onActivityCreated(Bundle)}.
   * <p>
   * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
   * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
   * applies here as well.  Note however: <em>this method may be called
   * at any time before {@link #onDestroy()}</em>.  There are many situations
   * where a fragment may be mostly torn down (such as when placed on the
   * back stack with no UI showing), but its state will not be saved until
   * its owning activity actually needs to save its state.
   *
   * @param outState Bundle in which to place your saved state.
   */
  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    getDelegate().onSaveInstanceState(outState);
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    getDelegate().onConfigurationChanged(newConfig);
  }

  /**
   * Called when the Fragment is no longer resumed.  This is generally
   * tied to {@link Activity#onPause() Activity.onPause} of the containing
   * Activity's lifecycle.
   */
  @Override
  public void onPause() {
    super.onPause();
    getDelegate().onPause();
  }

  /**
   * Called when the Fragment is no longer started.  This is generally
   * tied to {@link Activity#onStop() Activity.onStop} of the containing
   * Activity's lifecycle.
   */
  @Override
  public void onStop() {
    super.onStop();
    getDelegate().onStop();
  }

  /**
   * Called when the view previously created by {@link #onCreateView} has
   * been detached from the fragment.  The next time the fragment needs
   * to be displayed, a new view will be created.  This is called
   * after {@link #onStop()} and before {@link #onDestroy()}.  It is called
   * <em>regardless</em> of whether {@link #onCreateView} returned a
   * non-null view.  Internally it is called after the view's state has
   * been saved but before it has been removed from its parent.
   */
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    getDelegate().onDestroyView();
  }

  /**
   * Called when the fragment is no longer in use.  This is called
   * after {@link #onStop()} and before {@link #onDetach()}.
   */
  @Override
  public void onDestroy() {
    super.onDestroy();
    getDelegate().onDestroy();
  }

  /**
   * Called when the fragment is no longer attached to its activity.  This
   * is called after {@link #onDestroy()}.
   */
  @Override
  public void onDetach() {
    super.onDetach();
    getDelegate().onDetach();
  }

  /**
   * Initialize the contents of the Activity's standard options menu.  You
   * should place your menu items in to <var>menu</var>.  For this method
   * to be called, you must have first called {@link #setHasOptionsMenu}.  See
   * {@link Activity#onCreateOptionsMenu(Menu) Activity.onCreateOptionsMenu}
   * for more information.
   *
   * @param menu     The options menu in which you place your items.
   * @param inflater
   * @see #setHasOptionsMenu
   * @see #onPrepareOptionsMenu
   * @see #onOptionsItemSelected
   */
  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    getDelegate().onCreateOptionsMenu(menu, inflater);
  }

  /**
   * Prepare the Screen's standard options menu to be displayed.  This is
   * called right before the menu is shown, every time it is shown.  You can
   * use this method to efficiently enable/disable items or otherwise
   * dynamically modify the contents.  See
   * {@link Activity#onPrepareOptionsMenu(Menu) Activity.onPrepareOptionsMenu}
   * for more information.
   *
   * @param menu The options menu as last shown or first initialized by
   *             onCreateOptionsMenu().
   * @see #setHasOptionsMenu
   * @see #onCreateOptionsMenu
   */
  @Override
  public void onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    getDelegate().onPrepareOptionsMenu(menu);
  }
}
