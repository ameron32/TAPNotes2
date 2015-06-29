package com.ameron32.apps.tapnotes.v2.parse.object;

import com.ameron32.apps.tapnotes.v2.model.INote;
import com.parse.ParseClassName;

import static com.ameron32.apps.tapnotes.v2.parse.object.ParseConstants.*;

/**
 * Created by klemeilleur on 6/29/2015.
 */
@ParseClassName(NOTE_OBJECT_NAME)
public class Note
    extends ColumnableParseObject
    implements INote
{

  public static Note create(final String text) {
    final Note n = new Note();
    n.put(NOTE_TEXT_KEY, text);
    return n;
  }

  public Note() {
    // required empty
  }

  @Override
  public String getNoteText() {
    return this.getString(NOTE_TEXT_KEY);
  }
}
