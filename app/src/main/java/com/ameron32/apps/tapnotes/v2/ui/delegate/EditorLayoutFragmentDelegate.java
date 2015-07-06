package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.support.v4.app.Fragment;

import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;

/**
 * Created by klemeilleur on 7/6/2015.
 */
public class EditorLayoutFragmentDelegate extends FragmentDelegate {

  public static EditorLayoutFragmentDelegate create(Fragment fragment) {
    final EditorLayoutFragmentDelegate delegate = new EditorLayoutFragmentDelegate();
    delegate.setFragment(fragment);
    return delegate;
  }

  protected EditorLayoutFragmentDelegate() {}

}
