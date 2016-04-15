package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.support.annotation.Nullable;

import com.ameron32.apps.tapnotes.v2.data.model.ISearchableBible;
import com.ameron32.apps.tapnotes.v2.data.model.IScripture;

/**
 * Created by klemeilleur on 7/17/2015.
 */
public interface IScripturePickerDelegate {

  void onBibleCreated(ISearchableBible bible);



  public interface IScripturePickerDelegateCallbacks {

    void scripturePickerDone(@Nullable IScripture scripture);
  }
}
