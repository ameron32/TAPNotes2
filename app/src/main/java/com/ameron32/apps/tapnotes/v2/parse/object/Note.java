package com.ameron32.apps.tapnotes.v2.parse.object;

import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.model.INoteEditable;
import com.ameron32.apps.tapnotes.v2.parse.Commands;
import com.ameron32.apps.tapnotes.v2.parse.frmk.ColumnableParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ameron32.apps.tapnotes.v2.parse.Constants.*;

/**
 * Created by klemeilleur on 6/29/2015.
 */
@ParseClassName(NOTE_OBJECT_NAME)
public class Note
    extends ColumnableParseObject
    implements INoteEditable
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

  @Override
  public String getNextNoteId() {
    return (getNextNote() == null) ? null : getNextNote().getObjectId();
  }

  @Override
  public boolean isImportantNote() {
    if (getTags().contains(INote.TAG_IMPORTANT_NOTE)) {
      return true;
    }
    return false;
  }

  @Override
  public boolean isBoldNote() {
    if (getTags().contains(INote.TAG_BOLD_NOTE)) {
      return true;
    }
    return false;
  }



  @Override
  public void setImportantNote(boolean state) {
    if (state) {
      addTag(INote.TAG_IMPORTANT_NOTE);
    } else {
      removeTag(INote.TAG_IMPORTANT_NOTE);
    }
    Commands.Local.saveEventuallyNote(this);
  }

  @Override
  public void setBoldNote(boolean state) {
    if (state) {
      addTag(INote.TAG_BOLD_NOTE);
    } else {
      removeTag(INote.TAG_BOLD_NOTE);
    }
    Commands.Local.saveEventuallyNote(this);
  }



  private Note getNextNote() {
    return (Note) this.get(NOTE_NEXTNOTE_OBJECT_KEY);
  }

  private List<Integer> getTags() {
    final List<Integer> tags = this.getList(NOTE_TAGS_ARRAY_KEY);
    return (tags == null) ? new ArrayList<>(0) : tags;
  }

  private void addTag(int tag) {
    this.addAllUnique(NOTE_TAGS_ARRAY_KEY,
        Arrays.asList(new int[] {tag}));
  }

  private void removeTag(int tag) {
    this.removeAll(NOTE_TAGS_ARRAY_KEY,
        Arrays.asList(new int[]{tag}));
  }
}
