package com.ameron32.apps.tapnotes.v2.frmk;

import com.ameron32.apps.tapnotes.v2.model.INote;

import java.util.List;

/**
 * Created by klemeilleur on 7/15/2015.
 */
public interface INoteHandler {

  void notesChanged(List<INote> notes);
  void notesAdded(List<INote> notes);
}
