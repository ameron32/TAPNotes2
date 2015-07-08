package com.ameron32.apps.tapnotes.v2.parse.object;

import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.parse.frmk.ColumnableParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;

import static com.ameron32.apps.tapnotes.v2.parse.Constants.*;

/**
 * Created by klemeilleur on 6/29/2015.
 */
@ParseClassName(NOTE_OBJECT_NAME)
public class Note
    extends ColumnableParseObject
    implements INote
{

  public static Note create(final String text,
      final Program program, final Talk talk, final ParseUser owner) {
    final Note note = new Note();
    note.put(NOTE_TEXT_STRING_KEY, text);
    note.put(NOTE_oPROGRAM_OBJECT_KEY, program);
    note.put(NOTE_oTALK_OBJECT_KEY, talk);
    note.put(NOTE_uOWNER_USER_KEY, owner);
    return note;
  }

  public Note() {
    // required empty
  }

  @Override
  public String getNoteText() {
    return this.getString(NOTE_TEXT_STRING_KEY);
  }
}
