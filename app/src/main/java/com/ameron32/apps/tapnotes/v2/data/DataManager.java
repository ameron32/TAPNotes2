package com.ameron32.apps.tapnotes.v2.data;

import com.ameron32.apps.tapnotes.v2.data.frmk.Helper;
import com.ameron32.apps.tapnotes.v2.data.frmk.LocalHelper;
import com.ameron32.apps.tapnotes.v2.data.frmk.RemoteHelper;
import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.parse.ParseHelper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by klemeilleur on 3/24/2016.
 */
@Singleton
public class DataManager {

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
        return localHelper.getNotes()
                .concatMap(new Func1<List<INote>, Observable<List<INote>>>() {
                    @Override
                    public Observable<List<INote>> call(List<INote> iNotes) {
                        return remoteHelper.saveNotes(iNotes);
                    }
                });
    }

    public Observable<List<INote>> getNotes() {
        return remoteHelper.getNotes(Helper.ALL_PROGRAMS, Helper.ALL_TALKS, Helper.FOREVER, Helper.USER_ALL)
                .concatMap(new Func1<List<INote>, Observable<List<INote>>>() {
                    @Override
                    public Observable<List<INote>> call(List<INote> iNotes) {
                        return localHelper.setNotes(iNotes);
                    }
                });
    }

    // Helper method to post events from doOnCompleted.
    private Action0 postEventAction(final Object event) {
        return new Action0() {
            @Override
            public void call() {
//                eventPoster.postEventSafely(event);
            }
        };
    }
}
