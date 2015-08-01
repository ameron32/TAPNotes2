package com.ameron32.apps.tapnotes.v2.util;

import android.content.Context;
import android.util.Log;

import com.ameron32.apps.tapnotes.v2.parse.Commands;
import com.ameron32.apps.tapnotes.v2.parse.Constants;
import com.ameron32.apps.tapnotes.v2.parse.Queries;
import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.parse.object.Talk;
import com.ameron32.apps.tapnotes.v2.ui.mc_sanitizer.Sanitizer;
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
      {   {"a001","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:",""},
          {"a002","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:",""},
          {"a003","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:","(Luke 6:40)"},
          {"a004","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:","(Matthew 6:25-30)"},
          {"a005","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:","(Mark 9:50)"},
          {"a006","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:","(Luke 6:47-49)"},
          {"a007","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:",""},
          {"a008","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:","(Luke 11:9-13)(Luke 22:41-44)"},
          {"a009","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:","(Colossians 2:2-4)(Matthew 5:17-20,43-48)"},
          {"a010","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:",""},
          {"a011","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:",""},
          {"a012","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:",""},
          {"a013","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:","(Exodus 15:1,2,21)(Matthew 26:30)(2 Corinthians 8:12)(Colossians 3:16)"},
          {"a014","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","(John 14:9)","Talk Theme:","(John 2:13-17)"},
          {"a015","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","(John 14:9)","Talk Theme:","(John 13:3-5)"},
          {"a016","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","(John 14:9)","Talk Theme:","(Matthew 4:8-11)"},
          {"a017","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","(John 14:9)","Talk Theme:","(John 19:30)(Isaiah 55:10,11)"},
          {"a018","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","(John 14:9)","Talk Theme:","(Matthew 5:9)(Luke 24:34)"},
          {"a019","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","(John 14:9)","Talk Theme:","(Matthew 22:15-22)"},
          {"a020","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:",""},
          {"a021","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:","(Mark 5:22-43)(Luke 2:7-14,42-47)(Luke 4:1-30)(Luke 8:40-56)"},
          {"a022","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:","(Hebrews 5:7)(Matthew 10:27-31)(1 Peter 3:14)"},
          {"a023","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:","(Luke 15:4-10)(Luke 19:10)"},
          {"a024","Daily Theme: \"Learn From Me\"","(Matthew 11:29)","Symposium Theme:","","Talk Theme:",""},
          {"b001","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:",""},
          {"b002","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:",""},
          {"b003","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(Luke 14:13,14)"},
          {"b004","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(Luke 7:44)"},
          {"b005","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(Matthew 5:37)"},
          {"b006","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(John 13:1)(Ephesians 4:24)"},
          {"b007","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(Matthew 23:23,24)"},
          {"b008","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(Matthew 18:22)(John 21:15-17)"},
          {"b009","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(Mark 10:13-15)"},
          {"b010","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(Matthew 11:28-30)"},
          {"b011","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:",""},
          {"b012","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","(Matthew 4:23)","Talk Theme:","(Mark 1:38)"},
          {"b013","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","(Matthew 4:23)","Talk Theme:","(Luke 24:32)"},
          {"b014","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","(Matthew 4:23)","Talk Theme:","(Proverbs 12:18)(Galatians 6:1)"},
          {"b015","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(1 Peter 2:21)"},
          {"b016","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:",""},
          {"b017","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:",""},
          {"b018","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:",""},
          {"b019","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(Proverbs 20:29)"},
          {"b020","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(1 Corinthians 7:32-35,37)"},
          {"b021","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(Proverbs 14:1)(Proverbs 27:15,16)(Proverbs 31:17)"},
          {"b022","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(1 Corinthians 11:3)(Hebrews 13:8)"},
          {"b023","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(Mark 10:14,16)"},
          {"b024","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(James 5:16)"},
          {"b025","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:",""},
          {"b026","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(Luke 22:54)"},
          {"b027","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(Luke 8:1)(Luke 9:1)(Luke 10:1)"},
          {"b028","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(John 18:37)(Matthew 21:23-46)(Matthew 22:15-46)"},
          {"b029","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:","(Matthew 4:8-10)(Matthew 8:1-3)(John 2:13-17)(1 Peter 4:1,2)"},
          {"b030","Daily Theme: \"Have ... the Same Mental Attitude That Christ Jesus Had\"","(Romans 15:5)","Symposium Theme:","","Talk Theme:",""},
          {"c001","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","","Talk Theme:",""},
          {"c002","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","","Talk Theme:",""},
          {"c003","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","(John 13:34,35)","Talk Theme:","(1 Corinthians 13:4)"},
          {"c004","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","(John 13:34,35)","Talk Theme:","(1 Corinthians 13:4)"},
          {"c005","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","(John 13:34,35)","Talk Theme:","(1 Corinthians 13:4,5)"},
          {"c006","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","(John 13:34,35)","Talk Theme:","(1 Corinthians 13:5)"},
          {"c007","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","(John 13:34,35)","Talk Theme:","(1 Corinthians 13:5)"},
          {"c008","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","(John 13:34,35)","Talk Theme:","(1 Corinthians 13:6)"},
          {"c009","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","(John 13:34,35)","Talk Theme:","(1 Corinthians 13:7)"},
          {"c010","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","(John 13:34,35)","Talk Theme:","(1 Corinthians 13:8)"},
          {"c011","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","","Talk Theme:",""},
          {"c012","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","","Talk Theme:","(John 16:33)(Revelation 6:2)(Revelation 17:12-14)"},
          {"c013","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","","Talk Theme:",""},
          {"c014","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","","Talk Theme:",""},
          {"c015","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","","Talk Theme:",""},
          {"c016","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","","Talk Theme:",""},
          {"c017","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","","Talk Theme:","(Leviticus 25:10-12)(Acts 3:21)"},
          {"c018","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","","Talk Theme:","(Matthew 16:13-20)(John 9:1-41)(John 11:1-44)(Acts 1:1-11)(Acts 2:31)"},
          {"c019","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","","Talk Theme:",""},
          {"c020","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","","Talk Theme:","(Matthew 14:22-34)(Hebrews 12:2)"},
          {"c021","Daily Theme: \"Keep Following Me\"","(Matthew 16:24)","Symposium Theme:","","Talk Theme:",""}
      };

  public static void _saveProgramScriptures(final Context context, Program program) {
    try {
      final List<Talk> talks = Queries.Local.findAllProgramTalks(program);
      if (talks.size() != seq.length) {
        Log.d(TAG, "mismatch size: " + talks.size() + " / " + seq.length);
        return;
      }

      int i = 0;
      for (Talk talk : talks) {
        Log.d(TAG, "talk: " + talk.getTalkTitle());
        final String talkSeq = talk.getString("sequence");
        for (String[] row : seq) {
          final String rowSeq = row[0]; // sequence
          if (talkSeq == rowSeq) {
            // they match
            // add notes to talk
            final String t1 = row[1];
            final String s1 = row[2];
            if (s1.length() > 1) {
              // TODO generate the note

            } else {
              // don't do it
            }

            final String t2 = row[3];
            final String s2 = row[4];
            if (s2.length() > 1) {
              // TODO generate the note

            } else {
              // don't do it
            }

            final String t3 = row[5];
            final String s4 = row[6];
            if (s2.length() > 1) {
              // TODO generate the note

            } else {
              // don't do it
            }

          }
        }
        talk.save();
        i++;
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public static void _saveSongNotes(String programId) {

    try {

      final Program program = Queries.Local.getProgram(programId);
      Talk talk;

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

  public static void _modifySymposiumTalkTitles(String programId) {
    try {
      final Program program = Queries.Local.getProgram(programId);
      final List<Talk> talks = Queries.Local.findAllProgramTalks(program);
      final List<Talk> toSave = new ArrayList<>();
      for (Talk talk : talks) {
        final String symposiumTitle = talk.getSymposiumTitle();
        if (symposiumTitle.contains("Symposium:")) {
          talk.setSymposiumTitle(symposiumTitle.replace(": ", ": \n"));
          toSave.add(talk);
        }
      }
      Talk.saveAll(toSave);
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

}
