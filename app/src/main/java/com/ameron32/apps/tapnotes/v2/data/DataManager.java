package com.ameron32.apps.tapnotes.v2.data;

import android.util.Log;

import com.ameron32.apps.tapnotes.v2.data.frmk.Helper;
import com.ameron32.apps.tapnotes.v2.data.frmk.LocalHelper;
import com.ameron32.apps.tapnotes.v2.data.frmk.RemoteHelper;
import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.parse.ParseHelper;
import com.ameron32.apps.tapnotes.v2.ui.mc_notes.AbstractDataProvider;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by klemeilleur on 3/24/2016.
 */
@Singleton
public class DataManager {

    private static final String TAG = DataManager.class.getSimpleName();

    private enum RemoteSource { Parse, BackendlessSDK, BackendlessREST }
    private enum LocalSource { ParseOffline, SQBrite, Realm, Iron }
    /*
     *  THIS IS THE DATA_SOURCE TOGGLE.
     *  SWITCH from PARSE when infrastructure ready to migrate away.
     */
    private static final RemoteSource REMOTE_DATA_SOURCE = RemoteSource.Parse;
    private static final LocalSource LOCAL_DATA_SOURCE = LocalSource.ParseOffline;

    //
    /**
     *  DEVELOPMENT NOTES:
     *  see https://github.com/ribot/android-boilerplate/blob/master/app/src/main/java/uk/co/ribot/androidboilerplate/data/DataManager.java
     *
     *  Options:
     *  - SharedPreferences
     *  - Local SQLite Database
     *  - Realm Object Storage
     *  - Offline-mode from 3rd-party service
     *
     *  Use ParseHelper to call Queries.* and Commands.*
     *  Migrate Rx to DataManager as much as possible.
     */
    //

    private final RemoteHelper remoteHelper;
    private final LocalHelper localHelper;

    @Inject
    public DataManager() {
        if (REMOTE_DATA_SOURCE == RemoteSource.Parse
                && LOCAL_DATA_SOURCE == LocalSource.ParseOffline) {
            ParseHelper parseHelper = new ParseHelper();
            remoteHelper = parseHelper;
            localHelper = parseHelper;
            return;
        }

        switch(REMOTE_DATA_SOURCE) {
            case Parse:
                remoteHelper = new ParseHelper();
                break;
            case BackendlessSDK:
                remoteHelper = new ParseHelper(); // FIXME
                break;
            case BackendlessREST:
                remoteHelper = new ParseHelper(); // FIXME
                break;
            default:
                remoteHelper = new ParseHelper(); // FIXME
        }
        switch(LOCAL_DATA_SOURCE) {
            case ParseOffline:
                localHelper = new ParseHelper();
                break;
            case SQBrite:
                localHelper = new ParseHelper(); // FIXME
                break;
            case Realm:
                localHelper = new ParseHelper(); // FIXME
                break;
            case Iron:
                localHelper = new ParseHelper(); // FIXME
                break;
            default:
                localHelper = new ParseHelper(); // FIXME
        }
    }



    public Observable<List<INote>> syncNotes() {
        // TODO: CONFIRM: seems like this calls...
        // localHelper.getNotes()
        // then hands those to...
        // remoteHelper.saveNotes() ...to save
        // then the next thing to happen is...
        // remoteHelper.getNotes()
        // which hands those to...
        // localHelper.setNotes() ...to store
        Log.d(TAG, "Observable.concat()");
        return Observable.concat(saveLocalChangesToRemote(), getRemoteNotes());
    }

    public Observable<List<INote>> getNotes() {
        Log.d(TAG, "Observable.concat()");
        return Observable.concat(getLocalNotes(), getRemoteNotes());
    }

    private Observable<List<INote>> getRemoteNotes() {
        Log.d(TAG, "remoteHelper.getNotes()");
        return remoteHelper.getNotes(Helper.ALL_PROGRAMS, Helper.ALL_TALKS, Helper.FOREVER, Helper.USER_GENERIC)
                .concatMap(new Func1<List<INote>, Observable<? extends List<INote>>>() {
                    @Override
                    public Observable<? extends List<INote>> call(List<INote> iNotes) {
                        Log.d(TAG, "localHelper.setNotes()");
                        return localHelper.setNotes(iNotes);
                    }
                });
    }

    private Observable<List<INote>> saveLocalChangesToRemote() {
        Log.d(TAG, "localHelper.getNotes()");
        return localHelper.getNotes()
                .concatMap(new Func1<List<INote>, Observable<List<INote>>>() {
                    @Override
                    public Observable<List<INote>> call(List<INote> iNotes) {
                        Log.d(TAG, "remoteHelper.saveNotes()");
                        return remoteHelper.saveNotes(iNotes);
                    }
                });
    }

    private Observable<List<INote>> getLocalNotes() {
        Log.d(TAG, "localHelper.getNotes().distinct()");
        return localHelper.getNotes().distinct();
    }

    // Helper method to post events from doOnCompleted.
    private Action0 postEventAction(final Object event) {
        return new Action0() {
            @Override
            public void call() {
                // TODO: method
//                eventPoster.postEventSafely(event);
            }
        };
    }
}
