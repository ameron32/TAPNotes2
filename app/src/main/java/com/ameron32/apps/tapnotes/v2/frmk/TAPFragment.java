package com.ameron32.apps.tapnotes.v2.frmk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ameron32.apps.tapnotes.v2.di.Injector;
import com.ameron32.apps.tapnotes.v2.frmk.DelegatedFragment;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.DummyFragmentDelegate;

/**
 * Created by klemeilleur on 6/12/2015.
 */
public abstract class TAPFragment extends DelegatedFragment {

  public TAPFragment() {}

  @Override
  protected FragmentDelegate createDelegate() {
    return DummyFragmentDelegate.create(this);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    Injector.INSTANCE.inject(this);
    super.onCreate(savedInstanceState);
  }

  public Context getContext() {
    return getActivity();
  }
}
