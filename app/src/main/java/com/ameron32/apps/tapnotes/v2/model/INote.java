package com.ameron32.apps.tapnotes.v2.model;

/**
 * Created by klemeilleur on 6/29/2015.
 */
public interface INote extends IObject {

  public static final int TAG_BOLD_NOTE = 1;
  public static final int TAG_IMPORTANT_NOTE = 2;

  public enum NoteType {

    STANDARD, SPEAKER, SCRIPTURE_ONLY, ATTENDANCE_COUNT, BAPTISM_COUNT;

    public static final int RANGE_MINIMUM_NOTETYPE_TAG = 100;
    public static final int OFFSET_NOTETYPE_TAG = 101;

    public static int getTagInteger(NoteType type) {
      final int tag = type.ordinal() + OFFSET_NOTETYPE_TAG;
      return tag;
    }

    public static NoteType getTypeFrom(int tag) {
      final int position = tag - OFFSET_NOTETYPE_TAG;
      return NoteType.values()[position];
    }
  }


  String getNoteText();
  boolean isImportantNote();
  boolean isBoldNote();
  NoteType getNoteType();
}
