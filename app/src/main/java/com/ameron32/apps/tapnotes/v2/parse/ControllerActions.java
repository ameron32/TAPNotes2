package com.ameron32.apps.tapnotes.v2.parse;

import com.ameron32.apps.tapnotes.v2.frmk.object.Progress;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.parse.object.Talk;

import java.util.Date;

import rx.Observable;

/**
 * Created by klemeilleur on 7/30/2015.
 * TODO confusing Rx.Live names and usage. Needs improvement.
 */
public class ControllerActions {

  // pin recent

  public static Observable<Progress> pinClientNotesFor(Program program, Talk talk, Date checkedTime) {
    return Rx.Live.pinRecentClientOwnedNotesFor(program, talk, checkedTime);
  }

  public static Observable<Progress> unpinThenRepinClientNotesFor(Program program, Talk talk, Date checkedTime) {
    return Rx.Live.unpinThenRepinAllClientOwnedNotesFor(program, talk, checkedTime);
  }

  public static Observable<Progress> pinNotesFor(Program program, Date checkedTime) {
    return Rx.Live.pinRecentProgramNotes(program, checkedTime);
  }

  public static Observable<Progress> unpinProgramAndTalksAndNotesThenRepin(Program program) {
    return Rx.Live.unpinThenRepinAllClientOwnedNotesFor(program, null);
  }

  // pin everything

  public static Observable<Progress> pinProgramAndTalks(String programId) {
    return Rx.Live.pinProgramWithTalks(programId);
  }

  public static Observable<Progress> pinCompleteClientNotesFor(Program program) {
    return Rx.Live.pinAllProgramNotes(program);
  }

  // online unavailable

  public static Observable<Progress> instantComplete() {
    return Rx.instantComplete();
  }
}
