package com.ameron32.apps.tapnotes.v2.di.controller;

import android.app.Application;
import android.util.Log;

import com.ameron32.apps.tapnotes.v2.frmk.object.Progress;
import com.ameron32.apps.tapnotes.v2.parse.ControllerActions;
import com.ameron32.apps.tapnotes.v2.parse.Queries;
import com.ameron32.apps.tapnotes.v2.parse.Status;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.parse.object.Talk;
import com.ameron32.apps.tapnotes.v2.util.Serializer;
import com.parse.ParseException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.util.Date;

import rx.Observable;

/**
 * Created by klemeilleur on 7/22/2015.
 * TODO confusing ControllerActions names and usage. Needs improvement.
 */
public class ParseNotesController extends AbsApplicationController {

  private static final String TAG = ParseNotesController.class.getSimpleName();

  private Serializer<DateTime> serializer;
  private DateTime lastCheckedAllNotes;

  public ParseNotesController(Application application) {
    super(application);
    // empty constructor
    serializer = new Serializer<>(DateTime.class);
  }

  public Observable<Progress> pinAllNewClientOwnedNotesFor(Program program, Talk talk) {
    if (!Status.isConnectionToServerAvailable(getApplication())) {
      return instantComplete();
    }

    final DateTime checkedTime = getLastCheckedAllNotes();
    return ControllerActions.pinClientNotesFor(program, talk, checkedTime.toDate());
  }

  public Observable<Progress> pinAllClientOwnedNotesFor(Program program, Talk talk) {
    if (!Status.isConnectionToServerAvailable(getApplication())) {
      return instantComplete();
    }

    return ControllerActions.pinClientNotesFor(program, talk, null);
  }

  public Observable<Progress> unpinThenRepinAllClientOwnedNotesFor(Program program, Talk talk) {
    if (!Status.isConnectionToServerAvailable(getApplication())) {
      return instantComplete();
    }

    return ControllerActions.unpinThenRepinClientNotesFor(program, talk, null);
  }

  public Observable<Progress> pinAllNewClientOwnedNotesFor(String programId) {
    if (!Status.isConnectionToServerAvailable(getApplication())) {
      return instantComplete();
    }

    final DateTime checkedTime = getLastCheckedThenUpdateToNow();
    try {
      final Program program = Queries.Local.getProgram(programId);

      if (checkedTime.plusDays(2).isBefore(getNow())) {
        // more than 2 days (48 hours), repin all notes
        return ControllerActions.pinCompleteClientNotesFor(program);
      }

      return ControllerActions.pinNotesFor(program, checkedTime.toDate());
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return instantComplete();
  }

  public Observable<Progress> unpinProgramAndTalksAndNotesThenRepin(String programId) {
    if (!Status.isConnectionToServerAvailable(getApplication())) {
      return instantComplete();
    }

    try {
      final Program program = Queries.Local.getProgram(programId);
      return ControllerActions.unpinProgramAndTalksAndNotesThenRepin(program);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return instantComplete();
  }

  public Observable<Progress> pinProgramAndTalks(String programId) {
    if (!Status.isConnectionToServerAvailable(getApplication())) {
      return instantComplete();
    }

    return ControllerActions.pinProgramAndTalks(programId);
  }

  public Observable<Progress> pinCompleteClientNotesFor(Program program) {
    if (!Status.isConnectionToServerAvailable(getApplication())) {
      return instantComplete();
    }

    return ControllerActions.pinCompleteClientNotesFor(program);
  }



  //

  private Observable<Progress> instantComplete() {
    return ControllerActions.instantComplete();
  }

  public DateTime getLastCheckedThenUpdateToNow() {
    DateTime checked = getLastCheckedAllNotes();
    if (checked != null) {
      return checked;
    }
    updateLastCheckedAllNotesToNow();
    return null;
  }

  private DateTime getLastCheckedAllNotes() {
    if (lastCheckedAllNotes == null) {
      lastCheckedAllNotes = recoverLastCheckedFromSerialization();
    }
    return lastCheckedAllNotes;
  }

  private void updateLastCheckedAllNotesToNow() {
    lastCheckedAllNotes = DateTime.now();
    storeLastCheckedIntoSerialization(lastCheckedAllNotes);
  }

  private DateTime getNow() {
    return DateTime.now();
  }



  //

  private static final String LAST_CHECKED_FILENAME = "lastChecked.date";

  private DateTime recoverLastCheckedFromSerialization() {
    try {
      final DateTime storedLastChecked = serializer.load(getApplication(), LAST_CHECKED_FILENAME);
      Log.d(TAG, "restored lastChecked as: " + storedLastChecked.toString(DateTimeFormat.forPattern("yyyy/MM/dd-hh:mm:ss")));
      return storedLastChecked;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // failed to recover
    return DateTime.parse("20000101", DateTimeFormat.forPattern("yyyyMMdd"));
  }

  private void storeLastCheckedIntoSerialization(final DateTime lastCheckedAllNotes) {
    try {
      serializer.save(getApplication(), LAST_CHECKED_FILENAME, lastCheckedAllNotes);
      Log.d(TAG, "stored lastChecked as: " + lastCheckedAllNotes.toString(DateTimeFormat.forPattern("yyyy/MM/dd-hh:mm:ss")));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
