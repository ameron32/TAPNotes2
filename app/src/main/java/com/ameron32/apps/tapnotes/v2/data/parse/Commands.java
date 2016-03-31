package com.ameron32.apps.tapnotes.v2.data.parse;

import android.util.Log;

import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Note;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by klemeilleur on 7/8/2015.
 */
public class Commands {

  private static final String TAG = Commands.class.getSimpleName();

  public static class Live {

    static Note saveNoteNow(Note note) {
      Log.d(TAG, "saveNoteNow " + note.getObjectId());
      try {
        note.save();
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return note;
    }

    static List<Note> saveNotesNow(List<Note> notes) {
      Log.d(TAG, "saveNoteNow: size " + notes.size());
      for (Note note : notes) {
        saveNoteNow(note);
      }
      return notes;
    }
  }

  public static class Local {

    static ParseUser getClientUser() {
      Log.d(TAG, "getClientUser");
      return ParseUser.getCurrentUser();
    }

    static Note saveEventuallyNote(Note note) {
      saveEventuallyNote(note, null);
      return note;
    }

    static Note saveEventuallyNote(Note note, SaveCallback callback) {
      Log.d(TAG, "saveEventuallyNote " + note.getObjectId());
      if (!note.isNoteOwnedByClient()) {
        // do not delete notes that are not owned by the client
        Log.d(TAG, "note was not owned by client. not saving.");
        return note;
      }
      note.saveEventually(callback);
      return note;
    }

    static List<Note> saveEventuallyNotes(List<Note> notes) {
      Log.d(TAG, "saveEventuallyNotes: size " + notes.size());
      for (Note note : notes) {
        saveEventuallyNote(note);
      }
      return notes;
    }

    static List<Note> saveEventuallyParseNotes(List<Note> notes) {
      Log.d(TAG, "saveEventuallyNotes: size " + notes.size());
      for (INote note : notes) {
        if (note instanceof Note) {
          saveEventuallyNote((Note) note);
        }
      }
      return notes;
    }

    static Note pinNote(Note note) {
      Log.d(TAG, "pinNote: " + note.getObjectId());
      note.pinInBackground();
      return note;
    }

    static void deleteEventuallyNote(Note note) {
      Log.d(TAG, "deleteEventuallyNote " + note.getObjectId());
      if (!note.isNoteOwnedByClient()) {
        // do not delete notes that are not owned by the client
        Log.d(TAG, "note was not owned by client. not deleting.");
        return;
      }
      note.deleteEventually();
    }

    static void deleteEventuallyNotes(List<Note> notes) {
      Log.d(TAG, "deleteEventuallyNotes: size " + notes.size());
      for (Note note : notes) {
        deleteEventuallyNote(note);
      }
    }

    static void logoutClientUser() {
      ParseUser.logOut();
    }
  }
}
