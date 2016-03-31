package com.ameron32.apps.tapnotes.v2.data.parse;

import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.frmk.object.Progress;

import java.util.Date;

import rx.Observable;

/**
 * Created by klemeilleur on 7/30/2015.
 * TODO confusing Rx.Live names and usage. Needs improvement.
 */
public class ControllerActions {

  // pin recent

  static Observable<Progress> pinClientNotesFor(IProgram program, ITalk talk, Date checkedTime) {
    return Rx.Live.pinRecentClientOwnedNotesFor(program, talk, checkedTime);
  }

  static Observable<Progress> unpinThenRepinClientNotesFor(IProgram program, ITalk talk, Date checkedTime) {
    return Rx.Live.unpinThenRepinAllClientOwnedNotesFor(program, talk, checkedTime);
  }

  static Observable<Progress> pinNotesFor(IProgram program, Date checkedTime) {
    return Rx.Live.pinRecentProgramNotes(program, checkedTime);
  }

  static Observable<Progress> unpinProgramAndTalksAndNotesThenRepin(IProgram program) {
    return Rx.Live.unpinThenRepinAllClientOwnedNotesFor(program, null);
  }

  // pin everything

  static Observable<Progress> pinProgramAndTalks(String programId) {
    return Rx.Live.pinProgramWithTalks(programId);
  }

  static Observable<Progress> pinCompleteClientNotesFor(IProgram program) {
    return Rx.Live.pinAllProgramNotes(program);
  }

  // online unavailable

  static Observable<Progress> instantComplete() {
    return Rx.instantComplete();
  }
}
