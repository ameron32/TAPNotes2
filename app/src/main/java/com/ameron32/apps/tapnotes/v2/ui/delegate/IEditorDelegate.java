package com.ameron32.apps.tapnotes.v2.ui.delegate;

import com.ameron32.apps.tapnotes.v2.model.INote;

/**
 * Created by klemeilleur on 7/8/2015.
 */
public interface IEditorDelegate {

  void updateEditorText(String newEditorText);



  public interface IEditorDelegateCallbacks {

    void onSubmitClicked(String editorText, INote.NoteType type);
  }
}
