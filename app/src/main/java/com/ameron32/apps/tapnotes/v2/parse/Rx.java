package com.ameron32.apps.tapnotes.v2.parse;

import android.util.Log;

import com.ameron32.apps.tapnotes.v2.di.controller.ParseNotesController;
import com.ameron32.apps.tapnotes.v2.frmk.object.Progress;
import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.parse.object.Talk;
import com.parse.ParseException;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by kris on 7/19/2015.
 */
public class Rx {

  static Observable<Progress> instantComplete() {
    return Observable.create(new Observable.OnSubscribe<Progress>() {
      @Override
      public void call(Subscriber<? super Progress> subscriber) {
        Log.d(ParseNotesController.class.getSimpleName(), "instantComplete called");
        subscriber.onNext(new Progress(1, 1, false));
        subscriber.onCompleted();
      }
    }).subscribeOn(Schedulers.computation());
  }



  public static class Live {

    static Observable<Progress> pinProgramWithTalks(final String programId) {
      return Observable.create(new Observable.OnSubscribe<Progress>() {
        @Override
        public void call(Subscriber<? super Progress> subscriber) {
          try {
            subscriber.onNext(new Progress(0, 2, false, "Download Program Notes", "Get program"));
            final Program program = Queries.Live.pinProgram(programId);
            subscriber.onNext(new Progress(1, 2, false, "Download Program Notes", "Saving notes"));

            // TODO KRIS are the talks already here? switch to local and delay network query
            Queries.Live.pinAllProgramTalksFor(program);
            subscriber.onNext(new Progress(2, 2, false, "Download Program Notes", "Notes saved"));
            subscriber.onCompleted();
          } catch (ParseException e) {
            e.printStackTrace();
            subscriber.onNext(new Progress(0, 2, true, "Download Program Notes", "Failed"));
            subscriber.onCompleted();
          }
        }
      }).subscribeOn(Schedulers.computation());
    }

    static Observable<Progress> pinAllProgramNotes(final Program program) {
      return Observable.create(new Observable.OnSubscribe<Progress>() {
        @Override
        public void call(Subscriber<? super Progress> subscriber) {
          try {
            subscriber.onNext(new Progress(0, 2, false, "Download Program Notes", "Downloading Notes (1/2)"));

            // TODO KRIS did we local the program and talks? delay network query and restrict to new only
            Queries.Live.pinAllGenericNotesFor(program);
            subscriber.onNext(new Progress(1, 2, false, "Download Program Notes", "Downloading Notes (2/2)"));
            Queries.Live.pinAllClientOwnedNotesFor(program, null);
            subscriber.onNext(new Progress(2, 2, false, "Download Program Notes", "Done!"));
            subscriber.onCompleted();
          } catch (ParseException e) {
            e.printStackTrace();
            subscriber.onNext(new Progress(0, 2, true));
            subscriber.onCompleted();
          }
        }
      }).subscribeOn(Schedulers.computation());
    }

    static Observable<Progress> pinRecentClientOwnedNotesFor(final Program program, final Talk talk, final Date date) {
      return Observable.create(new Observable.OnSubscribe<Progress>() {
        @Override
        public void call(Subscriber<? super Progress> subscriber) {
          subscriber.onNext(new Progress(0, 1, false));
          try {
            Queries.Live.pinAllClientOwnedNotesFor(program, talk, date);
            subscriber.onNext(new Progress(1, 1, false));
            subscriber.onCompleted();
          } catch (ParseException e) {
            subscriber.onNext(new Progress(0, 1, true));
            subscriber.onCompleted();
          }
        }
      }).subscribeOn(Schedulers.computation());
    }

    static Observable<Progress> unpinThenRepinAllClientOwnedNotesFor(final Program program, final Talk talk, final Date date) {
      return Observable.create(new Observable.OnSubscribe<Progress>() {
        @Override
        public void call(Subscriber<? super Progress> subscriber) {
          subscriber.onNext(new Progress(0, 2, false));
          try {
            Queries.Local.unpinAllClientOwnedNotesFor(program, talk, date);
            subscriber.onNext(new Progress(1, 2, false));
            Queries.Live.pinAllClientOwnedNotesFor(program, talk, date);
            subscriber.onNext(new Progress(2, 2, false));
            subscriber.onCompleted();
          } catch (ParseException e) {
            subscriber.onNext(new Progress(0, 1, true));
            subscriber.onCompleted();
          }
        }
      }).subscribeOn(Schedulers.computation());
    }

    static Observable<Progress> pinRecentProgramNotes(final Program program, final Date date) {
      return Observable.create(new Observable.OnSubscribe<Progress>() {
        @Override
        public void call(Subscriber<? super Progress> subscriber) {
          subscriber.onNext(new Progress(0, 1, false));
          try {
            Queries.Live.pinAllClientOwnedNotesFor(program, date);
            subscriber.onNext(new Progress(1, 1, false));
            subscriber.onCompleted();
          } catch (ParseException e) {
            subscriber.onNext(new Progress(0, 1, true));
            subscriber.onCompleted();
          }
        }
      }).subscribeOn(Schedulers.computation());
    }
  }



  public static class Local {

    static Observable<Progress> empty(final String programId) {
      return Observable.create(
          new Observable.OnSubscribe<Progress>() {
            @Override
            public void call(Subscriber<? super Progress> subscriber) {

            }
          }
      ).subscribeOn(Schedulers.computation());
    }
  }
}
