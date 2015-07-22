package com.ameron32.apps.tapnotes.v2.ui.fragment;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.di.controller.ActivitySnackBarController;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.model.IScripture;
import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.scripture.Scripture;
import com.ameron32.apps.tapnotes.v2.ui.delegate.IScripturePickerDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.IScripturePickerDelegate.IScripturePickerDelegateCallbacks;
import com.ameron32.apps.tapnotes.v2.ui.delegate.ScripturePickerLayoutFragmentDelegate;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by klemeilleur on 6/15/2015.
 */
public class ScripturePickerFragment extends TAPFragment
    implements IScripturePickerDelegateCallbacks
{

  public static ScripturePickerFragment create() {
    final ScripturePickerFragment f = new ScripturePickerFragment();
    f.setArguments(new Bundle());
    return f;
  }



  public ScripturePickerFragment() {}

  private Callbacks mCallbacks;
  private IScripturePickerDelegate mDelegate;

  @Inject
  ActivitySnackBarController mSnackBar;



  @Override
  protected FragmentDelegate createDelegate() {
    return ScripturePickerLayoutFragmentDelegate.create(ScripturePickerFragment.this);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof Callbacks) {
      mCallbacks = (Callbacks) activity;
    } else {
      throw new IllegalStateException(activity.getClass().getSimpleName()
          + "must implement " + Callbacks.class.getSimpleName() + " in order to use "
          + ScripturePickerFragment.class.getSimpleName());
    }
  }

  @Override
  public void onDetach() {
    mCallbacks = null;
    super.onDetach();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_mni_scripture_picker, container, false);
    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    confirmDelegateHasInterface();
  }

  @Override
  public void scriptureComplete(IScripture scripture) {
    mCallbacks.scripturePrepared(scripture);
  }

  private void confirmDelegateHasInterface() {
    if (getDelegate() instanceof IScripturePickerDelegate) {
      mDelegate = ((IScripturePickerDelegate) getDelegate());
    } else {
      throw new IllegalStateException("delegate " +
          "should implement " + IScripturePickerDelegate.class.getSimpleName() +
          " to allow necessary method calls.");
    }
  }


  private int getColorFromAttribute(@AttrRes int attr, @ColorRes int defaultColor) {
    final TypedValue typedValue = new TypedValue();
    getActivity().getTheme()
            .resolveAttribute(attr, typedValue, true);
    final int[] accentColor = new int[] { attr };
    final int indexOfAttrColor = 0;
    final TypedArray a = getContext()
            .obtainStyledAttributes(typedValue.data, accentColor);
    final int color = a.getColor(indexOfAttrColor, defaultColor);
    a.recycle();
    return color;
  }

  @Override
  public void onDestroyView() {
    mDelegate = null;
    ButterKnife.reset(this);
    super.onDestroyView();
  }



  public interface Callbacks {

    void scripturePrepared(IScripture scripture);
  }
}
