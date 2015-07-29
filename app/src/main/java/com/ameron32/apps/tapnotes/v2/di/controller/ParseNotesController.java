package com.ameron32.apps.tapnotes.v2.di.controller;

import com.ameron32.apps.tapnotes.v2.frmk.object.Progress;
import com.ameron32.apps.tapnotes.v2.parse.Queries;
import com.ameron32.apps.tapnotes.v2.parse.Rx;
import com.ameron32.apps.tapnotes.v2.parse.Status;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.parse.object.Talk;
import com.parse.ParseException;

import org.joda.time.DateTime;

import java.util.Date;

import rx.Observable;

/**
 * Created by klemeilleur on 7/22/2015.
 */
public class ParseNotesController {

  private DateTime lastChecked;

  public ParseNotesController() {
    // empty constructor
  }

  public Observable<Progress> pinAllNewClientOwnedNotesFor(Program program, Talk talk) {
    final Date checkedTime = getLastChecked();
    return Rx.Live.pinRecentClientOwnedNotesFor(program, talk, checkedTime);
  }

  public Observable<Progress> pinAllNewClientOwnedNotesFor(String programId) {
    final Date checkedTime = getLastCheckedThenUpdateToNow();
    try {
      final Program program = Queries.Local.getProgram(programId);
      return Rx.Live.pinRecentProgramNotes(program, checkedTime);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Date getLastCheckedThenUpdateToNow() {
    Date checked = getLastChecked();
    if (checked != null) {
      return checked;
    }
    updateLastCheckedToNow();
    return null;
  }

  private Date getLastChecked() {
    if (lastChecked != null) {
      return lastChecked.toDate();
    }
    return null;
  }

  private void updateLastCheckedToNow() {
    lastChecked = DateTime.now();
  }
}
