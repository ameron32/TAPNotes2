package com.ameron32.apps.tapnotes.v2.data.parse.model;

import android.support.annotation.Nullable;
import android.util.Log;

import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.INoteEditable;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.parse.Constants;
import com.ameron32.apps.tapnotes.v2.data.parse.ParseHelper;
import com.ameron32.apps.tapnotes.v2.data.parse.frmk.ColumnableParseObject;
import com.parse.ParseACL;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.ameron32.apps.tapnotes.v2.data.parse.Constants.*;

/**
 * Created by klemeilleur on 6/29/2015.
 */
@ParseClassName(NOTE_OBJECT_NAME)
public class Note
    extends ColumnableParseObject
    implements INoteEditable
{

//  public static Note create(final String text,
//      final Program program, final Talk talk,
//      final ParseUser owner) {
//    final Note note = new Note();
//    note.setNoteText(text);
//    note.put(NOTE_oPROGRAM_OBJECT_KEY, program);
//    note.put(NOTE_oTALK_OBJECT_KEY, talk);
//    if (owner != null) {
//      note.put(NOTE_uOWNER_USER_KEY, owner);
//      note.setACL(getClientNoteACL(owner));
//    } else {
//      note.setACL(getClientNoteACL(null));
//    }
//    return note;
//  }

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

//  @Nullable
//  public static Note create(
//      final String text,
//      final String programId, final String talkId,
//      final ParseUser owner) {
//    Log.d(Note.class.getSimpleName(),
//        "create Note : " + text + "|" + programId + "|" + talkId + "|" + owner.getObjectId());
//    try {
//      final IProgram program = ParseHelper.Queries.Local.getProgram(programId);
//      final ITalk talk = ParseHelper.Queries.Local.getTalk(talkId);
//      if (program instanceof Program && talk instanceof Talk) {
//        return Note.create(text, (Program) program, (Talk) talk, owner);
//      }
//    } catch (ParseException e) {
//      e.printStackTrace();
//    }
//    return null;
//  }

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
//    ParseHelper.Commands.Local.saveEventuallyNote(this);
  }

  @Override
  public synchronized void setBoldNote(boolean state) {
    if (state) {
      addTag(INote.TAG_BOLD_NOTE);
    } else {
      removeTag(INote.TAG_BOLD_NOTE);
    }
//    ParseHelper.Commands.Local.saveEventuallyNote(this);
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
//    ParseHelper.Commands.Local.saveEventuallyNote(this);
  }

  private List<Integer> getTags() {
    final List<String> tags = this.getList(NOTE_TAGS_ARRAY_KEY);
    if (tags == null) {
      return new ArrayList<>(0);
    }

    final List<Integer> tagsI = new ArrayList<>(tags.size());
    for (String tag : tags) {
      tagsI.add(Integer.valueOf(tag));
    }
    return tagsI;
  }

  private void addTag(int tag) {
    this.addAllUnique(NOTE_TAGS_ARRAY_KEY,
        Arrays.asList(new String[]{ Integer.toString(tag) }));
  }

  private void removeTag(int tag) {
    this.removeAll(NOTE_TAGS_ARRAY_KEY,
        Arrays.asList(new String[]{ Integer.toString(tag) }));
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

//  @Override
//  public boolean isNoteOwnedByClient() {
//    final ParseUser owner = this.getOwner();
//    if (owner == null) {
//      return false;
//    }
//
//    return (ParseHelper.Commands.Local.getClientUser().getObjectId() == owner.getObjectId());
//  }
//
//  private ParseUser getOwner() {
//    Object o = this.get(NOTE_uOWNER_USER_KEY);
//    if (o != null) {
//      ParseUser user = (ParseUser) o;
//      return user;
//    }
//    return null;
//  }
//
//  @Override
//  public boolean isNoteEditableByClient() {
//    final ParseACL acl = getACL();
//    return acl.getWriteAccess(ParseHelper.Commands.Local.getClientUser());
//  }

  @Override
  public void setUserTimestamp(Date date) {
    // TODO ignore userTimestamp for Parse
  }
}
