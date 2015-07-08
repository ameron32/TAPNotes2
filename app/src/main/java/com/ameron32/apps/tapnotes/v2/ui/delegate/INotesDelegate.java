package com.ameron32.apps.tapnotes.v2.ui.delegate;

import com.ameron32.apps.tapnotes.v2.model.INote;

import java.util.List;

/**
 * Created by klemeilleur on 7/8/2015.
 */
public interface INotesDelegate {

  void synchronizeNotes(List<INote> allNotes);
  void addNotes(List<INote> notesToAdd);
  void removeNotes(List<INote> notesToRemove);
  void changeNotes(List<INote> notesToChange);



  public interface INotesDelegateCallbacks {

    void onUserClickBoldNote(String noteId);
    void onUserClickImportantNote(String noteId);
    void onUserClickEditNote(String noteId);
    void onUserClickDeleteNote(String noteId);

    void onUserRepositionNote(
        String repositionedNoteId,
        String noteIdBeforeOriginOfRepositionedNote,
        String noteIdBeforeTargetOfRepositionedNote);
  }
}
