package com.ameron32.apps.tapnotes.v2.parse;

import com.ameron32.apps.tapnotes.v2.Progress;
import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.parse.object.Talk;
import com.parse.ParseException;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by kris on 7/19/2015.
 */
public class Rx {

  public static class Live {

    public static Observable<Progress> pinProgramWithTalksAndNotes(final String programId) {
      return Observable.create(new Observable.OnSubscribe<Progress>() {
        @Override
        public void call(Subscriber<? super Progress> subscriber) {
          try {
            // PRECACHE TALKS AND NOTES

            // TODO KRIS is program already pinned? switch to local and delay network query
            // possibly never (aka OFFLINE ONLY)
            final int numberOfPrograms = Queries.Local.countPrograms();
//            if (numberOfPrograms > 0) {
//              // TODO KRIS temporarily skip to test local datastore... FIXME
//              subscriber.onCompleted();
//              return;
//            }

            final Program program = Queries.Live.pinProgram(programId);
            subscriber.onNext(new Progress(1, 3, false));

            // TODO KRIS are the talks already here? switch to local and delay network query
            final List<Talk> talks = Queries.Live.pinAllProgramTalksFor(program);
            subscriber.onNext(new Progress(2, 3, false));

            // TODO KRIS did we local the program and talks? delay network query and restrict to new only
            final List<Note> notes = Queries.Live.pinAllClientOwnedNotesFor(program);
            subscriber.onNext(new Progress(3, 3, false));
            subscriber.onCompleted();
          } catch (ParseException e) {
            e.printStackTrace();
            subscriber.onNext(new Progress(0, 0, true));
            subscriber.onCompleted();
          }
        }
      }).subscribeOn(Schedulers.io());
    }

    public static Observable<Progress> pinAllClientOwnedNotesFor(final Program program, final Talk talk) {
      return Observable.create(
          new Observable.OnSubscribe<Progress>() {
            @Override
            public void call(Subscriber<? super Progress> subscriber) {
              subscriber.onNext(new Progress(0, 1, false));
              try {
                final List<Note> notes = Queries.Live.pinAllClientOwnedNotesFor(program, talk);
              } catch (ParseException e) {
                subscriber.onNext(new Progress(0, 1, true));
                subscriber.onCompleted();
              }
              subscriber.onNext(new Progress(1, 1, false));
              subscriber.onCompleted();
            }
          }
      ).subscribeOn(Schedulers.io());
    }
  }

  public static class Local {

    public static Observable<Progress> empty(final String programId) {
      return Observable.create(
          new Observable.OnSubscribe<Progress>() {
           @Override
           public void call(Subscriber<? super Progress> subscriber) {

           }
         }
      ).subscribeOn(Schedulers.io());
    }
  }
}
