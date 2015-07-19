package com.ameron32.apps.tapnotes.v2.parse;

import android.util.Log;

import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.parse.object.Talk;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klemeilleur on 7/7/2015.
 */
public class Queries {

  private static final String TAG = Queries.class.getSimpleName();
  private static final int FIRST_OR_ONLY = 0;

  private static final int LIMIT_QUERY_MAXIMUM_ALLOWED = 100;
  private static final int LIMIT_SKIP_MAXIMUM_ALLOWED = 10000;



  public static class Live {

    public static final int REQUEST_NOTES_REFRESH = 1;
    public static final int REQUEST_TALKS_REFRESH = 2;
    public static final int REQUEST_PROGRAM_REFRESH = 3;

    // CURRENT LIMIT = 10000
    public static List<Note> pinAllClientOwnedNotesFor(Program program)
        throws ParseException {
      Log.d(TAG, "pinAllClientOwnedNotesFor " + program.getObjectId());
      int currentPage = 0;
      int notesPinned = 0;
      final List<Note> notes = new ArrayList<>();
      do {
        final List<Note> moreNotes = queryClientOwnedNotesFor(program, null, currentPage);
        final int size = moreNotes.size();
        notesPinned = notesPinned + size;
        notes.addAll(moreNotes);
        currentPage++;
        Log.d(TAG, "pinAllClientOwnedNotesFor(loop) | page: " +
            currentPage + " notesPinned: " + notesPinned);
      } while (notesPinned == currentPage * LIMIT_QUERY_MAXIMUM_ALLOWED &&
          notesPinned < LIMIT_SKIP_MAXIMUM_ALLOWED);
      Note.pinAll(notes);
      return notes;
    }

  public static List<Note> pinAllClientOwnedNotesFor(Program program, Talk talk)
          throws ParseException {
      Log.d(TAG, "pinAllClientOwnedNotesFor " + program.getObjectId() + " and " + talk.getObjectId());
      int currentPage = 0;
      int notesPinned = 0;
      final List<Note> notes = new ArrayList<>();
      do {
          final List<Note> moreNotes = queryClientOwnedNotesFor(program, talk, currentPage);
          final int size = moreNotes.size();
          notesPinned = notesPinned + size;
          notes.addAll(moreNotes);
          currentPage++;
          Log.d(TAG, "pinAllClientOwnedNotesFor(loop) | page: " +
                  currentPage + " notesPinned: " + notesPinned);
      } while (notesPinned == currentPage * LIMIT_QUERY_MAXIMUM_ALLOWED &&
              notesPinned < LIMIT_SKIP_MAXIMUM_ALLOWED);
      Note.pinAll(notes);
      return notes;
  }

    private static List<Note> queryClientOwnedNotesFor(Program program, Talk talk, int page)
        throws ParseException {
      ParseQuery<Note> query = ParseQuery.getQuery(Note.class)
                .whereEqualTo(Constants.NOTE_uOWNER_USER_KEY,
                        Commands.Local.getClientUser())
                .setSkip(page * LIMIT_QUERY_MAXIMUM_ALLOWED)
                .setLimit(LIMIT_QUERY_MAXIMUM_ALLOWED);
        if (talk != null) {
            query = query.whereEqualTo(Constants.NOTE_oTALK_OBJECT_KEY, talk);
        }
        final List<Note> notes = query.find();
      return notes;
    }

    // CURRENT LIMIT = 1000
    public static List<Talk> pinAllProgramTalksFor(Program program)
        throws ParseException {
      Log.d(TAG, "pinAllProgramTalksFor " + program.getObjectId());
      final List<Talk> talks = ParseQuery.getQuery(Talk.class)
          .whereEqualTo(Constants.TALK_oPROGRAM_OBJECT_KEY, program)
          .setLimit(LIMIT_QUERY_MAXIMUM_ALLOWED)
          .find();
      Talk.pinAll(talks);
      return talks;
    }

    public static Program pinProgram(String programId)
        throws ParseException {
      Log.d(TAG, "pinProgram " + programId);
      final Program program = ParseQuery.getQuery(Program.class)
          .get(programId);
      program.pin();
      return program;
    }
  }



  public static class Local {

    public static List<Note> findAllClientOwnedNotes()
        throws ParseException {
      Log.d(TAG, "findAllClientOwnedNotes");
      final List<Note> notes = ParseQuery.getQuery(Note.class)
          .fromLocalDatastore()
          .whereEqualTo(Constants.NOTE_uOWNER_USER_KEY,
              Commands.Local.getClientUser())
          .find();
      return notes;
    }

public static int countPrograms()
      throws ParseException {
  Log.d(TAG, "countPrograms");
  final List<Program> programs = ParseQuery.getQuery(Program.class)
          .fromLocalDatastore()
          .find();
  return programs.size();
}

    public static Program getProgram(String programId)
        throws ParseException {
      Log.d(TAG, "getProgram " + programId);
      final Program program = ParseQuery.getQuery(Program.class)
          .fromLocalDatastore()
          .get(programId);
      return program;
    }

    public static Talk getTalk(String talkId)
        throws ParseException {
      Log.d(TAG, "getTalk " + talkId);
      final Talk talk = ParseQuery.getQuery(Talk.class)
          .fromLocalDatastore()
          .get(talkId);
      return talk;
    }

    public static List<Note> findClientOwnedNotesFor(Talk talk)
        throws ParseException {
      Log.d(TAG, "findClientOwnedNotesFor " + talk.getObjectId());
      final List<Note> notes = ParseQuery.getQuery(Note.class)
          .fromLocalDatastore()
          .whereEqualTo(Constants.NOTE_oTALK_OBJECT_KEY,
              talk)
          .whereEqualTo(Constants.NOTE_uOWNER_USER_KEY,
              Commands.Local.getClientUser())
          .find();
      return notes;
    }

    public static Note findLastClientOwnedNoteFor(Talk talk)
        throws ParseException {
      final List<Note> notes = ParseQuery.getQuery(Note.class)
          .fromLocalDatastore()
          .whereEqualTo(Constants.NOTE_oTALK_OBJECT_KEY,
              talk)
          .whereEqualTo(Constants.NOTE_uOWNER_USER_KEY,
              Commands.Local.getClientUser())
          .whereDoesNotExist(Constants.NOTE_NEXTNOTE_OBJECT_KEY)
          .find();
      return notes.get(FIRST_OR_ONLY);
    }

    public static List<Talk> findAllProgramTalks(Program program)
        throws ParseException {
      Log.d(TAG, "findAllProgramTalks " + program.getObjectId());
      final List<Talk> talks = ParseQuery.getQuery(Talk.class)
          .fromLocalDatastore()
          .whereEqualTo(Constants.TALK_oPROGRAM_OBJECT_KEY, program)
          .find();
      return talks;
    }

    public static Note getNote(String noteId)
        throws ParseException {
      Log.d(TAG, "getNote " + noteId);
      final Note note = ParseQuery.getQuery(Note.class)
          .fromLocalDatastore()
          .get(noteId);
      return note;
    }
  }
}
