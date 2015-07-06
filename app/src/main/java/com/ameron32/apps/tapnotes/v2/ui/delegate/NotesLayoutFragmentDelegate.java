package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.support.v4.app.Fragment;

import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;

/**
 * Created by klemeilleur on 7/6/2015.
 */
public class NotesLayoutFragmentDelegate extends FragmentDelegate {

  public static NotesLayoutFragmentDelegate create(Fragment fragment) {
    final NotesLayoutFragmentDelegate delegate = new NotesLayoutFragmentDelegate();
    delegate.setFragment(fragment);
    return delegate;
  }

  protected NotesLayoutFragmentDelegate() {}


}
