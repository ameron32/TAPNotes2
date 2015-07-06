package com.ameron32.apps.tapnotes.v2.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.ui.delegate.ProgramSelectionLayoutFragmentDelegate;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProgramSelectionFragment extends TAPFragment {

  public static ProgramSelectionFragment create() {
    ProgramSelectionFragment f = new ProgramSelectionFragment();
    // set Arguments
    return f;
  }

  TestCallbacks mCallbacks;

  public ProgramSelectionFragment() {}

  @Override
  protected FragmentDelegate createDelegate() {
    return ProgramSelectionLayoutFragmentDelegate.create(ProgramSelectionFragment.this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_program_selection, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
  }

  @OnClick(R.id.testing_button_mni)
  void onClick() {
    getCallbacks().startMNIActivity("0");
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof TestCallbacks) {
      mCallbacks = (TestCallbacks) activity;
    } else {
      final String message = activity.getClass().getSimpleName() + " must implement " + TestCallbacks.class.getSimpleName();
      throw new IllegalStateException(message);
    }
  }

  @Override
  public void onDetach() {
    mCallbacks = null;
    super.onDetach();
  }

  public interface TestCallbacks {
    void startMNIActivity(final String programId);
  }

  private TestCallbacks getCallbacks() {
    if (mCallbacks == null) {
      Log.e(ProgramSelectionFragment.class.getSimpleName(),
          TestCallbacks.class.getSimpleName() + "#mCallbacks was null");
      return null;
    }
    return mCallbacks;
  }
}
