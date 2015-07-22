package com.ameron32.apps.tapnotes.v2.di.controller;

import android.util.Log;

import com.ameron32.apps.tapnotes.v2.frmk.object.Progress;
import com.ameron32.apps.tapnotes.v2.parse.Queries;
import com.ameron32.apps.tapnotes.v2.parse.Rx;
import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.parse.object.Talk;
import com.parse.ParseException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by klemeilleur on 7/22/2015.
 */
public class ParseNotesController {

  private DateTime lastChecked;

  public ParseNotesController() {
    // empty constructor
  }

  public void pinAllNewClientOwnedNotesFor(Program program, Talk talk) {
    Rx.Live.pinAllClientOwnedNotesFor(program, talk, lastChecked.toDate());
  }

  public Date incrementLastChecked() {
    Date value = null;
    if (lastChecked != null) {
      value = lastChecked.toDate();
    }
    lastChecked = DateTime.now();
    return value;
  }
}
