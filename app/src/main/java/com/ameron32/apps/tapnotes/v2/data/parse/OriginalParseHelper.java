package com.ameron32.apps.tapnotes.v2.data.parse;

import android.util.Log;

import com.ameron32.apps.tapnotes.v2.data.frmk.Helper;
import com.ameron32.apps.tapnotes.v2.data.frmk.LocalHelper;
import com.ameron32.apps.tapnotes.v2.data.frmk.RemoteHelper;
import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.model.IUser;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Note;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Program;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Talk;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static com.ameron32.apps.tapnotes.v2.data.frmk.Helper.*;

/**
 * Created by klemeilleur on 3/25/2016.
 */
public class OriginalParseHelper {

  private static final String NOTE_TAG = "note";
  private static final String TALK_TAG = "talk";
  private static final String PROGRAM_TAG = "program";

//  public final
  ParseLocalHelper cache;
//  public final
  ParseRemoteHelper remote;
  public final ParseSyncEvent syncEvent;

  public OriginalParseHelper() {
//    cache = new ParseLocalHelper();
//    remote = new ParseRemoteHelper();
    syncEvent = new ParseSyncEvent();
  }

  public LocalHelper getCache() {
    return cache;
  }

  public RemoteHelper getRemote() {
    return remote;
  }

  public ParseSyncEvent getSyncEvent() {
    return syncEvent;
  }

  // ------------------------------------------
  // LOCAL

//    @Override
// public Observable<List<INote>> clearNotes(final IProgram program, final ITalk talk, final DateTime date, final IUser user) {
//        return Observable.create(new Observable.OnSubscribe<List<INote>>() {
//            @Override
//            public void call(Subscriber<? super List<INote>> subscriber) {
//                if (subscriber.isUnsubscribed()) return;
//                List<INote> iNotes = unpinNotesAsync(program, talk, date, user);
//                subscriber.onNext(iNotes);
//                subscriber.onCompleted();
//            }
//        });
//    }
//
//    @Override
// public Observable<List<INote>> setNotes(Collection<INote> newNotes) {
//        // TODO method
//        return Observable.create(new Observable.OnSubscribe<List<INote>>() {
//            @Override
//            public void call(Subscriber<? super List<INote>> subscriber) {
//
//            }
//        });
//    }
//
//    @Override
// public Observable<List<INote>> getLocalNotes(final IProgram program, final ITalk talk, final DateTime date, final IUser user) {
//        // TODO method
//        return Observable.create(new Observable.OnSubscribe<List<INote>>() {
//            @Override
//            public void call(Subscriber<? super List<INote>> subscriber) {
//                if (subscriber.isUnsubscribed()) return;
//                List<INote> iNotes = getNotesAsync(program, talk, date, user);
//                subscriber.onNext(iNotes);
//                subscriber.onCompleted();
//            }
//        });
//    }

  private List<IProgram> pinProgramsAsync() {
    try {
      return Queries.Live.pinPrograms();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  private IProgram pinProgramAsync(String programId) {
    try {
      return Queries.Live.pinProgram(programId);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  private List<ITalk> pinTalksAsync(IProgram program) {
    if (program instanceof Program) {
      try {
        return Queries.Live.pinAllProgramTalksFor((Program) program);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  private INote deleteNoteAsync(INote note) {
    if (note instanceof Note) {
//      Commands.Local.deleteEventuallyNote((Note) note);
      return note;
    }
    return note;
  }

  private List<INote> pinNotesAsync(IProgram iProgram, ITalk iTalk, DateTime dateTime, IUser iUser) {
    try {
      if (iProgram instanceof Program) {
        Program program = (Program) iProgram;
        Date date = null;
        Talk talk = (iTalk != null && iTalk instanceof Talk) ? (Talk) iTalk : null;
        if (dateTime != FOREVER) {
          date = dateTime.toDate();
        }

        if (iUser == USER_GENERIC) {
          if (iTalk == ALL_TALKS) {
            return Queries.Live.pinAllGenericNotesFor(program, date);
          } else {
            return Queries.Live.pinAllGenericNotesFor(program, talk, date);
          }
        }

        if (iUser == USER_ME) {
          if (iTalk == ALL_TALKS) {
            return Queries.Live.pinAllClientOwnedNotesFor(program, date);
          } else {
            return Queries.Live.pinAllClientOwnedNotesFor(program, talk, date);
          }
        }

        if (iUser == USER_ALL) {
          throw new IllegalStateException("FIXME");
        }
        return null;
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }


  public abstract class ParseLocalHelper implements LocalHelper {

    @Override
    public Observable<List<IProgram>> getPrograms() {
      return Observable.create(new Observable.OnSubscribe<List<IProgram>>() {
        @Override
        public void call(Subscriber<? super List<IProgram>> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          List<IProgram> program = getProgramsAsync();
          subscriber.onNext(program);
          subscriber.onCompleted();
        }
      });
    }

    @Override
    public Observable<List<IProgram>> pinPrograms(final List<IProgram> programs) {
      return Observable.create(new Observable.OnSubscribe<List<IProgram>>() {
        @Override
        public void call(Subscriber<? super List<IProgram>> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          pinPrograms(programs);
          subscriber.onNext(programs);
          subscriber.onCompleted();
        }
      });
    }

    @Override
    public Observable<IProgram> pinProgram(final IProgram program) {
      return Observable.create(new Observable.OnSubscribe<IProgram>() {
        @Override
        public void call(Subscriber<? super IProgram> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          pinProgram(program);
          subscriber.onNext(program);
          subscriber.onCompleted();
        }
      });
    }

    @Override
    public Observable<ITalk> pinTalk(ITalk talk) {
      return null;
    }

    @Override
    public Observable<List<ITalk>> pinTalks(List<ITalk> talks) {
      return null;
    }

    @Override
    public Observable<IProgram> getProgram(final String programId) {
      return Observable.create(new Observable.OnSubscribe<IProgram>() {
        @Override
        public void call(Subscriber<? super IProgram> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          IProgram program = getProgramAsync(programId);
          subscriber.onNext(program);
          subscriber.onCompleted();
        }
      });
    }

    @Override
    public Observable<ITalk> getTalk(final String talkId) {
      return Observable.create(new Observable.OnSubscribe<ITalk>() {
        @Override
        public void call(Subscriber<? super ITalk> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          ITalk talk = getTalkAsync(talkId);
          subscriber.onNext(talk);
          subscriber.onCompleted();
        }
      });
    }

    @Override
    public Observable<ITalk> getTalkAtSequence(final String sequencePosition) {
      return Observable.create(new Observable.OnSubscribe<ITalk>() {
        @Override
        public void call(Subscriber<? super ITalk> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          ITalk talk = getTalkAtSequenceAsync(sequencePosition);
          subscriber.onNext(talk);
          subscriber.onCompleted();
        }
      });
    }

    @Override
    public Observable<List<ITalk>> getTalks(final IProgram program) {
      return Observable.create(new Observable.OnSubscribe<List<ITalk>>() {
        @Override
        public void call(Subscriber<? super List<ITalk>> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          List<ITalk> talks = getProgramTalksAsync(program);
          subscriber.onNext(talks);
          subscriber.onCompleted();
        }
      });
    }

    @Override
    public Observable<List<INote>> getNotes(final IProgram program, final ITalk talk, final DateTime date, final IUser user) {
      return Observable.create(new Observable.OnSubscribe<List<INote>>() {
        @Override
        public void call(Subscriber<? super List<INote>> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          List<INote> notes = getNotesAsync(program, talk, date, user);
          subscriber.onNext(notes);
          subscriber.onCompleted();
        }
      });
    }

    @Override
    public Observable<List<INote>> pinNotes(final List<INote> notes) {
      return Observable.create(new Observable.OnSubscribe<List<INote>>() {
        @Override
        public void call(Subscriber<? super List<INote>> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          pinNotesAsync(notes);
          subscriber.onNext(notes);
          subscriber.onCompleted();
        }
      });
    }

    @Override
    public Observable<INote> deleteNote(final INote note) {
      return Observable.create(new Observable.OnSubscribe<INote>() {
        @Override
        public void call(Subscriber<? super INote> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          deleteNoteAsync(note);
          subscriber.onNext(note);
          subscriber.onCompleted();
        }
      });
    }

    private List<IProgram> getProgramsAsync() {
      try {
        return Queries.Local.getPrograms();
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }

    private IProgram getProgramAsync(String programId) {
      try {
        return Queries.Local.getProgram(programId);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }

    private List<ITalk> getProgramTalksAsync(IProgram iProgram) {
      try {
        if (iProgram instanceof Program) {
          Program program = (Program) iProgram;

          List<ITalk> results = new ArrayList<>();
          results.addAll(Queries.Local.findAllProgramTalks(program));
          return results;
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }

    private ITalk getTalkAsync(String talkId) {
      try {
        return Queries.Local.getTalk(talkId);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }

    private ITalk getTalkAtSequenceAsync(String sequencePosition) {
      try {
        return Queries.Local.getTalkAtSequence(sequencePosition);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }

    private List<INote> getNotesAsync(IProgram iProgram, ITalk iTalk, DateTime dateTime, IUser iUser) {
      try {
        if (iProgram instanceof Program) {
          Program program = (Program) iProgram;
          Date date = null;
          Talk talk = (iTalk != null && iTalk instanceof Talk) ? (Talk) iTalk : null;
          if (dateTime != FOREVER) {
            date = dateTime.toDate();
          }

          if (iUser == USER_ALL) {
            throw new IllegalStateException("FIXME");
          } else {
            if (talk != ALL_TALKS) {
              List<INote> results = new ArrayList<>();
              results.addAll(Queries.Local.findGenericNotesFor(talk));
              results.addAll(Queries.Local.findClientOwnedNotesFor(talk));
              return results;
            }
          }

          return null;
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }

    private void pinNotesAsync(List<INote> notes) {
      for (INote iNote : notes) {
        if (iNote instanceof Note) {
          try {
            ((Note) iNote).pin(NOTE_TAG);
          } catch (ParseException e) {
            e.printStackTrace();
          }
        }
      }
    }

    private void pinTalksAsync(List<ITalk> talks) {
      for (ITalk iTalk : talks) {
        if (iTalk instanceof Talk) {
          try {
            ((Talk) iTalk).pin(TALK_TAG);
          } catch (ParseException e) {
            e.printStackTrace();
          }
        }
      }
    }

    private void pinProgramsAsync(List<IProgram> programs) {
      for (IProgram iProgram : programs) {
        if (iProgram instanceof Program) {
          try {
            ((Program) iProgram).pin(PROGRAM_TAG);
          } catch (ParseException e) {
            e.printStackTrace();
          }
        }
      }
    }

    private List<INote> unpinNotesAsync(IProgram iProgram, ITalk iTalk, DateTime dateTime, IUser iUser) {
      try {
        if (iProgram instanceof Program) {
          Program program = (Program) iProgram;
          Date date = (dateTime != FOREVER) ? dateTime.toDate() : null;
          Talk talk = (iTalk != null && iTalk instanceof Talk) ? (Talk) iTalk : null;
          if (dateTime != FOREVER) {
            date = dateTime.toDate();
          }

          if (iUser == USER_GENERIC) {
            return Queries.Local.unpinAllGenericNotesFor(program);
          }

          if (iUser == USER_ME) {
            if (iTalk == ALL_TALKS) {
              return Queries.Local.unpinAllClientOwnedNotesFor(program, date);
            } else {
              return Queries.Local.unpinAllClientOwnedNotesFor(program, talk, date);
            }
          }

          if (iUser == USER_ALL) {
            throw new IllegalStateException("FIXME");
          }
          return null;
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }
  }

  public abstract class ParseRemoteHelper implements RemoteHelper {

    @Override
    public Observable<List<IProgram>> getPrograms() {
      return Observable.create(new Observable.OnSubscribe<List<IProgram>>() {
        @Override
        public void call(Subscriber<? super List<IProgram>> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          List<IProgram> iPrograms = pinProgramsAsync();
          subscriber.onNext(iPrograms);
          subscriber.onCompleted();
        }
      });
    }

    @Override
    public Observable<IProgram> getProgram(final String programId) {
      return Observable.create(new Observable.OnSubscribe<IProgram>() {
        @Override
        public void call(Subscriber<? super IProgram> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          IProgram iProgram = pinProgramAsync(programId);
          subscriber.onNext(iProgram);
          subscriber.onCompleted();
        }
      });
    }

    @Override
    public Observable<List<ITalk>> getTalks(final IProgram program) {
      return Observable.create(new Observable.OnSubscribe<List<ITalk>>() {
        @Override
        public void call(Subscriber<? super List<ITalk>> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          List<ITalk> talks = pinTalksAsync(program);
          subscriber.onNext(talks);
          subscriber.onCompleted();
        }
      });
    }

//    @Override
//    public Observable<List<INote>> getNotes(final IProgram program, final ITalk talk, final DateTime date, final IUser user) {
//      return Observable.create(new Observable.OnSubscribe<List<INote>>() {
//        @Override
//        public void call(Subscriber<? super List<INote>> subscriber) {
//          if (subscriber.isUnsubscribed()) return;
//          List<INote> iNotes = pinNotesAsync(program, talk, date, user);
//          subscriber.onNext(iNotes);
//          subscriber.onCompleted();
//        }
//      });
//    }

    @Override
    public Observable<List<INote>> saveNotes(final List<INote> iNotes) {
      return Observable.create(new Observable.OnSubscribe<List<INote>>() {
        @Override
        public void call(Subscriber<? super List<INote>> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          saveNotesAsync(iNotes);
        }
      });
    }

    @Override
    public Observable<List<INote>> getNotes(final IProgram program) {
      return Observable.create(new Observable.OnSubscribe<List<INote>>() {
        @Override
        public void call(Subscriber<? super List<INote>> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          List<INote> notes = new ArrayList<>();
          notes.addAll(pinNotesAsync(program, ALL_TALKS, FOREVER, USER_GENERIC));
          notes.addAll(pinNotesAsync(program, ALL_TALKS, FOREVER, USER_ME));
          subscriber.onNext(notes);
          subscriber.onCompleted();
        }
      });
    }

    @Override
    public Observable<INote> deleteNote(final INote note) {
      return Observable.create(new Observable.OnSubscribe<INote>() {
        @Override
        public void call(Subscriber<? super INote> subscriber) {
          if (subscriber.isUnsubscribed()) return;
          deleteNote(note);
          subscriber.onNext(note);
          subscriber.onCompleted();
        }
      });
    }

    private List<INote> saveNotesAsync(List<INote> iNotes) {
      return ParseUtil.toINote(
          Commands.Live.saveNotesNow(
              ParseUtil.fromINote(iNotes)));
    }
  }

  /**
   * Created by klemeilleur on 7/8/2015.
   */
  public static class Commands {

    private static final String TAG = Commands.class.getSimpleName();

    public static class Live {

      public //
      static Note saveNoteNow(Note note) {
        Log.d(TAG, "saveNoteNow " + note.getObjectId());
        try {
          note.save();
        } catch (ParseException e) {
          e.printStackTrace();
        }
        return note;
      }

      public //
      static List<Note> saveNotesNow(List<Note> notes) {
        Log.d(TAG, "saveNoteNow: size " + notes.size());
        for (Note note : notes) {
          saveNoteNow(note);
        }
        return notes;
      }
    }

    public static class Local {

      public //
      static ParseUser getClientUser() {
        Log.d(TAG, "getClientUser");
        return ParseUser.getCurrentUser();
      }

//      public //
//      static Note saveEventuallyNote(Note note) {
//        saveEventuallyNote(note, null);
//        return note;
//      }
//
//      public //
//      static Note saveEventuallyNote(Note note, SaveCallback callback) {
//        Log.d(TAG, "saveEventuallyNote " + note.getObjectId());
//        if (!note.isNoteOwnedByClient()) {
//          // do not delete notes that are not owned by the client
//          Log.d(TAG, "note was not owned by client. not saving.");
//          return note;
//        }
//        note.saveEventually(callback);
//        return note;
//      }
//
//      public //
//      static List<Note> saveEventuallyNotes(List<Note> notes) {
//        Log.d(TAG, "saveEventuallyNotes: size " + notes.size());
//        for (Note note : notes) {
//          saveEventuallyNote(note);
//        }
//        return notes;
//      }
//
//      public //
//      static List<Note> saveEventuallyParseNotes(List<Note> notes) {
//        Log.d(TAG, "saveEventuallyNotes: size " + notes.size());
//        for (INote note : notes) {
//          if (note instanceof Note) {
//            saveEventuallyNote((Note) note);
//          }
//        }
//        return notes;
//      }
//
//      public //
//      static Note pinNote(Note note) {
//        Log.d(TAG, "pinNote: " + note.getObjectId());
//        note.pinInBackground();
//        return note;
//      }
//
//      public //
//      static void deleteEventuallyNote(Note note) {
//        Log.d(TAG, "deleteEventuallyNote " + note.getObjectId());
//        if (!note.isNoteOwnedByClient()) {
//          // do not delete notes that are not owned by the client
//          Log.d(TAG, "note was not owned by client. not deleting.");
//          return;
//        }
//        note.deleteEventually();
//      }
//
//      public //
//      static void deleteEventuallyNotes(List<Note> notes) {
//        Log.d(TAG, "deleteEventuallyNotes: size " + notes.size());
//        for (Note note : notes) {
//          deleteEventuallyNote(note);
//        }
//      }
//
//      public //
//      static void logoutClientUser() {
//        ParseUser.logOut();
//      }
    }
  }

  /**
   * Created by klemeilleur on 7/7/2015.
   */
  public static class Queries {

    private static final String TAG = Queries.class.getSimpleName();
    //  private static final int FIRST_OR_ONLY = 0;

    private static final int LIMIT_QUERY_MAXIMUM_ALLOWED = 100;
    private static final int LIMIT_SKIP_MAXIMUM_ALLOWED = 10000;


    /**
     * ALL ACTIONS ARE THREAD-BLOCKING. Best not to access on UI-THREAD
     */
    public static class Live {

      // CURRENT LIMIT = 10000
      public //
      static List<INote> pinAllClientOwnedNotesFor(Program program, Date date)
          throws ParseException {
        Log.d(TAG, "pinAllClientOwnedNotesFor " + program.getObjectId());
        int currentPage = 0;
        int notesPinned = 0;
        final List<INote> notes = new ArrayList<>();
        do {
          // use private complex method for specific query details
          final List<INote> moreNotes = queryClientOwnedNotesFor(program, null, date, currentPage);
          final int size = moreNotes.size();
          notesPinned = notesPinned + size;
          notes.addAll(moreNotes);
          currentPage++;
          Log.d(TAG, "pinAllClientOwnedNotesFor(loop) | page: " +
              currentPage + " notesPinned: " + notesPinned);
        } while (notesPinned == currentPage * LIMIT_QUERY_MAXIMUM_ALLOWED
            && notesPinned < LIMIT_SKIP_MAXIMUM_ALLOWED);
        Note.pinAll(Constants.NOTE_PIN_NAME, ParseUtil.fromINote(notes));
        return notes;
      }

      public //
      static List<INote> pinAllGenericNotesFor(Program program, Date date)
          throws ParseException {
        Log.d(TAG, "pinAllGenericNotesFor " + program.getObjectId());
        int currentPage = 0;
        int notesPinned = 0;
        final List<INote> notes = new ArrayList<>();
        do {
          final List<INote> moreNotes = queryGenericNotesFor(program, null, date, currentPage);
          final int size = moreNotes.size();
          notesPinned = notesPinned + size;
          notes.addAll(moreNotes);
          currentPage++;
          Log.d(TAG, "pinAllGenericNotesFor(loop) | page: " +
              currentPage + " notesPinned: " + notesPinned);
        } while (notesPinned == currentPage * LIMIT_QUERY_MAXIMUM_ALLOWED
            && notesPinned < LIMIT_SKIP_MAXIMUM_ALLOWED);
        Note.pinAll(Constants.NOTE_PIN_NAME, ParseUtil.fromINote(notes));
        return notes;
      }

      public //
      static List<INote> pinAllClientOwnedNotesFor(Program program, Talk talk, Date date)
          throws ParseException {
        Log.d(TAG, "pinAllClientOwnedNotesFor " + program.getObjectId() + " and " + talk.getObjectId());
        int currentPage = 0;
        int notesPinned = 0;
        final List<INote> notes = new ArrayList<>();
        do {
          final List<INote> moreNotes = queryClientOwnedNotesFor(program, talk, date, currentPage);
          final int size = moreNotes.size();
          notesPinned = notesPinned + size;
          notes.addAll(moreNotes);
          currentPage++;
          Log.d(TAG, "pinAllClientOwnedNotesFor(loop) | page: " +
              currentPage + " notesPinned: " + notesPinned + ((date == null) ? "" : " @ " + date.getTime()));
        } while (notesPinned == currentPage * LIMIT_QUERY_MAXIMUM_ALLOWED
            && notesPinned < LIMIT_SKIP_MAXIMUM_ALLOWED);
        Note.pinAll(Constants.NOTE_PIN_NAME, ParseUtil.fromINote(notes));
        return notes;
      }

      public //
      static List<INote> pinAllGenericNotesFor(Program program, Talk talk, Date date)
          throws ParseException {
        Log.d(TAG, "pinAllGenericNotesFor " + program.getObjectId() + " and " + talk.getObjectId());
        int currentPage = 0;
        int notesPinned = 0;
        final List<INote> notes = new ArrayList<>();
        do {
          final List<INote> moreNotes = queryGenericNotesFor(program, talk, date, currentPage);
          final int size = moreNotes.size();
          notesPinned = notesPinned + size;
          notes.addAll(moreNotes);
          currentPage++;
          Log.d(TAG, "pinAllGenericNotesFor(loop) | page: " +
              currentPage + " notesPinned: " + notesPinned);
        } while (notesPinned == currentPage * LIMIT_QUERY_MAXIMUM_ALLOWED
            && notesPinned < LIMIT_SKIP_MAXIMUM_ALLOWED);
        Note.pinAll(Constants.NOTE_PIN_NAME, ParseUtil.fromINote(notes));
        return notes;
      }

      // CURRENT LIMIT = 1000
      public //
      static List<ITalk> pinAllProgramTalksFor(Program program)
          throws ParseException {
        Log.d(TAG, "pinAllProgramTalksFor " + program.getObjectId());
        final List<Talk> talks = ParseQuery.getQuery(Talk.class)
            .whereEqualTo(Constants.TALK_oPROGRAM_OBJECT_KEY, program)
            .setLimit(LIMIT_QUERY_MAXIMUM_ALLOWED)
            .find();
        Talk.pinAll(Constants.TALK_PIN_NAME, talks);
        return ParseUtil.toITalk(talks);
      }

      public //
      static Program pinProgram(String programId)
          throws ParseException {
        Log.d(TAG, "pinProgram " + programId);
        final Program program = ParseQuery.getQuery(Program.class)
            .get(programId);
        program.pin(Constants.PROGRAM_PIN_NAME);
        return program;
      }

      public //
      static List<IProgram> pinPrograms()
          throws ParseException {
        Log.d(TAG, "pinPrograms: all");
        final List<Program> programs = ParseQuery.getQuery(Program.class)
            .find();
        Program.pinAll(Constants.PROGRAM_PIN_NAME, programs);
        return ParseUtil.toIProgram(programs);
      }
    }


    public static class Local {

      public //
      static List<INote> unpinAllClientOwnedNotesFor(Program program, Talk talk, Date date)
          throws ParseException {
        Log.d(TAG, "unpinAllClientOwnedNotesFor " + program.getObjectId() + " and " + talk.getObjectId());
        int currentPage = 0;
        int notesPinned = 0;
        final List<INote> notes = new ArrayList<>();
        do {
          final List<INote> moreNotes = queryLocalClientOwnedNotesFor(program, talk, date, currentPage);
          final int size = moreNotes.size();
          notesPinned = notesPinned + size;
          notes.addAll(moreNotes);
          currentPage++;
          Log.d(TAG, "unpinAllClientOwnedNotesFor(loop) | page: " +
              currentPage + " notesUnpinned: " + notesPinned + ((date == null) ? "" : " @ " + date.getTime()));
        } while (notesPinned == currentPage * LIMIT_QUERY_MAXIMUM_ALLOWED
            && notesPinned < LIMIT_SKIP_MAXIMUM_ALLOWED);
        Note.unpinAll(Constants.NOTE_PIN_NAME, ParseUtil.fromINote(notes));
        return notes;
      }

      public //
      static List<INote> unpinAllGenericNotesFor(Program program)
          throws ParseException {
        Log.d(TAG, "unpinAllGenericNotesFor " + program.getObjectId());
        int currentPage = 0;
        int notesPinned = 0;
        final List<INote> notes = new ArrayList<>();
        do {
          final List<INote> moreNotes = queryLocalGenericNotesFor(program, null, null, currentPage);
          final int size = moreNotes.size();
          notesPinned = notesPinned + size;
          notes.addAll(moreNotes);
          currentPage++;
          Log.d(TAG, "unpinAllGenericNotesFor(loop) | page: " +
              currentPage + " notesUnpinned: " + notesPinned);
        } while (notesPinned == currentPage * LIMIT_QUERY_MAXIMUM_ALLOWED
            && notesPinned < LIMIT_SKIP_MAXIMUM_ALLOWED);
        Note.unpinAll(ParseUtil.fromINote(notes));
        return notes;
      }

      public //
      static List<INote> unpinAllClientOwnedNotesFor(Program program, Date date)
          throws ParseException {
        Log.d(TAG, "unpinAllClientOwnedNotesFor " + program.getObjectId());
        int currentPage = 0;
        int notesPinned = 0;
        final List<INote> notes = new ArrayList<>();
        do {
          final List<INote> moreNotes = queryLocalClientOwnedNotesFor(program, null, date, currentPage);
          final int size = moreNotes.size();
          notesPinned = notesPinned + size;
          notes.addAll(moreNotes);
          currentPage++;
          Log.d(TAG, "unpinAllClientOwnedNotesFor(loop) | page: " +
              currentPage + " notesUnpinned: " + notesPinned + ((date == null) ? "" : " @ " + date.getTime()));
        } while (notesPinned == currentPage * LIMIT_QUERY_MAXIMUM_ALLOWED
            && notesPinned < LIMIT_SKIP_MAXIMUM_ALLOWED);
        Note.unpinAll(Constants.NOTE_PIN_NAME, ParseUtil.fromINote(notes));
        return notes;
      }

      public //
      static void unpinNotes() throws ParseException {
        Note.unpinAll(Constants.NOTE_PIN_NAME);
      }

      public //
      static void unpinTalks() throws ParseException {
        Talk.unpinAll(Constants.TALK_PIN_NAME);
      }

      public //
      static void unpinPrograms() throws ParseException {
        Program.unpinAll(Constants.PROGRAM_PIN_NAME);
      }

      public //
      static List<INote> findAllClientOwnedNotes()
          throws ParseException {
        Log.d(TAG, "findAllClientOwnedNotes");
        final List<INote> notes = ParseUtil.toINote(ParseQuery.getQuery(Note.class)
            .fromLocalDatastore()
            .whereEqualTo(Constants.NOTE_uOWNER_USER_KEY,
                Commands.Local.getClientUser())
            .find());
        return notes;
      }

      public //
      static int countPrograms()
          throws ParseException {
        Log.d(TAG, "countPrograms");
        final List<Program> programs = ParseQuery.getQuery(Program.class)
            .fromLocalDatastore()
            .find();
        return programs.size();
      }

      public //
      static List<IProgram> getPrograms()
          throws ParseException {
        Log.d(TAG, "getPrograms: all");
        final List<Program> programs = ParseQuery.getQuery(Program.class)
            .fromLocalDatastore()
            .find();
        return ParseUtil.toIProgram(programs);
      }

      public //
      static IProgram getProgram(String programId)
          throws ParseException {
        Log.d(TAG, "getProgram " + programId);
        final Program program = ParseQuery.getQuery(Program.class)
            .fromLocalDatastore()
            .get(programId);
        return program;
      }

      public //
      static ITalk getTalk(String talkId)
          throws ParseException {
        Log.d(TAG, "getTalk " + talkId);
        final ITalk talk = ParseQuery.getQuery(Talk.class)
            .fromLocalDatastore()
            .get(talkId);
        return talk;
      }

      public //
      static ITalk getTalkAtSequence(String sequence)
          throws ParseException, IndexOutOfBoundsException {
        Log.d(TAG, "getTalkAtSequence " + sequence);
        final ITalk talk = ParseQuery.getQuery(Talk.class)
            .fromLocalDatastore()
            .whereEqualTo(Constants.TALK_SEQUENCE_STRING_KEY, sequence)
            .find().get(0);
        return talk;
      }

      public //
      static List<INote> findClientOwnedNotesFor(Talk talk)
          throws ParseException {
        Log.d(TAG, "findClientOwnedNotesFor " + talk.getObjectId());
        final List<INote> notes = ParseUtil.toINote(ParseQuery.getQuery(Note.class)
            .fromLocalDatastore()
            .whereEqualTo(Constants.NOTE_oTALK_OBJECT_KEY,
                talk)
            .whereEqualTo(Constants.NOTE_uOWNER_USER_KEY,
                Commands.Local.getClientUser())
            .orderByAscending(Constants.NOTE_CREATEDAT_DATE_KEY)
            .find());
        return notes;
      }

      public //
      static List<INote> findGenericNotesFor(Talk talk)
          throws ParseException {
        Log.d(TAG, "findClientOwnedNotesFor " + talk.getObjectId());
        final List<INote> notes = ParseUtil.toINote(ParseQuery.getQuery(Note.class)
            .fromLocalDatastore()
            .whereEqualTo(Constants.NOTE_oTALK_OBJECT_KEY,
                talk)
            .whereDoesNotExist(Constants.NOTE_uOWNER_USER_KEY)
            .orderByAscending(Constants.NOTE_CREATEDAT_DATE_KEY)
            .find());
        return notes;
      }

      // public
      //    static Note findLastClientOwnedNoteFor(Talk talk)
      //        throws ParseException {
      //      final List<INote> notes = ParseQuery.getQuery(Note.class)
      //          .fromLocalDatastore()
      //          .whereEqualTo(Constants.NOTE_oTALK_OBJECT_KEY,
      //              talk)
      //          .whereEqualTo(Constants.NOTE_uOWNER_USER_KEY,
      //              Commands.Local.getClientUser())
      //          .whereDoesNotExist(Constants.NOTE_NEXTNOTE_OBJECT_KEY)
      //          .find();
      //      return notes.get(FIRST_OR_ONLY);
      //    }

      public //
      static List<ITalk> findAllProgramTalks(Program program)
          throws ParseException {
        Log.d(TAG, "findAllProgramTalks " + program.getObjectId());
        final List<Talk> talks = ParseQuery.getQuery(Talk.class)
            .fromLocalDatastore()
            .whereEqualTo(Constants.TALK_oPROGRAM_OBJECT_KEY, program)
            .find();
        return ParseUtil.toITalk(talks);
      }

      public //
      static Note getNote(String noteId)
          throws ParseException {
        Log.d(TAG, "getNote " + noteId);
        final Note note = ParseQuery.getQuery(Note.class)
            .fromLocalDatastore()
            .get(noteId);
        return note;
      }
    }

    private static List<INote> queryGenericNotesFor(Program program, Talk talk, Date date, int page)
        throws ParseException {
      ParseQuery<Note> query = ParseQuery.getQuery(Note.class)
          .whereDoesNotExist(Constants.NOTE_uOWNER_USER_KEY)
          .setSkip(page * LIMIT_QUERY_MAXIMUM_ALLOWED)
          .setLimit(LIMIT_QUERY_MAXIMUM_ALLOWED);
      if (talk != null) {
        query = query.whereEqualTo(Constants.NOTE_oTALK_OBJECT_KEY, talk);
      }
      if (date != null) {
        query = query.whereGreaterThanOrEqualTo(Constants.NOTE_UPDATEDAT_DATE_KEY,
            date);
      }
      final List<INote> notes = ParseUtil.toINote(query.find());
      return notes;
    }

    // PRIVATE UNIFIED USAGE METHOD
    private static List<INote> queryClientOwnedNotesFor(Program program, Talk talk, Date date, int page)
        throws ParseException {
      ParseQuery<Note> query = ParseQuery.getQuery(Note.class)
          .whereEqualTo(Constants.NOTE_uOWNER_USER_KEY,
              Commands.Local.getClientUser())
          .setSkip(page * LIMIT_QUERY_MAXIMUM_ALLOWED)
          .setLimit(LIMIT_QUERY_MAXIMUM_ALLOWED);
      if (program != null) {
        query = query.whereEqualTo(Constants.NOTE_oPROGRAM_OBJECT_KEY, program);
      }
      if (date != null) {
        query = query.whereGreaterThanOrEqualTo(Constants.NOTE_UPDATEDAT_DATE_KEY,
            date);
      }
      if (talk != null) {
        query = query.whereEqualTo(Constants.NOTE_oTALK_OBJECT_KEY, talk);
      }
      final List<INote> notes = ParseUtil.toINote(query.find());
      return notes;
    }

    // PRIVATE UNIFIED USAGE METHOD
    private static List<INote> queryLocalClientOwnedNotesFor(Program program, Talk talk, Date date, int page)
        throws ParseException {
      ParseQuery<Note> query = ParseQuery.getQuery(Note.class)
          .fromLocalDatastore()
          .whereEqualTo(Constants.NOTE_uOWNER_USER_KEY,
              Commands.Local.getClientUser())
          .setSkip(page * LIMIT_QUERY_MAXIMUM_ALLOWED)
          .setLimit(LIMIT_QUERY_MAXIMUM_ALLOWED);
      if (program != null) {
        query = query.whereEqualTo(Constants.NOTE_oPROGRAM_OBJECT_KEY, program);
      }
      if (date != null) {
        query = query.whereGreaterThanOrEqualTo(Constants.NOTE_UPDATEDAT_DATE_KEY,
            date);
      }
      if (talk != null) {
        query = query.whereEqualTo(Constants.NOTE_oTALK_OBJECT_KEY, talk);
      }
      final List<INote> notes = ParseUtil.toINote(query.find());
      return notes;
    }

    // PRIVATE UNIFIED USAGE METHOD
    private static List<INote> queryLocalGenericNotesFor(Program program, Talk talk, Date date, int page)
        throws ParseException {
      ParseQuery<Note> query = ParseQuery.getQuery(Note.class)
          .fromLocalDatastore()
          .whereDoesNotExist(Constants.NOTE_uOWNER_USER_KEY)
          .setSkip(page * LIMIT_QUERY_MAXIMUM_ALLOWED)
          .setLimit(LIMIT_QUERY_MAXIMUM_ALLOWED);
      if (program != null) {
        query = query.whereEqualTo(Constants.NOTE_oPROGRAM_OBJECT_KEY, program);
      }
      if (date != null) {
        query = query.whereGreaterThanOrEqualTo(Constants.NOTE_UPDATEDAT_DATE_KEY,
            date);
      }
      if (talk != null) {
        query = query.whereEqualTo(Constants.NOTE_oTALK_OBJECT_KEY, talk);
      }
      final List<INote> notes = ParseUtil.toINote(query.find());
      return notes;
    }
  }
}