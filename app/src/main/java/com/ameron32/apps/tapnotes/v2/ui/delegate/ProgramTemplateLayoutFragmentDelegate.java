package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.support.v4.app.Fragment;

import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;

/**
 * Created by klemeilleur on 7/6/2015.
 */
public class ProgramTemplateLayoutFragmentDelegate extends FragmentDelegate
    implements IProgramTemplateDelegate
{

  public static ProgramTemplateLayoutFragmentDelegate create(Fragment fragment) {
    final ProgramTemplateLayoutFragmentDelegate delegate = new ProgramTemplateLayoutFragmentDelegate();
    delegate.setFragment(fragment);
    return delegate;
  }

  protected ProgramTemplateLayoutFragmentDelegate() {}


}
