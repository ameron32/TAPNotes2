package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.ui.adapter.ScripturePickerAdapter;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.model.IBible;
import com.ameron32.apps.tapnotes.v2.model.IScripture;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/6/2015.
 */
public class ScripturePickerLayoutFragmentDelegate extends FragmentDelegate
    implements
      IScripturePickerDelegate,
    ScripturePickerAdapter.OnButtonClickedListener {

  public static ScripturePickerLayoutFragmentDelegate create(Fragment fragment) {
    final ScripturePickerLayoutFragmentDelegate delegate = new ScripturePickerLayoutFragmentDelegate();
    delegate.setFragment(fragment);
    return delegate;
  }

  public static final int NUM_OF_PAGES = 3;
  public static final int SCREEN_BOOKS = 0;
  public static final int SCREEN_CHAPTERS = 1;
  public static final int SCREEN_VERSES = 2;

  private static final IScripturePickerDelegateCallbacks stubCallbacks
      = new IScripturePickerDelegate.IScripturePickerDelegateCallbacks() {
    @Override
    public void scripturePickerDone(IScripture scripture) {
      // stub
    }
  };

  private IScripturePickerDelegateCallbacks mCallbacks;

  protected ScripturePickerLayoutFragmentDelegate() {}

  @InjectView(R.id.view_pager)
  ViewPager mPager;

  private ScripturePickerAdapter mAdapter;
  private IBible mBible;

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    confirmHostFragmentHasNecessaryCallbacks();
    ButterKnife.inject(this, view);

    // adapter creation delayed until Bible provided
  }

  @Override
  public void onBibleCreated(IBible bible) {
    this.mBible = bible;

    mAdapter = new ScripturePickerAdapter(mBible);
    mPager.setOffscreenPageLimit(2);
    mAdapter.setOnPageNextClickedListener(this);
    mPager.setAdapter(mAdapter);
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



  @Override
  public void onPageNextClicked(int page, @Nullable IScripture scripture) {
    if (page == SCREEN_VERSES) {
      if (scripture != null) {
        Log.d("onPageNextClicked()", scripture.getBook() + " " + scripture.getChapter() + " " + stringFrom(scripture.getVerses()));
        mCallbacks.scripturePickerDone(scripture);
      }
    } else {
      mPager.setCurrentItem(page + 1);
      mAdapter.focusPage(page + 1);
    }
  }

  @Override
  public void onCancelClicked() {
    mCallbacks.scripturePickerDone(null);
  }

  private String stringFrom(int[] verses) {
    final StringBuilder bs = new StringBuilder();
    for (int i = 0; i < verses.length; i++) {
      if (i != 0) {
        bs.append(",");
      }
      bs.append(verses[i]);
    }
    return bs.toString();
  }
}
