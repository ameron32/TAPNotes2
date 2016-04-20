package com.ameron32.apps.tapnotes.v2.data.parse;

import android.util.Log;

import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.di.controller.NotesController;
import com.ameron32.apps.tapnotes.v2.frmk.object.Progress;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Program;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Talk;
import com.parse.ParseException;

import java.util.Date;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by kris on 7/19/2015.
 */
public class Rx {

//  static Observable<Progress> instantComplete() {
//    return Observable.create(new Observable.OnSubscribe<Progress>() {
//      @Override
//      public void call(Subscriber<? super Progress> subscriber) {
//        Log.d(NotesController.class.getSimpleName(), "instantComplete called");
//        subscriber.onNext(new Progress(1, 1, false));
//        subscriber.onCompleted();
//      }
//    }).subscribeOn(Schedulers.computation());
//  }
//
//
//
//  public static class Live {
//
//    static Observable<Progress> pinProgramWithTalks(final String programId) {
//      return Observable.create(new Observable.OnSubscribe<Progress>() {
//        @Override
//        public void call(Subscriber<? super Progress> subscriber) {
//          try {
//            subscriber.onNext(new Progress(0, 2, false, "Download Program Notes", "Get program"));
//            final IProgram program = ParseHelper.Queries.Live.pinProgram(programId);
//            subscriber.onNext(new Progress(1, 2, false, "Download Program Notes", "Saving notes"));
//
//            ParseHelper.Queries.Live.pinAllProgramTalksFor((Program) program);
//            subscriber.onNext(new Progress(2, 2, false, "Download Program Notes", "Notes saved"));
//            subscriber.onCompleted();
//          } catch (ParseException e) {
//            e.printStackTrace();
//            subscriber.onNext(new Progress(0, 2, true, "Download Program Notes", "Failed"));
//            subscriber.onCompleted();
//          }
//        }
//      }).subscribeOn(Schedulers.computation());
//    }
//
//    static Observable<Progress> pinAllProgramNotes(final IProgram program) {
//      return Observable.create(new Observable.OnSubscribe<Progress>() {
//        @Override
//        public void call(Subscriber<? super Progress> subscriber) {
//          try {
//            subscriber.onNext(new Progress(0, 2, false, "Download Program Notes", "Downloading Notes (1/2)"));
//
//            ParseHelper.Queries.Live.pinAllGenericNotesFor((Program) program, null);
//            subscriber.onNext(new Progress(1, 2, false, "Download Program Notes", "Downloading Notes (2/2)"));
//            ParseHelper.Queries.Live.pinAllClientOwnedNotesFor((Program) program, null);
//            subscriber.onNext(new Progress(2, 2, false, "Download Program Notes", "Done!"));
//            subscriber.onCompleted();
//          } catch (ParseException e) {
//            e.printStackTrace();
//            subscriber.onNext(new Progress(0, 2, true));
//            subscriber.onCompleted();
//          }
//        }
//      }).subscribeOn(Schedulers.computation());
//    }
//
//    static Observable<Progress> pinRecentClientOwnedNotesFor(final IProgram program, final ITalk talk, final Date date) {
//      return Observable.create(new Observable.OnSubscribe<Progress>() {
//        @Override
//        public void call(Subscriber<? super Progress> subscriber) {
//          subscriber.onNext(new Progress(0, 1, false));
//          try {
//            ParseHelper.Queries.Live.pinAllClientOwnedNotesFor((Program) program, (Talk) talk, date);
//            subscriber.onNext(new Progress(1, 1, false));
//            subscriber.onCompleted();
//          } catch (ParseException e) {
//            subscriber.onNext(new Progress(0, 1, true));
//            subscriber.onCompleted();
//          }
//        }
//      }).subscribeOn(Schedulers.computation());
//    }
//
//    static Observable<Progress> unpinThenRepinAllClientOwnedNotesFor(final IProgram program, final ITalk talk, final Date date) {
//      return Observable.create(new Observable.OnSubscribe<Progress>() {
//        @Override
//        public void call(Subscriber<? super Progress> subscriber) {
//          subscriber.onNext(new Progress(0, 2, false));
//          try {
//            ParseHelper.Queries.Local.unpinAllClientOwnedNotesFor((Program) program, (Talk) talk, date);
//            subscriber.onNext(new Progress(1, 2, false));
//            ParseHelper.Queries.Live.pinAllClientOwnedNotesFor((Program) program, (Talk) talk, date);
//            subscriber.onNext(new Progress(2, 2, false));
//            subscriber.onCompleted();
//          } catch (ParseException e) {
//            subscriber.onNext(new Progress(0, 1, true));
//            subscriber.onCompleted();
//          }
//        }
//      }).subscribeOn(Schedulers.computation());
//    }
//
//    static Observable<Progress> unpinThenRepinAllClientOwnedNotesFor(final IProgram program, final Date date) {
//      return Observable.create(new Observable.OnSubscribe<Progress>() {
//        @Override
//        public void call(Subscriber<? super Progress> subscriber) {
//          try {
//            subscriber.onNext(new Progress(0, 3, false, "Refresh Program Notes", "Clearing Local Notes"));
//            ParseHelper.Queries.Local.unpinAllGenericNotesFor((Program) program);
//            ParseHelper.Queries.Local.unpinAllClientOwnedNotesFor((Program) program, date);
//            subscriber.onNext(new Progress(1, 3, false, "Refresh Program Notes", "Retrieving Theme and Song Notes"));
//            ParseHelper.Queries.Live.pinAllGenericNotesFor((Program) program, date);
//            subscriber.onNext(new Progress(2, 3, false, "Refresh Program Notes", "Retrieving Personal Notes"));
//            ParseHelper.Queries.Live.pinAllClientOwnedNotesFor((Program) program, date);
//            subscriber.onNext(new Progress(3, 3, false, "Refresh Program Notes", "Complete"));
//            subscriber.onCompleted();
//          } catch (ParseException e) {
//            subscriber.onNext(new Progress(0, 3, true));
//            subscriber.onCompleted();
//          }
//        }
//      }).subscribeOn(Schedulers.computation());
//    }
//
//    static Observable<Progress> pinRecentProgramNotes(final IProgram program, final Date date) {
//      return Observable.create(new Observable.OnSubscribe<Progress>() {
//        @Override
//        public void call(Subscriber<? super Progress> subscriber) {
//          try {
//            subscriber.onNext(new Progress(1, 2, false, "Download Program Notes", "Retrieving Theme and Song Notes"));
//            ParseHelper.Queries.Live.pinAllGenericNotesFor((Program) program, date);
//            subscriber.onNext(new Progress(1, 2, false, "Download Program Notes", "Retrieving Personal Notes"));
//            ParseHelper.Queries.Live.pinAllClientOwnedNotesFor((Program) program, date);
//            subscriber.onNext(new Progress(2, 2, false, "Download Program Notes", "Complete"));
//            subscriber.onCompleted();
//          } catch (ParseException e) {
//            subscriber.onNext(new Progress(0, 2, true));
//            subscriber.onCompleted();
//          }
//        }
//      }).subscribeOn(Schedulers.computation());
//    }
//  }
//
//
//
//  public static class Local {
//
//    static Observable<Progress> empty(final String programId) {
//      return Observable.create(
//          new Observable.OnSubscribe<Progress>() {
//            @Override
//            public void call(Subscriber<? super Progress> subscriber) {
//
//            }
//          }
//      ).subscribeOn(Schedulers.computation());
//    }
//  }
}
