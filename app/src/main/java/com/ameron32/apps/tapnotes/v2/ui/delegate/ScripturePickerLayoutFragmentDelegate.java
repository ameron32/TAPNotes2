package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.adapter.ScripturePickerAdapter;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.model.IScripture;
import com.ameron32.apps.tapnotes.v2.scripture.Scripture;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/6/2015.
 */
public class ScripturePickerLayoutFragmentDelegate extends FragmentDelegate
    implements
      IScripturePickerDelegate,
      ScripturePickerAdapter.OnPageNextClickedListener {

  public static ScripturePickerLayoutFragmentDelegate create(Fragment fragment) {
    final ScripturePickerLayoutFragmentDelegate delegate = new ScripturePickerLayoutFragmentDelegate();
    delegate.setFragment(fragment);
    return delegate;
  }

  private static final IScripturePickerDelegateCallbacks stubCallbacks
      = new IScripturePickerDelegate.IScripturePickerDelegateCallbacks() {
    @Override
    public void scriptureComplete(IScripture scripture) {
      // stub
    }
  };

  private IScripturePickerDelegateCallbacks mCallbacks;

  protected ScripturePickerLayoutFragmentDelegate() {}


  @InjectView(R.id.view_pager)
  ViewPager mPager;

  private ScripturePickerAdapter mAdapter;

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    confirmHostFragmentHasNecessaryCallbacks();
    ButterKnife.inject(this, view);

    mAdapter = new ScripturePickerAdapter(getContext());
    mPager.setAdapter(mAdapter);
    mAdapter.setOnPageNextClickedListener(this);
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


  public static final int NUM_OF_PAGES = 3;

  @Override
  public void onPageNextClicked(int page) {
    if (page == NUM_OF_PAGES) {
      mCallbacks.scriptureComplete(Scripture.generate(0, 0, new int[]{0, 1, 2}));
    }
    mPager.setCurrentItem(page +1);
  }

}
