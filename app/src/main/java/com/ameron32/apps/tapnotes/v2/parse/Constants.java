package com.ameron32.apps.tapnotes.v2.parse;

import com.parse.ParseUser;

/**
 * Created by klemeilleur on 6/29/2015.
 */
public class Constants {

  public static final String CONVENTION2015_PROGRAM_OBJECT_ID = "BPCRNbT6Lf";

  // OBJECT KEY NAMES on Parse Server
  public static final String NOTE_OBJECT_NAME = "Note";
  public static final String TALK_OBJECT_NAME = "Talk";
  public static final String PROGRAM_OBJECT_NAME = "Program";

  // COLUMN KEYS for OBJECTS on Parse Server
  // PROGRAM
  public static final String PROGRAM_NAME_STRING_KEY = "name";

  // TALK
  public static final String TALK_oPROGRAM_OBJECT_KEY = "oProgram";
  public static final String TALK_DATE_STRING_KEY = "date";
  public static final String TALK_TYPE_STRING_KEY = "type";
  public static final String TALK_METADATA_STRING_KEY = "metadata";
  public static final String TALK_TITLE_STRING_KEY = "title";
  public static final String TALK_SCRIPTURES_STRING_KEY = "scriptures";

  // NOTE
  public static final String NOTE_oPROGRAM_OBJECT_KEY = "oProgram";
  public static final String NOTE_oTALK_OBJECT_KEY = "oTalk";
  public static final String NOTE_uOWNER_USER_KEY = "uOwner";
  public static final String NOTE_TEXT_STRING_KEY = "text";
  public static final String NOTE_TAGS_ARRAY_KEY = "tags";
  public static final String NOTE_NEXTNOTE_OBJECT_KEY = "nextNote";

}
