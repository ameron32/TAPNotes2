package com.ameron32.apps.tapnotes.v2.parse;

import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.parse.object.Talk;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by klemeilleur on 7/7/2015.
 */
public class Queries {

  public static class Live {

    public static List<Note> pinAllClientOwnedNotesFor(Program program)
        throws ParseException {
      final List<Note> notes = ParseQuery.getQuery(Note.class)
          .whereEqualTo(Constants.NOTE_uOWNER_USER_KEY, ParseUser.getCurrentUser())
          .find();
      Note.pinAll(notes);
      return notes;
    }

    public static List<Talk> pinAllProgramTalksFor(Program program)
        throws ParseException {
      final List<Talk> talks = ParseQuery.getQuery(Talk.class)
          .whereEqualTo(Constants.TALK_oPROGRAM_OBJECT_KEY, program)
          .find();
      Talk.pinAll(talks);
      return talks;
    }

    public static Program pinProgram(String programId)
        throws ParseException {
      final Program program = ParseQuery.getQuery(Program.class)
          .get(programId);
      program.pin();
      return program;
    }
  }

  public static class Local {

    public static List<Note> findAllClientOwnedNotes()
        throws ParseException {
      final List<Note> notes = ParseQuery.getQuery(Note.class)
          .fromLocalDatastore()
          .whereEqualTo(Constants.NOTE_uOWNER_USER_KEY, ParseUser.getCurrentUser())
          .find();
      return notes;
    }

    public static Program getProgram(String programId)
        throws ParseException {
      final Program program = ParseQuery.getQuery(Program.class)
          .fromLocalDatastore()
          .get(programId);
      return program;
    }

    public static Talk getTalk(String talkId)
        throws ParseException {
      final Talk talk = ParseQuery.getQuery(Talk.class)
          .fromLocalDatastore()
          .get(talkId);
      return talk;
    }

    public static List<Note> findClientOwnedNotesFor(Program program, Talk talk)
        throws ParseException {
      final List<Note> notes = ParseQuery.getQuery(Note.class)
          .fromLocalDatastore()
          .whereEqualTo(Constants.NOTE_uOWNER_USER_KEY,
              ParseUser.getCurrentUser())
          .find();
      return notes;
    }

    public static List<Talk> findAllProgramTalks(Program program)
        throws ParseException {
      final List<Talk> talks = ParseQuery.getQuery(Talk.class)
          .fromLocalDatastore()
          .whereEqualTo(Constants.TALK_oPROGRAM_OBJECT_KEY, program)
          .find();
      return talks;
    }
  }
}
