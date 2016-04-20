package com.ameron32.apps.tapnotes.v2.data.parse;

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
  public static final String PROGRAM_PROGRAMIMAGE_FILE_KEY = "programImage";
  public static final String PROGRAM_CREATEDAT_DATE_KEY = "createdAt";
  public static final String PROGRAM_UPDATEDAT_DATE_KEY = "updatedAt";

  // TALK
  public static final String TALK_oPROGRAM_OBJECT_KEY = "oProgram";
  public static final String TALK_DATE_STRING_KEY = "date";
  public static final String TALK_TYPE_STRING_KEY = "type";
  public static final String TALK_METADATA_STRING_KEY = "metadata";
  public static final String TALK_TITLE_STRING_KEY = "title";
  public static final String TALK_SCRIPTURES_STRING_KEY = "scriptures";
  public static final String TALK_SEQUENCE_STRING_KEY = "sequence";
  public static final String TALK_DURATIONMINUTES_NUMBER_KEY = "durationMinutes";
  public static final String TALK_HEADERIMAGE_FILE_KEY = "headerImage";
  public static final String TALK_HEADERIMAGEURL_STRING_KEY = "headerImageUrl";
  public static final String TALK_CREATEDAT_DATE_KEY = "createdAt";

  // NOTE
  public static final String NOTE_oPROGRAM_OBJECT_KEY = "oProgram";
  public static final String NOTE_oTALK_OBJECT_KEY = "oTalk";
  public static final String NOTE_uOWNER_USER_KEY = "uOwner";
  public static final String NOTE_TEXT_STRING_KEY = "text";
  public static final String NOTE_TAGS_ARRAY_KEY = "tags";
  public static final String NOTE_CREATEDAT_DATE_KEY = "createdAt";
  public static final String NOTE_UPDATEDAT_DATE_KEY = "updatedAt";

  public static final String CONFIG_MINIMUM_VERSION = "minimumVersion";
  public static final String CONFIG_CURRENT_VERSION = "currentVersion";

  public static final String ROLE_NAME_KEY = "name";
  public static final String ROLE_ADMINISTRATOR = "administrator";

  public static final String NOTE_PIN_NAME = "note";
  public static final String TALK_PIN_NAME = "talk";
  public static final String PROGRAM_PIN_NAME = "program";


  // NOT PARSE
  public static final String PACKAGE_JW_LIBRARY = "org.jw.jwlibrary.mobile";
  public static final String PACKAGE_JW_LANGUAGE = "org.jw.jwlanguage";
  public static final String LINK_WATCHTOWER_ONLINE_LIBARY = "http://wol.jw.org";
  public static final String LINK_SUBMIT_FEEDBACK_GOOGLE_FORMS = "http://goo.gl/forms/8WLLGdcYUf";


  // SERIALIZER
  public static final String SERIALIZER_FILE_BIBLE_PREFIX = "bible-";
  public static final String SERIALIZER_FILE_BIBLE_EXTENSION = ".bible";
}
