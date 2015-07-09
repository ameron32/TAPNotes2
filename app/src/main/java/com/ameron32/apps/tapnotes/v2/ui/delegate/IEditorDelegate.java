package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.support.annotation.Nullable;

import com.ameron32.apps.tapnotes.v2.model.INote;

/**
 * Created by klemeilleur on 7/8/2015.
 */
public interface IEditorDelegate {

  void updateEditorText(String newEditorText, @Nullable String noteId);



  public interface IEditorDelegateCallbacks {

    void onSubmitClicked(String editorText, INote.NoteType type, @Nullable String noteId);
  }
}
