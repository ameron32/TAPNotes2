package com.ameron32.apps.tapnotes.v2.parse;

import android.util.Log;

import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by klemeilleur on 7/8/2015.
 */
public class Commands {

  private static final String TAG = Commands.class.getSimpleName();

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

    public static List<Note> saveEventuallyNotes(List<Note> notes) {
      Log.d(TAG, "saveEventuallyNotes: size " + notes.size());
      for (Note note : notes) {
        saveEventuallyNote(note);
      }
      return notes;
    }
  }
}