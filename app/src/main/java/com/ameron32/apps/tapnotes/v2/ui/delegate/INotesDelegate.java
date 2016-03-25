package com.ameron32.apps.tapnotes.v2.ui.delegate;

import com.ameron32.apps.tapnotes.v2.data.model.IBible;
import com.ameron32.apps.tapnotes.v2.data.model.INote;

import java.util.List;

/**
 * Created by klemeilleur on 7/8/2015.
 */
public interface INotesDelegate {

  void synchronizeNotes(List<INote> allNotes);
  void addNotes(List<INote> notesToAdd);
  void removeNotes(List<INote> notesToRemove);
  void replaceNotes(List<INote> notesToReplace);

  void onBibleCreated(IBible bible);



  public interface INotesDelegateCallbacks {

    void onUserClickBoldNote(INote note);
    void onUserClickImportantNote(INote note);
    void onUserClickEditNote(INote note);
    void onUserClickDeleteNote(INote note);

    void onUserRepositionNote(
        INote repositionedNote,
        INote noteBeforeOriginOfRepositionedNote,
        INote noteBeforeTargetOfRepositionedNote);
  }
}
