package com.ameron32.apps.tapnotes.v2.frmk;

import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.model.IScripture;

/**
 * Created by klemeilleur on 7/15/2015.
 */
public interface IEditHandler {

  void displayNoteToEdit(INote note);

  void provideScriptureToEditor(IScripture scripture);
}
