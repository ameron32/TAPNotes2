package com.ameron32.apps.tapnotes.v2.data;

import android.util.Log;

import com.ameron32.apps.tapnotes.v2.data.frmk.Helper;
import com.ameron32.apps.tapnotes.v2.data.frmk.LocalHelper;
import com.ameron32.apps.tapnotes.v2.data.frmk.RemoteHelper;
import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.IObject;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.model.IUser;
import com.ameron32.apps.tapnotes.v2.data.parse.ParseHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Notification;
import rx.Observable;
import rx.Observer;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by klemeilleur on 3/24/2016.
 */
@Singleton
public class DataManager implements DataAccess {

    private static final String TAG = DataManager.class.getSimpleName();

    private enum RemoteSource { Parse, BackendlessSDK, BackendlessREST }
    private enum LocalSource { ParseOffline, SQBrite, Realm, Iron }
    private enum SyncEventType { New }
    /*
     *  THIS IS THE DATA_SOURCE TOGGLE.
     *  SWITCH from PARSE when infrastructure ready to migrate away.
     */
    private static final RemoteSource REMOTE_DATA_SOURCE = RemoteSource.Parse;
    private static final LocalSource LOCAL_DATA_SOURCE = LocalSource.ParseOffline;
    private static final SyncEventType SYNC_EVENT = SyncEventType.New;

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
    private final SyncEvent syncEvent;

    @Inject
    public DataManager() {
        if (REMOTE_DATA_SOURCE == RemoteSource.Parse
                && LOCAL_DATA_SOURCE == LocalSource.ParseOffline) {
            ParseHelper parseHelper = new ParseHelper();
            remoteHelper = parseHelper.getRemote();
            localHelper = parseHelper.getCache();
            syncEvent = parseHelper.getSyncEvent();
            return;
        }

        switch(REMOTE_DATA_SOURCE) {
            case Parse:
                remoteHelper = new ParseHelper().getRemote();
                break;
            case BackendlessSDK:
                remoteHelper = null; // FIXME
                break;
            case BackendlessREST:
                remoteHelper = null; // FIXME
                break;
            default:
                remoteHelper = null; // FIXME
        }
        switch(LOCAL_DATA_SOURCE) {
            case ParseOffline:
                localHelper = new ParseHelper().getCache();
                break;
            case SQBrite:
                localHelper = null; // FIXME
                break;
            case Realm:
                localHelper = null; // FIXME
                break;
            case Iron:
                localHelper = null; // FIXME
                break;
            default:
                localHelper = null; // FIXME
        }
        switch(SYNC_EVENT) {
            case New:
            default:
                syncEvent = new ParseHelper().getSyncEvent(); // FIXME
        }
    }

    @Override
    public Observable<INote> createNote(INote note) {
        return localHelper.createNote(note)
            .concatMap(new Func1<INote, Observable<? extends INote>>() {
                @Override
                public Observable<? extends INote> call(INote note) {
                    return remoteHelper.createNote(note);
                }
            });
    }

    @Override
    public Observable<INote> updateNote(INote note) {
        return localHelper.updateNote(note)
            .concatMap(new Func1<INote, Observable<? extends INote>>() {
                @Override
                public Observable<? extends INote> call(INote note) {
                    return remoteHelper.updateNote(note);
                }
            });
    }

    @Override
    public Observable<INote> deleteNote(INote note) {
        return localHelper.deleteNote(note)
            .concatMap(new Func1<INote, Observable<? extends INote>>() {
                @Override
                public Observable<? extends INote> call(INote note) {
                    return remoteHelper.deleteNote(note);
                }
            });
    }

    @Override
    public Observable<List<IObject>> getObjects() {
        throw new Error("method not ready");
    }

    @Override
    public Observable<List<IProgram>> getPrograms() {
        return localHelper.getPrograms();
    }

    @Override
    public Observable<List<ITalk>> getTalks(IProgram program) {
        return localHelper.getTalks(program);
    }

    @Override
    public Observable<List<INote>> getNotes(ITalk talk) {
        return localHelper.getNotes(null, talk, null, null);
    }

    @Override
    public Observable<IObject> getObject(String objectId) {
        throw new Error("method not ready");
    }

    @Override
    public Observable<IProgram> getProgram(String programId) {
        return localHelper.getProgram(programId);
    }

    @Override
    public Observable<ITalk> getTalk(String talkId) {
        return localHelper.getTalk(talkId);
    }

    @Override
    public Observable<ITalk> getTalkAtSequence(String sequence) {
        throw new Error("Don't use getTalkAtSequence()");
    }

    @Override
    public Observable<INote> getNote(final String noteId) {
        return localHelper.getNote(noteId);
    }

    @Override
    public Observable<List<IObject>> syncObjects() {
        throw new Error("method not ready");
    }

    @Override
    public Observable<List<IObject>> syncObjects(Scope... scopes) {
        throw new Error("method not ready");
    }

    @Override
    public Observable<List<IProgram>> syncPrograms() {
        return remoteHelper.getPrograms()
            .concatMap(new Func1<List<IProgram>, Observable<? extends List<IProgram>>>() {
                @Override
                public Observable<? extends List<IProgram>> call(List<IProgram> programs) {
                    return localHelper.pinPrograms(programs);
                }
            });
    }

    @Override
    public Observable<IProgram> syncProgram(String programId) {
        return remoteHelper.getProgram(programId)
            .concatMap(new Func1<IProgram, Observable<? extends IProgram>>() {
                @Override
                public Observable<? extends IProgram> call(IProgram program) {
                    return localHelper.pinProgram(program);
                }
            });
    }

    @Override
    public Observable<List<ITalk>> syncTalks(IProgram program) {
        return remoteHelper.getTalks(program)
            .concatMap(new Func1<List<ITalk>, Observable<? extends List<ITalk>>>() {
                @Override
                public Observable<? extends List<ITalk>> call(List<ITalk> talks) {
                    return localHelper.pinTalks(talks);
                }
            });
    }

    @Override
    public Observable<List<INote>> syncNotes(IProgram program) {
        return remoteHelper.getNotes(program)
            .concatMap(new Func1<List<INote>, Observable<? extends List<INote>>>() {
                @Override
                public Observable<? extends List<INote>> call(List<INote> notes) {
                    return localHelper.pinNotes(notes);
                }
            });
    }

    @Override
    public Observable<List<INote>> syncNotes(ITalk talk) {
        return remoteHelper.getNotes(talk)
            .concatMap(new Func1<List<INote>, Observable<? extends List<INote>>>() {
                @Override
                public Observable<? extends List<INote>> call(List<INote> notes) {
                    return localHelper.pinNotes(notes);
                }
            });
    }

    @Override
    public void setDataManagerSetting(String option) {

    }

    @Override
    public Observable<Action> getActions() {
        return null;
    }

    @Override
    public void initiateAndroidService() {

    }

    @Override
    public Observable<IUser> getClientUser() {
        return null;
    }

    @Override
    public Observable<IUser> setClientUser() {
        return null;
    }

    //    @Override
//    public Observable<Progress> sync() {
//        // upload all changed objects
//        // wait for server response is finished processing
//        // download all objects
//        // TODO: determine full scope of object sync
//        return Observable.concat(saveLocalChangesToRemote(), getRemoteNotes())
//                .map(new Func1<List<INote>, List<IObject>>() {
//                    @Override
//                    public List<IObject> call(List<INote> iNotes) {
//                        List<IObject> iObjects = new ArrayList<>();
//                        iObjects.addAll(iNotes);
//                        return iObjects;
//                    }
//                });
//    }
//
//    @Override
//    public Observable<List<IProgram>> getPrograms() {
//        return localHelper.getPrograms().distinct();
//    }
//
//    @Override
//    public Observable<List<IProgram>> syncPrograms() {
//        return remoteHelper.getPrograms()
//                .concatMap(new Func1<List<IProgram>, Observable<? extends List<IProgram>>>() {
//                    @Override
//                    public Observable<? extends List<IProgram>> call(List<IProgram> iPrograms) {
//                        return localHelper.pinPrograms(iPrograms);
//                    }
//                });
//    }
//
//    @Override
//    public Observable<IProgram> getProgram(String programId) {
//        return localHelper.getProgram(programId).distinct();
//    }
//
//    @Override
//    public Observable<IProgram> syncProgram(String programId) {
//        return remoteHelper.getProgram(programId)
//                .concatMap(new Func1<IProgram, Observable<? extends IProgram>>() {
//                    @Override
//                    public Observable<? extends IProgram> call(IProgram iProgram) {
//                        return localHelper.pinProgram(iProgram);
//                    }
//                });
//    }
//
//    @Override
//    public Observable<List<ITalk>> getTalks(IProgram program) {
//        return localHelper.getTalks(program).distinct();
//    }
//
//    @Override
//    public Observable<List<ITalk>> syncTalks(IProgram program) {
//        return remoteHelper.getTalks(program).concatMap(new Func1<List<ITalk>, Observable<? extends List<ITalk>>>() {
//            @Override
//            public Observable<? extends List<ITalk>> call(List<ITalk> iTalks) {
//                return localHelper.pinTalks(iTalks);
//            }
//        });
//    }
//
//    @Override
//    public Observable<ITalk> getTalk(String talkId) {
//        return localHelper.getTalk(talkId).distinct();
//    }
//
//    @Override
//    public Observable<ITalk> getTalkAtSequence(String sequence) {
//        return null;
//    }
//
//    @Override
//    public Observable<List<INote>> syncNotes(IProgram program) {
//        return Observable.concat(
//                localHelper.getNotes(program, Helper.ALL_TALKS, Helper.FOREVER, Helper.USER_ALL)
//                        .concatMap(new Func1<List<INote>, Observable<? extends List<INote>>>() {
//                            @Override
//                            public Observable<? extends List<INote>> call(List<INote> iNotes) {
//                                return remoteHelper.saveNotes(iNotes);
//                            }
//                        }),
//                remoteHelper.getNotes(program, Helper.ALL_TALKS, Helper.FOREVER, Helper.USER_ALL)
//                        .concatMap(new Func1<List<INote>, Observable<? extends List<INote>>>() {
//                            @Override
//                            public Observable<? extends List<INote>> call(List<INote> iNotes) {
//                                return localHelper.pinNotes(iNotes);
//                            }
//                        }));
//    }
//
//    @Override
//    public Observable<List<INote>> getNotes(ITalk talk) {
//        return localHelper.getNotes(Helper.ALL_PROGRAMS, talk, Helper.FOREVER, Helper.USER_ALL).distinct();
//    }
//
//    @Override
//    public Observable<List<INote>> syncNotes(ITalk talk) {
//        return Observable.concat(
//                localHelper.getNotes(Helper.ALL_PROGRAMS, talk, Helper.FOREVER, Helper.USER_ALL)
//                        .concatMap(new Func1<List<INote>, Observable<? extends List<INote>>>() {
//                            @Override
//                            public Observable<? extends List<INote>> call(List<INote> iNotes) {
//                                return remoteHelper.saveNotes(iNotes);
//                            }
//                        }),
//                remoteHelper.getNotes(Helper.ALL_PROGRAMS, talk, Helper.FOREVER, Helper.USER_ALL)
//                        .concatMap(new Func1<List<INote>, Observable<? extends List<INote>>>() {
//                            @Override
//                            public Observable<? extends List<INote>> call(List<INote> iNotes) {
//                                return localHelper.pinNotes(iNotes);
//                            }
//                        }));
//    }
//
//    @Override
//    public Observable<INote> deleteNote(INote note) {
//        return localHelper.deleteNote(note).concatMap(new Func1<INote, Observable<? extends INote>>() {
//            @Override
//            public Observable<? extends INote> call(INote iNote) {
//                return remoteHelper.deleteNote(iNote);
//            }
//        });
//    }
//
//    @Override
//    public Observable<IProgram> getProgram(String programId) {
//        return localHelper.getProgram(programId);
//    }
//
//    @Override
//    public Observable<ITalk> getTalk(String talkId) {
//        return localHelper.getTalk(talkId);
//    }
//
//    @Override
//    public Observable<List<INote>> syncNotes(IProgram program) {
//        // TODO: CONFIRM: seems like this calls...
//        // localHelper.getNotes()
//        // then hands those to...
//        // remoteHelper.saveNotes() ...to save
//        // then the next thing to happen is...
//        // remoteHelper.getNotes()
//        // which hands those to...
//        // localHelper.setNotes() ...to store
//        Log.d(TAG, "Observable.concat()");
//        return Observable.concat(saveLocalChangesToRemote(program), getRemoteNotes());
//    }
//
//    @Override
//    public Observable<List<INote>> getNotes(IProgram program) {
//        Log.d(TAG, "Observable.concat()");
//        return Observable.concat(getLocalNotes(program), getRemoteNotes());
//    }
//
//    private Observable<List<INote>> getRemoteNotes() {
//        Log.d(TAG, "remoteHelper.getNotes()");
//        return remoteHelper.getNotes(Helper.ALL_PROGRAMS, Helper.ALL_TALKS, Helper.FOREVER, Helper.USER_GENERIC)
//                .concatMap(new Func1<List<INote>, Observable<? extends List<INote>>>() {
//                    @Override
//                    public Observable<? extends List<INote>> call(List<INote> iNotes) {
//                        Log.d(TAG, "localHelper.setNotes()");
//                        return localHelper.pinNotes(iNotes);
//                    }
//                });
//    }
//
//    private Observable<List<INote>> saveLocalChangesToRemote() {
//        Log.d(TAG, "localHelper.getNotes()");
//        return localHelper.getNotes(Helper.ALL_PROGRAMS, Helper.ALL_TALKS, Helper.FOREVER, Helper.USER_ALL)
//                .concatMap(new Func1<List<INote>, Observable<List<INote>>>() {
//                    @Override
//                    public Observable<List<INote>> call(List<INote> iNotes) {
//                        Log.d(TAG, "remoteHelper.saveNotes()");
//                        return remoteHelper.saveNotes(iNotes);
//                    }
//                });
//    }
//
//    private Observable<List<INote>> getLocalNotes(IProgram program) {
//        Log.d(TAG, "localHelper.getNotes().distinct()");
//        return localHelper.getNotes(program, Helper.ALL_TALKS, Helper.FOREVER, Helper.USER_ALL).distinct();
//    }

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

    public SyncEvent getSyncEvent() { return syncEvent; }
}
