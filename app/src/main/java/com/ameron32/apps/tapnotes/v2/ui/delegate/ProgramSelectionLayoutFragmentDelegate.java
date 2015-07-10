package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.support.v4.app.Fragment;

import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;

/**
 * Created by klemeilleur on 7/6/2015.
 */
public class ProgramSelectionLayoutFragmentDelegate extends FragmentDelegate
    implements IProgramSelectionDelegate
{

  public static ProgramSelectionLayoutFragmentDelegate create(Fragment fragment) {
    final ProgramSelectionLayoutFragmentDelegate delegate = new ProgramSelectionLayoutFragmentDelegate();
    delegate.setFragment(fragment);
    return delegate;
  }

  protected ProgramSelectionLayoutFragmentDelegate() {}


}
