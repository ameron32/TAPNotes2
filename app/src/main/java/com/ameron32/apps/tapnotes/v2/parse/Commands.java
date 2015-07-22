package com.ameron32.apps.tapnotes.v2.parse;

import android.util.Log;

import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.scripture.Bible;
import com.ameron32.apps.tapnotes.v2.scripture.BibleBookChooser;
import com.ameron32.apps.tapnotes.v2.scripture.Scripture;
import com.ameron32.apps.tapnotes.v2.util.LocaleUtil;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by klemeilleur on 7/8/2015.
 */
public class Commands {

  private static final String TAG = Commands.class.getSimpleName();

  public static class Live {

    public static Note saveNoteNow(Note note) {
      Log.d(TAG, "saveNoteNow " + note.getObjectId());
      try {
        note.save();
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return note;
    }

    public static List<Note> saveNotesNow(List<Note> notes) {
      Log.d(TAG, "saveNoteNow: size " + notes.size());
      for (Note note : notes) {
        saveNoteNow(note);
      }
      return notes;
    }
  }

  public static class Local {

    public static ParseUser getClientUser() {
      Log.d(TAG, "getClientUser");
      return ParseUser.getCurrentUser();
    }

    public static Note saveEventuallyNote(Note note) {
      Log.d(TAG, "saveEventuallyNote " + note.getObjectId());
      note.saveEventually();
      return note;
    }

    public static Note saveEventuallyNote(Note note, SaveCallback callback) {
      Log.d(TAG, "saveEventuallyNote " + note.getObjectId());
      note.saveEventually(callback);
      return note;
    }

    public static List<Note> saveEventuallyNotes(List<Note> notes) {
      Log.d(TAG, "saveEventuallyNotes: size " + notes.size());
      for (Note note : notes) {
        saveEventuallyNote(note);
      }
      return notes;
    }

    public static List<INote> saveEventuallyParseNotes(List<INote> notes) {
      Log.d(TAG, "saveEventuallyNotes: size " + notes.size());
      for (INote note : notes) {
        if (note instanceof Note) {
          saveEventuallyNote((Note) note);
        }
      }
      return notes;
    }

    public static void deleteEventuallyNote(Note note) {
      Log.d(TAG, "deleteEventuallyNote " + note.getObjectId());
      if (!note.isNoteOwnedByClient()) {
        // do not delete notes that are not owned by the client
        Log.d(TAG, "note was not owned by client. not deleting.");
        return;
      }
      note.deleteEventually();
    }

    public static void deleteEventuallyNotes(List<Note> notes) {
      Log.d(TAG, "deleteEventuallyNotes: size " + notes.size());
      for (Note note : notes) {
        deleteEventuallyNote(note);
      }
    }
  }
}
