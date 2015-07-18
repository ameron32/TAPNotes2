package com.ameron32.apps.tapnotes.v2.ui.delegate;

import com.ameron32.apps.tapnotes.v2.model.IScripture;

/**
 * Created by klemeilleur on 7/17/2015.
 */
public interface IScripturePickerDelegate {





  public interface IScripturePickerDelegateCallbacks {

    void scriptureComplete(IScripture scripture);
  }
}
