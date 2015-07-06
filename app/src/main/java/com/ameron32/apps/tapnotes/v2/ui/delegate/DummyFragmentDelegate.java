package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.support.v4.app.Fragment;

import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;

/**
 * Created by klemeilleur on 7/6/2015.
 */
public class DummyFragmentDelegate extends FragmentDelegate {

  public static DummyFragmentDelegate create(Fragment fragment) {
    final DummyFragmentDelegate delegate = new DummyFragmentDelegate();
    delegate.setFragment(fragment);
    return delegate;
  }

  protected DummyFragmentDelegate() {}

}
