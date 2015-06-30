package com.ameron32.apps.tapnotes.v2.parse.rx;


import bolts.Task;


import rx.schedulers.*;
import rx.Observable;
import rx.functions.*;
import rx.observables.*;


/**
 * Bolts2Rx
 * Bolts.Task2Observable
 * RxBolts
 * RxTask
 * BoltsObservable
 * TaskObservable
 */
public class TaskObservable {


  public static <R> Observable<R> just(Task<R> task, boolean nullable) {
    return Observable.create(sub -> {
      task.continueWith(t -> {
        if (t.isCancelled()) {
          // NOTICE: doOnUnsubscribe(() -> Observable.just(query) in outside
          sub.unsubscribe(); //sub.onCompleted();?
        } else if (t.isFaulted()) {
          Throwable error = t.getError();
          sub.onError(error);
        } else {
          R r = t.getResult();
          if (nullable || r != null) sub.onNext(r);
          sub.onCompleted();
        }
        return null;
      });
    });
  }


  public static <R> Observable<R> justNullable(Task<R> task) {
    return just(task, true);
  }


  public static <R> Observable<R> just(Task<R> task) {
    return just(task, false);
  }


  @Deprecated
  public static <R> Observable<R> just(Func0<Task<R>> task) {
    return defer(task);
  }


  public static <R> Observable<R> defer(Func0<Task<R>> task) {
    return Observable.defer(() -> just(task.call()));
  }


}
