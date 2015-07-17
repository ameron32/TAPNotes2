package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.model.INote;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/6/2015.
 */
public class ScripturePickerLayoutFragmentDelegate extends FragmentDelegate
    implements IScripturePickerDelegate
{

  public static ScripturePickerLayoutFragmentDelegate create(Fragment fragment) {
    final ScripturePickerLayoutFragmentDelegate delegate = new ScripturePickerLayoutFragmentDelegate();
    delegate.setFragment(fragment);
    return delegate;
  }

  private static final IScripturePickerDelegateCallbacks stubCallbacks
      = new IScripturePickerDelegate.IScripturePickerDelegateCallbacks() {

  };

  private IScripturePickerDelegateCallbacks mCallbacks;

  protected ScripturePickerLayoutFragmentDelegate() {}



  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    confirmHostFragmentHasNecessaryCallbacks();
    ButterKnife.inject(this, view);
  }

  @Override
  public void onDestroyView() {
    ButterKnife.reset(this);
    mCallbacks = stubCallbacks;
    super.onDestroyView();
  }

  private void confirmHostFragmentHasNecessaryCallbacks() {
    if (getFragment() instanceof IScripturePickerDelegateCallbacks) {
      mCallbacks = ((IScripturePickerDelegateCallbacks) getFragment());
    } else {
      throw new IllegalStateException("host fragment " +
          "should implement " + IScripturePickerDelegate.IScripturePickerDelegateCallbacks.class.getSimpleName() +
          "to support callback methods.");
    }
  }
}
