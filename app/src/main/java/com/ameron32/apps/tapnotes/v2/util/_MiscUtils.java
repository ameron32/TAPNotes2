package com.ameron32.apps.tapnotes.v2.util;

import android.content.Context;
import android.util.Log;

import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.parse.Commands;
import com.ameron32.apps.tapnotes.v2.data.parse.Constants;
import com.ameron32.apps.tapnotes.v2.data.parse.Queries;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Note;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Program;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Talk;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRole;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klemeilleur on 7/7/2015.
 */
public class _MiscUtils {

  private static final String TAG = _MiscUtils.class.getSimpleName();

  /**
   * ONE TIME USE APPLICATION OF PROGRAM OBJECT TO TALKS FOR 2015 CONVENTION
   */
  public static void put2015ConventionProgramObjectInto2015ConventionTalks() {

    ParseQuery.getQuery(Program.class)
        .getInBackground("BPCRNbT6Lf", new GetCallback<Program>() {

          @Override
          public void done(final Program program, ParseException e) {
            if (e != null) {
              return;
            }
            ParseQuery.getQuery(Talk.class)
                .findInBackground(new FindCallback<Talk>() {

                  @Override
                  public void done(List<Talk> list, ParseException e) {
                    if (e != null) {
                      return;
                    }
                    for (int i = 0; i < list.size(); i++) {
                      Talk t = list.get(i);
                      t.put("oProgram", program);
                      t.saveEventually();
                    }
                  }
                });
          }
        });
  }

  public static void _saveFakeNotes(List<Talk> talks, Program program) {

////  List<Note> notes = new ArrayList<>(talks.size());
////  List<Note> notes2 = new ArrayList<>();
//    for (int i = 0; i < talks.size(); i++) {
//      Talk talk = talks.get(i);
//      if (talk.getEventType() == EventType.TALK ||
//          talk.getEventType() == EventType.SYMPOSIUMTALK) {
//        Note last = null;
//        try {
//          last = Queries.Local.findLastClientOwnedNoteFor(talk);
//          Log.d("_MiscUtils", "last = " + last.toString());
//        } catch (ParseException e) {
//          e.printStackTrace();
//        }
//        Note note = Note.create("test3", program, talk, Commands.Local.getClientUser());
//        Commands.Local.saveEventuallyNote(note);
//        if (last != null) {
//          last.setNextNote(note);
//          Commands.Local.saveEventuallyNote(last);
////          notes2.add(last);
//        }
////        notes.add(note);
//      }
//    }
////  Commands.Live.saveNotesNow(notes);
////  Commands.Live.saveNotesNow(notes2);
  }

  public static void _generate1001Notes(Talk talk, Program program)
      throws ParseException {
    for (int i = 0; i < 1001; i++) {
      final Note note = Note.create("spam note " + (i + 1), program, talk, Commands.Local.getClientUser());
      note.save();
    }
  }

  private static final String[][] seq =
      {
          {"a001", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "", ""},
          {"a002", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "", ""},
          {"a003", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "Talk Theme:", "@<<!<41 5 39 <Luke 6:40>!>>"},
          {"a004", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "Talk Theme:", "@<<!<39 5 24 25 26 27 28 29 <Matthew 6:25-30>!>>"},
          {"a005", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "Talk Theme:", "@<<!<40 8 49 <Mark 9:50>!>>"},
          {"a006", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "Talk Theme:", "@<<!<41 5 46 47 48 <Luke 6:47-49>!>>"},
          {"a007", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "", ""},
          {"a008", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "Talk Theme:", "@<<!<41 10 8 9 10 11 12 <Luke 11:9-13>!>>  @<<!<41 21 40 41 42 43 <Luke 22:41-44>!>>"},
          {"a009", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "Talk Theme:", "@<<!<50 1 1 2 3 <Colossians 2:2-4>!>>  @<<!<39 4 16 17 18 19 42 43 44 45 46 47 <Matthew 5:17-20, 43-48>!>>"},
          {"a010", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "", ""},
          {"a011", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "", ""},
          {"a012", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "", ""},
          {"a013", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "Talk Theme:", "@<<!<1 14 0 1 20 <Exodus 15:1, 2, 21>!>>  @<<!<39 25 29 <Matthew 26:30>!>>  @<<!<46 7 11 <2 Corinthinas 8:12>!>>  @<<!<50 2 15 <Colossians 3:16>!>>"},
          {"a014", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "Symposium Theme:", "@<<!<42 13 8 <John 14:9>!>>", "Talk Theme:", "@<<!<42 1 12 13 14 15 16 <John 2:13-17>!>>"},
          {"a015", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "Symposium Theme:", "@<<!<42 13 8 <John 14:9>!>>", "Talk Theme:", "@<<!<42 12 2 3 4 <John 13:3-5>!>>"},
          {"a016", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "Symposium Theme:", "@<<!<42 13 8 <John 14:9>!>>", "Talk Theme:", "@<<!<39 3 7 8 9 10 <Matthew 4:8-11>!>>"},
          {"a017", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "Symposium Theme:", "@<<!<42 13 8 <John 14:9>!>>", "Talk Theme:", "@<<!<42 18 29 <John 19:30>!>>  @<<!<22 54 9 10 <Isaiah 55:10, 11>!>>"},
          {"a018", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "Symposium Theme:", "@<<!<42 13 8 <John 14:9>!>>", "Talk Theme:", "@<<!<39 4 8 <Matthew 5:9>!>>  @<<!<41 23 33 <Luke 24:34>!>>"},
          {"a019", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "Symposium Theme:", "@<<!<42 13 8 <John 14:9>!>>", "Talk Theme:", "@<<!<39 21 14 15 16 17 18 19 20 21 <Matthew 22:15-22>!>>"},
          {"a020", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "", ""},
          {"a021", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "Talk Theme:", "@<<!<40 4 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 <Mark 5:22-43>!>>  @<<!<41 1 41 42 43 44 45 46 <Luke 2:42-47>!>>  @<<!<41 3 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 <Luke 4:1-30>!>>  @<<!<41 7 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 <Luke 8:40-56>!>>"},
          {"a022", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "Talk Theme:", "@<<!<57 4 6 <Hebrews 5:7>!>>  @<<!<39 9 26 27 28 29 30 <Matthew 10:27-31>!>>  @<<!<59 2 13 <1 Peter 3:14>!>>"},
          {"a023", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "Talk Theme:", "@<<!<41 14 3 4 5 6 7 8 9 <Luke 15:4-10>!>>  @<<!<41 18 9 <Luke 19:10>!>>"},
          {"a024", "Daily Theme: \"Learn From Me\"", "@<<!<39 10 28 <Matthew 11:29>!>>", "", "", "", ""},
          {"b001", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "", ""},
          {"b002", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "", ""},
          {"b003", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<41 13 12 13 <Luke 14:13, 14>!>>"},
          {"b004", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<41 6 43 <Luke 7:44>!>>"},
          {"b005", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<39 4 36 <Matthew 5:37>!>>"},
          {"b006", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<42 12 0 <John 13:1>!>>  @<<!<48 3 23 <Ephesians 4:24>!>>"},
          {"b007", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<39 22 22 23 <Matthew 23:23, 24>!>>"},
          {"b008", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<39 17 21 <Matthew 18:22>!>>  @<<!<42 20 14 15 16 <John 21:15-17>!>>"},
          {"b009", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<40 9 12 13 14 <Mark 10:13-15>!>>"},
          {"b010", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<39 10 27 28 29 <Matthew 11:28-30>!>>"},
          {"b011", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "", ""},
          {"b012", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "Symposium Theme:", "@<<!<39 3 22 <Matthew 4:23>!>>", "Talk Theme:", "@<<!<40 0 37 <Mark 1:38>!>>"},
          {"b013", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "Symposium Theme:", "@<<!<39 3 22 <Matthew 4:23>!>>", "Talk Theme:", "@<<!<41 23 31 <Luke 24:32>!>>"},
          {"b014", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "Symposium Theme:", "@<<!<39 3 22 <Matthew 4:23>!>>", "Talk Theme:", "@<<!<19 11 17 <Proverbs 12:18>!>>  @<<!<47 5 0 <Galatians 6:1>!>>"},
          {"b015", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<59 1 20 <1 Peter 2:21>!>>"},
          {"b016", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "", ""},
          {"b017", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "", ""},
          {"b018", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "", ""},
          {"b019", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<19 19 28 <Proverbs 20:29>!>>"},
          {"b020", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<45 6 31 32 33 34 36 <1 Corinthians 7:32-35, 37>!>>"},
          {"b021", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<19 13 0 <Proverbs 14:1>!>>  @<<!<19 26 14 15 <Proverbs 27:15, 16>!>>  @<<!<19 30 16 <Proverbs 31:17>!>>"},
          {"b022", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<45 10 2 <1 Corinthians 11:3>!>>  @<<!<57 12 7 <Hebrews 13:8>!>>"},
          {"b023", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<40 9 13 15 <Mark 10:14, 16>!>>"},
          {"b024", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<58 4 15 <James 5:16>!>>"},
          {"b025", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "", ""},
          {"b026", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<41 21 53 <Luke 22:54>!>>"},
          {"b027", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<41 7 0 <Luke 8:1>!>>  @<<!<41 8 0 <Luke 9:1>!>>  @<<!<41 9 0 <Luke 10:1>!>>"},
          {"b028", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<42 17 36 <John 18:37>!>>  @<<!<39 20 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 <Matthew 21:23-46>!>>  @<<!<39 21 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 <Matthew 22:15-46>!>>"},
          {"b029", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "Talk Theme:", "@<<!<39 3 7 8 9 <Matthew 4:8-10>!>>  @<<!<39 7 0 1 2 <Matthew 8:1-3>!>>  @<<!<42 1 12 13 14 15 16 <John 2:13-17>!>>  @<<!<59 3 0 1 <1 Peter 4:1, 2>!>>"},
          {"b030", "Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"", "@<<!<44 14 4 <Romans 15:5>!>>", "", "", "", ""},
          {"c001", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "", "", "", ""},
          {"c002", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "", "", "", ""},
          {"c003", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "Symposium Theme:", "@<<!<42 12 33 34 <John 13:34, 35>!>>", "Talk Theme:", "@<<!<45 12 3 <1 Corinthians 13:4>!>>"},
          {"c004", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "Symposium Theme:", "@<<!<42 12 33 34 <John 13:34, 35>!>>", "Talk Theme:", "@<<!<45 12 3 <1 Corinthians 13:4>!>>"},
          {"c005", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "Symposium Theme:", "@<<!<42 12 33 34 <John 13:34, 35>!>>", "Talk Theme:", "@<<!<45 12 3 4 <1 Corinthians 13:4, 5>!>>"},
          {"c006", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "Symposium Theme:", "@<<!<42 12 33 34 <John 13:34, 35>!>>", "Talk Theme:", "@<<!<45 12 4 <1 Corinthians 13:5>!>>"},
          {"c007", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "Symposium Theme:", "@<<!<42 12 33 34 <John 13:34, 35>!>>", "Talk Theme:", "@<<!<45 12 4 <1 Corinthians 13:5>!>>"},
          {"c008", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "Symposium Theme:", "@<<!<42 12 33 34 <John 13:34, 35>!>>", "Talk Theme:", "@<<!<45 12 5 <1 Corinthians 13:6>!>>"},
          {"c009", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "Symposium Theme:", "@<<!<42 12 33 34 <John 13:34, 35>!>>", "Talk Theme:", "@<<!<45 12 6 <1 Corinthians 13:7>!>>"},
          {"c010", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "Symposium Theme:", "@<<!<42 12 33 34 <John 13:34, 35>!>>", "Talk Theme:", "@<<!<45 12 7 <1 Corinthians 13:8>!>>"},
          {"c011", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "", "", "", ""},
          {"c012", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "", "", "Talk Theme:", "@<<!<42 15 32 <John 16:33>!>>  @<<!<65 5 1 <Revelation 6:2>!>>  @<<!<65 16 11 12 13 <Revelation 17:12-14>!>>"},
          {"c013", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "", "", "", ""},
          {"c014", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "", "", "", ""},
          {"c015", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "", "", "", ""},
          {"c016", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "", "", "", ""},
          {"c017", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "", "", "Talk Theme:", "@<<!<2 24 9 10 11 <Leviticus 25:10-12>!>>  @<<!<43 2 20 <Acts 3:21>!>>"},
          {"c018", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "", "", "Talk Theme:", "@<<!<39 15 12 13 14 15 16 17 18 19 <Matthew 16:13-20>!>>  @<<!<42 8 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 <John 9:1-41>!>>  @<<!<42 10 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 <John 11:1-44>!>>  @<<!<43 0 0 1 2 3 4 5 6 7 8 9 10 <Acts 1:1-11>!>>  @<<!<43 1 30 <Acts 2:31>!>>"},
          {"c019", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "", "", "", ""},
          {"c020", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "", "", "Talk Theme:", "@<<!<39 13 21 22 23 24 25 26 27 28 29 30 31 32 33 <Matthew 14:22-34>!>>  @<<!<57 11 1 <Hebrews 12:2>!>>"},
          {"c021", "Daily Theme: \"Keep Following Me\"", "@<<!<39 15 3 <Matthew 16:4>!>>", "", "", "", ""}
      };

  public static void _saveProgramScriptures(final Context context, Program program) {
    try {
      final List<ITalk> talks = Queries.Local.findAllProgramTalks(program);
      if (talks.size() != seq.length) {
        Log.d(TAG, "mismatch size: " + talks.size() + " / " + seq.length);
        return;
      }

      int i = 0;
      for (ITalk iTalk : talks) {
          if (iTalk instanceof Talk) {
              Talk talk = (Talk) iTalk;
              Log.d(TAG, "talk: " + talk.getTalkTitle());
              final String talkSeq = talk.getString(Constants.TALK_SEQUENCE_STRING_KEY);
              for (String[] row : seq) {
                  final String rowSeq = row[0]; // sequence
                  if (talkSeq.equalsIgnoreCase(rowSeq)) {
                      // they match
                      // add notes to talk
                      final String t1 = row[1];
                      final String s1 = row[2];
                      if (s1.length() > 1) {
                          // generate the note
                          Log.d(TAG, "note to create: " + rowSeq + " " + t1 + " " + s1);
                          Note.create(t1 + "\n" + s1, program, talk, null).save();
                      } else {
                          // don't do it
                      }

                      final String t2 = row[3];
                      final String s2 = row[4];
                      if (s2.length() > 1) {
                          // generate the note
                          Log.d(TAG, "note to create: " + rowSeq + " " + t2 + " " + s2);
                          Note.create(t2 + "\n" + s2, program, talk, null).save();
                      } else {
                          // don't do it
                      }

                      final String t3 = row[5];
                      final String s3 = row[6];
                      if (s3.length() > 1) {
                          // generate the note
                          Log.d(TAG, "note to create: " + rowSeq + " " + t3 + " " + s3);
                          Note.create(t3 + "\n" + s3, program, talk, null).save();
                      } else {
                          // don't do it
                      }
                  }
              }
              talk.save();
              i++;
          }
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public static void _saveSongNotes(String programId) {

    try {

      final IProgram program = Queries.Local.getProgram(programId);
      ITalk talk;

//      talk = getTalk(120);
//      Note.create("1. If we have listened to Christ, will we show it?", program, talk, null).save();
//      Note.create("His teaching shines as it shows us the way.", program, talk, null).save();
//      Note.create("It makes us happy to hear and to know it,", program, talk, null).save();
//      Note.create("But we'll be blessed if we know and obey.", program, talk, null).save();
//      Note.create("(CHORUS)", program, talk, null).save();
//      Note.create("Listen, obey, and be blessed", program, talk, null).save();
//      Note.create("When you hear God's will expressed.", program, talk, null).save();
//      Note.create("If you'd be happy and enter his rest,", program, talk, null).save();
//      Note.create("Listen, obey, and be blessed.", program, talk, null).save();
//      Note.create("2. Our way of life, like a house, gives protection", program, talk, null).save();
//      Note.create("When it is built on the rock, not on sand.", program, talk, null).save();
//      Note.create("If we apply Jesus' loving direction,", program, talk, null).save();
//      Note.create("We'll build a life which on bedrock will stand.", program, talk, null).save();
//      Note.create("(CHORUS)", program, talk, null).save();
//      Note.create("Listen, obey, and be blessed", program, talk, null).save();
//      Note.create("When you hear God's will expressed.", program, talk, null).save();
//      Note.create("If you'd be happy and enter his rest,", program, talk, null).save();
//      Note.create("Listen, obey, and be blessed.", program, talk, null).save();
//      Note.create("3. Just as a tree rooted deep by the waters", program, talk, null).save();
//      Note.create("Gives of its fruit when each season arrives,", program, talk, null).save();
//      Note.create("If we obey as God's own sons and daughters,", program, talk, null).save();
//      Note.create("We'll all be blessed and enjoy endless lives.", program, talk, null).save();
//      Note.create("(CHORUS)", program, talk, null).save();
//      Note.create("Listen, obey, and be blessed", program, talk, null).save();
//      Note.create("When you hear God's will expressed.", program, talk, null).save();
//      Note.create("If you'd be happy and enter his rest,", program, talk, null).save();
//      Note.create("Listen, obey, and be blessed.", program, talk, null).save();

//      ParseACL roleAdminACL = new ParseACL();
//      roleAdminACL.setPublicReadAccess(true);
//      roleAdminACL.setPublicWriteAccess(true);
//      ParseRole roleAdmin = new ParseRole("administrator", roleAdminACL);
//      roleAdmin.save();
//
//      final List<ParseRole> parseRoles = ParseRole.getQuery().whereEqualTo("name", "administrator").find();
//      ParseRole roleAdmin = parseRoles.get(0);
//      roleAdmin.getUsers().add(Commands.Local.getClientUser());
//      roleAdmin.save();

//      _loop(roleAdmin);

    } catch (ParseException e) {
      e.printStackTrace();
//    } catch (InterruptedException e) {
//      e.printStackTrace();
    }
  }

  private static void _loop(ParseRole roleAdmin) throws ParseException, InterruptedException {
    final List<Note> notes = ParseQuery.getQuery(Note.class)
        .whereDoesNotExist(Constants.NOTE_uOWNER_USER_KEY)
        .whereLessThan(Constants.NOTE_UPDATEDAT_DATE_KEY, DateTime.parse("20150727", DateTimeFormat.forPattern("yyyyMMdd")).toDate())
        .find();
    ParseACL acl = new ParseACL();
    acl.setPublicReadAccess(true);
    acl.setPublicWriteAccess(false);
    acl.setRoleWriteAccess(roleAdmin, true);
    int i = 0;
    String text;
    for (Note note : notes) {
      text = note.getString(Constants.NOTE_TEXT_STRING_KEY);
      Log.d("_updateNote", note.getId() + ": " + ((text.length() > 14) ? text.substring(0, 14) : text));
      note.setACL(acl);
      note.save();
      if (i % 100 == 0) {
        Thread.sleep(1000);
      } else if (i % 30 == 0) {
        Thread.sleep(100);
      }

      i++;
    }
  }

  private static Talk getTalk(int number) throws ParseException {
    return ParseQuery.getQuery(Talk.class)
        .whereEqualTo(Constants.TALK_METADATA_STRING_KEY, "#" + number)
        .getFirst();
  }

//  public static void _modifySymposiumTalkTitles(String programId) {
//    try {
//      final IProgram program = Queries.Local.getProgram(programId);
//      final List<ITalk> talks = Queries.Local.findAllProgramTalks(program);
//      final List<ITalk> toSave = new ArrayList<>();
//      for (ITalk talk : talks) {
//        final String symposiumTitle = talk.getSymposiumTitle();
//        if (symposiumTitle.contains("Symposium:")) {
//          talk.setSymposiumTitle(symposiumTitle.replace(": ", ": \n"));
//          toSave.add(talk);
//        }
//      }
//      Talk.saveAll(toSave);
//    } catch (ParseException e) {
//      e.printStackTrace();
//    }
//  }

}
