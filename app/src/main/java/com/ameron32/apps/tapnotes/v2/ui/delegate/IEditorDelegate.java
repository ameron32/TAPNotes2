package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.support.annotation.Nullable;

import com.ameron32.apps.tapnotes.v2.data.model.IBible;
import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.IScripture;
import com.ameron32.apps.tapnotes.v2.ui.mc_sanitizer.ISanitizer;

/**
 * Created by klemeilleur on 7/8/2015.
 */
public interface IEditorDelegate {

  void updateEditorText(String newEditorText, @Nullable INote note);

  void onSanitizerCreated(ISanitizer sanitizer);

  void onBibleCreated(IBible bible);

  void onInjectScriptureFromPicker(IScripture scripture);



  public interface IEditorDelegateCallbacks {

    void onSubmitClicked(String editorText, INote.NoteType type, @Nullable INote note);

    void setSanitizerCallbacks(ISanitizer.ISanitizerCallbacks callbacks);

    void onKeyNextTalk();

    void onKeyPreviousTalk();
  }
}
