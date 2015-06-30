package com.ameron32.apps.tapnotes.v2.parse.rx;


import rx.schedulers.*;
import rx.Observable;

import com.parse.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ParseObservable {

  public static <R extends ParseObject> Observable<R> find(ParseQuery<R> query) {
    return Observable.defer(() -> TaskObservable.just(query.findInBackground()))
        .flatMap(l -> Observable.from(l))
        .doOnUnsubscribe(() -> Observable.just(query)
            .doOnNext(q -> q.cancel())
            .timeout(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe(o -> {}, e -> {}));
  }

  public static <R extends ParseObject> Observable<Integer> count(ParseQuery<R> query) {
    return Observable.defer(() -> TaskObservable.just(query.countInBackground()))
        .doOnUnsubscribe(() -> Observable.just(query)
            .doOnNext(q -> q.cancel())
            .timeout(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe(o -> {}, e -> {}));

  }

  public static <R extends ParseObject> Observable<R> pin(R object) {
    return Observable.defer(() -> TaskObservable.justNullable(object.pinInBackground()))
        .map(v -> object);
  }

  public static <R extends ParseObject> Observable<R> pin(List<R> objects) {
    return Observable.defer(() -> TaskObservable.justNullable(ParseObject.pinAllInBackground(objects)))
        .flatMap(v -> Observable.from(objects));
  }

  public static <R extends ParseObject> Observable<R> pin(String name, R object) {
    return Observable.defer(() -> TaskObservable.justNullable(object.pinInBackground(name)))
        .map(v -> object);
  }

  public static <R extends ParseObject> Observable<R> pin(String name, List<R> objects) {
    return Observable.defer(() -> TaskObservable.justNullable(ParseObject.pinAllInBackground(name, objects)))
        .flatMap(v -> Observable.from(objects));
  }

  public static <R extends ParseObject> Observable<R> unpin(R object) {
    return Observable.defer(() -> TaskObservable.justNullable(object.unpinInBackground()))
        .map(v -> object);
  }

  public static <R extends ParseObject> Observable<R> unpin(List<R> objects) {
    return Observable.defer(() -> TaskObservable.justNullable(ParseObject.unpinAllInBackground(objects)))
        .flatMap(v -> Observable.from(objects));
  }

  public static <R extends ParseObject> Observable<R> unpin(String name, R object) {
    return Observable.defer(() -> TaskObservable.justNullable(object.unpinInBackground(name)))
        .map(v -> object);
  }

  public static <R extends ParseObject> Observable<R> unpin(String name, List<R> objects) {
    return Observable.defer(() -> TaskObservable.justNullable(ParseObject.unpinAllInBackground(name, objects)))
        .flatMap(v -> Observable.from(objects));
  }

  public static <R extends ParseObject> Observable<R> all(ParseQuery<R> query) {
    return count(query).flatMap(c -> all(query, c));
  }

  /** limit 10000 by skip */
  public static <R extends ParseObject> Observable<R> all(ParseQuery<R> query, int count) {
    final int limit = 1000; // limit limitation
    query.setSkip(0);
    query.setLimit(limit);
    Observable<R> find = find(query);
    for (int i = limit; i < count; i+= limit) {
      if (i >= 10000) break; // skip limitation
      query.setSkip(i);
      query.setLimit(limit);
      find.concatWith(find(query));
    }
    return find.distinct(o -> o.getObjectId());
  }

  public static <R extends ParseObject> Observable<R> first(ParseQuery<R> query) {
    return Observable.defer(() -> TaskObservable.just(query.getFirstInBackground()))
        .doOnUnsubscribe(() -> Observable.just(query)
            .doOnNext(q -> q.cancel())
            .timeout(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe(o -> {}, e -> {}));
  }

  public static <R extends ParseObject> Observable<R> get(Class<R> clazz, String objectId) {
    ParseQuery<R> query = ParseQuery.getQuery(clazz);
    return Observable.defer(() -> TaskObservable.just(query.getInBackground(objectId)))
        .doOnUnsubscribe(() -> Observable.just(query)
            .doOnNext(q -> q.cancel())
            .timeout(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe(o -> {}, e -> {}));
  }

  public static <R> Observable<R> callFunction(String name, Map<String, R> params) {
    return Observable.defer(() -> TaskObservable.justNullable(ParseCloud.callFunctionInBackground(name, params)));
  }

  public static <R extends ParseObject> Observable<R> save(R object) {
    return Observable.defer(() -> TaskObservable.justNullable(object.saveInBackground()))
        .map(v -> object);
  }

  public static <R extends ParseObject> Observable<R> save(List<R> objects) {
    return Observable.defer(() -> TaskObservable.justNullable(ParseObject.saveAllInBackground(objects)))
        .flatMap(v -> Observable.from(objects));
  }

  public static <R extends ParseObject> Observable<R> saveEventually(R object) {
    return Observable.defer(() -> TaskObservable.justNullable(object.saveEventually()))
        .map(v -> object);
  }

  public static <R extends ParseObject> Observable<R> fetchIfNeeded(R object) {
    return Observable.defer(() -> TaskObservable.justNullable(object.fetchIfNeededInBackground()));
  }

  public static <R extends ParseObject> Observable<R> fetchIfNeeded(List<R> objects) {
    return Observable.defer(() -> TaskObservable.justNullable(ParseObject.fetchAllIfNeededInBackground(objects)))
        .flatMap(l -> Observable.from(l));
  }

  public static <R extends ParseObject> Observable<R> delete(R object) {
    return Observable.defer(() -> TaskObservable.justNullable(object.deleteInBackground()))
        .map(v -> object);
  }

  public static <R extends ParseObject> Observable<R> delete(List<R> objects) {
    return Observable.defer(() -> TaskObservable.justNullable(ParseObject.deleteAllInBackground(objects)))
        .flatMap(v -> Observable.from(objects));
  }

  public static Observable<String> subscribe(String channel) {
    android.util.Log.d("ParseObservable", "subscribe: channel: " + channel);

    return Observable.defer(() -> TaskObservable.justNullable(ParsePush.subscribeInBackground(channel)))
        .doOnNext(v -> android.util.Log.d("ParseObservable", "doOnNext: " + v))
        .map(v -> channel);
  }

  public static Observable<String> unsubscribe(String channel) {
    android.util.Log.d("ParseObservable", "unsubscribe, channel: " + channel);

    return Observable.defer(() -> TaskObservable.justNullable(ParsePush.unsubscribeInBackground(channel)))
        .map(v -> channel);
  }

    /* ParseFacebookUtils 1.8 */
  // ***
  // * DISABLED BECAUSE FACEBOOK IS NOT NEEDED FOR THIS APP
  // ***
//  public static Observable<ParseUser> link(ParseUser user, Activity activity) {
//    return Observable.defer(() -> TaskObservable.justNullable(ParseFacebookUtils.linkInBackground(user, activity)))
//        .map(v -> user);
//  }
//
//  public static Observable<ParseUser> link(ParseUser user, Activity activity, int activityCode) {
//    return Observable.defer(() -> TaskObservable.justNullable(ParseFacebookUtils.linkInBackground(user, activity, activityCode)))
//        .map(v -> user);
//  }
//
//  public static Observable<ParseUser> link(ParseUser user, Collection<String> permissions, Activity activity) {
//    return Observable.defer(() -> TaskObservable.justNullable(ParseFacebookUtils.linkInBackground(user, permissions, activity)))
//        .map(v -> user);
//  }
//
//  public static Observable<ParseUser> link(ParseUser user, Collection<String> permissions, Activity activity, int activityCode) {
//    return Observable.defer(() -> TaskObservable.justNullable(ParseFacebookUtils.linkInBackground(user, permissions, activity, activityCode)))
//        .map(v -> user);
//  }
//
//  public static Observable<ParseUser> link(ParseUser user, String facebookId, String accessToken, Date expirationDate) {
//    return Observable.defer(() -> TaskObservable.justNullable(ParseFacebookUtils.linkInBackground(user, facebookId, accessToken, expirationDate)))
//        .map(v -> user);
//  }
//
//  public static Observable<ParseUser> logIn(Collection<String> permissions, Activity activity, int activityCode) {
//    return Observable.defer(() -> TaskObservable.justNullable(ParseFacebookUtils.logInInBackground(permissions, activity, activityCode)));
//  }
//
//  public static Observable<ParseUser> logIn(Collection<String> permissions, Activity activity) {
//    // package class com.parse.FacebookAuthenticationProvider.DEFAULT_AUTH_ACTIVITY_CODE
//    // private com.facebook.android.Facebook.DEFAULT_AUTH_ACTIVITY_CODE = 32665
//    return logIn(permissions, activity, 32665);
//  }
//
//  public static Observable<ParseUser> logIn(String facebookId, String accessToken, Date expirationDate) {
//    return Observable.defer(() -> TaskObservable.justNullable(ParseFacebookUtils.logInInBackground(facebookId, accessToken, expirationDate)));
//  }
//
//  public static Observable<ParseUser> saveLatestSessionData(ParseUser user) {
//    return Observable.defer(() -> TaskObservable.justNullable(ParseFacebookUtils.saveLatestSessionDataInBackground(user)))
//        .map(v -> user);
//  }
//
//  public static Observable<ParseUser> unlink(ParseUser user) {
//    return Observable.defer(() -> TaskObservable.justNullable(ParseFacebookUtils.unlinkInBackground(user)))
//        .map(v -> user);
//  }

    /* ParsePush */

  // TODO send(JSONObject data, ParseQuery<ParseInstallation> query)
  // TODO send()
  // TODO sendMessage(String message)

    /* ParseObject */

  // TODO refresh()
  // TODO fetchFromLocalDatastore()

    /* ParseUser */

  // TODO becomeInBackground()
  // TODO enableRevocableSessionInBackground

  public static Observable<ParseUser> logIn(String username, String password) {
    return Observable.defer(() -> TaskObservable.justNullable(ParseUser.logInInBackground(username, password)));
  }

  // TODO logOutInBackground()
  // TODO requestPasswordResetInBackground(String email)
  // TODO signUpInBackground()

}
