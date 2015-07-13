package com.ameron32.apps.tapnotes.v2.model;

/**
 * Created by klemeilleur on 6/29/2015.
 */
public interface INote extends IObject {

  public static final int TAG_BOLD_NOTE = 1;
  public static final int TAG_IMPORTANT_NOTE = 2;

  public enum NoteType {
    STANDARD, SPEAKER, SCRIPTURE_ONLY, ATTENDANCE_COUNT, BAPTISM_COUNT;

    public static int getInteger(NoteType type) {
      return type.ordinal() + 101;
    }
  }


  String getNoteText();
  boolean isImportantNote();
  boolean isBoldNote();
  String getNextNoteId();
}
