package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.support.annotation.Nullable;

import com.ameron32.apps.tapnotes.v2.model.INote;

/**
 * Created by klemeilleur on 7/8/2015.
 */
public interface IEditorDelegate {

  void updateEditorText(String newEditorText, @Nullable INote note);



  public interface IEditorDelegateCallbacks {

    /**
     * @param editorText
     * @param type
     * @param noteId
     * provide a noteId when editing an existing note (see updateEditorText())
     * provide null when submitting a brand new note
     */
    void onSubmitClicked(String editorText, INote.NoteType type, @Nullable INote note);
  }
}
