package com.ameron32.apps.tapnotes.v2.model;

/**
 * Created by klemeilleur on 7/8/2015.
 */
public interface INoteEditable extends INote {

  void setImportantNote(boolean state);
  void setBoldNote(boolean state);
}