package com.ameron32.apps.tapnotes.v2.parse.object;

import android.support.annotation.Nullable;
import android.util.Log;

import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.model.INoteEditable;
import com.ameron32.apps.tapnotes.v2.parse.Commands;
import com.ameron32.apps.tapnotes.v2.parse.Constants;
import com.ameron32.apps.tapnotes.v2.parse.Queries;
import com.ameron32.apps.tapnotes.v2.parse.frmk.ColumnableParseObject;
import com.parse.ParseACL;
import com.parse.ParseClassName;
import com.parse.ParseException;
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
      final Program program, final Talk talk,
      final ParseUser owner) {
    final Note note = new Note();
    note.setNoteText(text);
    note.put(NOTE_oPROGRAM_OBJECT_KEY, program);
    note.put(NOTE_oTALK_OBJECT_KEY, talk);
    if (owner != null) {
      note.put(NOTE_uOWNER_USER_KEY, owner);
      note.setACL(getClientNoteACL(owner));
    } else {
      note.setACL(getClientNoteACL(null));
    }
    return note;
  }

  public static ParseACL getClientNoteACL(ParseUser user) {
    final ParseACL acl = new ParseACL();
    acl.setPublicWriteAccess(false);
    if (user != null) {
      acl.setWriteAccess(user, true);
    }
    acl.setRoleWriteAccess(Constants.ROLE_ADMINISTRATOR, true);
    acl.setPublicReadAccess(true);
    return acl;
  }

  @Nullable
  public static Note create(
      final String text,
      final String programId, final String talkId,
      final ParseUser owner) {
    Log.d(Note.class.getSimpleName(),
        "create Note : " + text + "|" + programId + "|" + talkId + "|" + owner.getObjectId());
    try {
      final Program program = Queries.Local.getProgram(programId);
      final Talk talk = Queries.Local.getTalk(talkId);
      return Note.create(text, program, talk, owner);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Note() {
    // required empty
  }

  @Override
  public String getNoteText() {
    return this.getString(NOTE_TEXT_STRING_KEY);
  }

  @Override
  public String getId() {
    return this.getObjectId();
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

  public boolean toggleImportantNote() {
    boolean newState = false;
    if (!isImportantNote()) {
      newState = true;
    }
    setImportantNote(newState);
    return newState;
  }

  public boolean toggleBoldNote() {
    boolean newState = false;
    if (!isBoldNote()) {
      newState = true;
    }
    setBoldNote(newState);
    return newState;
  }



  @Override
  public synchronized void setImportantNote(boolean state) {
    if (state) {
      addTag(INote.TAG_IMPORTANT_NOTE);
    } else {
      removeTag(INote.TAG_IMPORTANT_NOTE);
    }
    Commands.Local.saveEventuallyNote(this);
  }

  @Override
  public synchronized void setBoldNote(boolean state) {
    if (state) {
      addTag(INote.TAG_BOLD_NOTE);
    } else {
      removeTag(INote.TAG_BOLD_NOTE);
    }
    Commands.Local.saveEventuallyNote(this);
  }

  @Override
  public void setNoteText(String noteText) {
    this.put(NOTE_TEXT_STRING_KEY, noteText);
  }

  @Override
  public synchronized void changeNoteType(NoteType newType) {
    if (newType != getNoteType()) {
      setNoteType(newType);
    }
    Commands.Local.saveEventuallyNote(this);
  }

  private List<Integer> getTags() {
    final List<Integer> tags = this.getList(NOTE_TAGS_ARRAY_KEY);
    return (tags == null) ? new ArrayList<>(0) : tags;
  }

  private void addTag(int tag) {
    this.addAllUnique(NOTE_TAGS_ARRAY_KEY,
        Arrays.asList(new int[]{tag}));
  }

  private void removeTag(int tag) {
    this.removeAll(NOTE_TAGS_ARRAY_KEY,
        Arrays.asList(new int[]{tag}));
  }

  private void removeNoteTypes() {
    int size = getTags().size();
    for (int i = 0; i < size; i++) {
      int tag = getTags().get(i);
      if (tag < NoteType.RANGE_MINIMUM_NOTETYPE_TAG) {
        removeTag(tag);
      }
    }
  }

  private void setNoteType(NoteType type) {
    removeNoteTypes();
    addTag(NoteType.getTagInteger(type));
  }

  @Override
  public NoteType getNoteType() {
    int size = getTags().size();
    for (int i = 0; i < size; i++) {
      int tag = getTags().get(i);
      if (tag > NoteType.RANGE_MINIMUM_NOTETYPE_TAG) {
        return NoteType.getTypeFrom(tag);
      }
    }
    return NoteType.STANDARD;
  }

  @Override
  public boolean isNoteOwnedByClient() {
    final ParseACL acl = getACL();
    return acl.getWriteAccess(Commands.Local.getClientUser());
  }

}
