package com.ameron32.apps.tapnotes.v2.data.parse;

import com.ameron32.apps.tapnotes.v2.data.frmk.LocalHelper;
import com.ameron32.apps.tapnotes.v2.data.frmk.RemoteHelper;
import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.IObject;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.model.IUser;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Note;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.joda.time.DateTime;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;

/**
 * Created by klemeilleur on 4/18/16.
 */
public class ParseHelper {

    public final ParseLocalHelper cache;
    public final ParseRemoteHelper remote;
    public final ParseSyncEvent syncEvent;

    public ParseHelper() {
        cache = new ParseLocalHelper();
        remote = new ParseRemoteHelper();
        syncEvent = new ParseSyncEvent();
    }

    class ParseLocalHelper implements LocalHelper {

        @Override
        public Observable<INote> createNote(INote note) {
            return null;
        }

        @Override
        public Observable<INote> updateNote(INote note) {
            return null;
        }

        @Override
        public Observable<INote> deleteNote(INote note) {
            return null;
        }

        @Override
        public Observable<List<IProgram>> getPrograms() {
            return null;
        }

        @Override
        public Observable<List<ITalk>> getProgramTalks(IProgram iProgram) {
            return null;
        }

        @Override
        public Observable<List<INote>> getNotes(IProgram program, ITalk talk, DateTime date, IUser user) {
            return null;
        }

        @Override
        public Observable<IProgram> getProgram(String programId) {
            return null;
        }

        @Override
        public Observable<ITalk> getTalk(String talkId) {
            return null;
        }

        @Override
        public Observable<ITalk> getTalkAtSequence(String sequencePosition) {
            throw new Error("do not use getTalkAtSequence()");
        }

        @Override
        public Observable<INote> getNote(String noteId) {
            return null;
        }

        @Override
        public Observable<List<IProgram>> pinPrograms(List<IProgram> programs) {
            return null;
        }

        @Override
        public Observable<List<ITalk>> pinTalks(List<ITalk> talks) {
            return null;
        }

        @Override
        public Observable<List<INote>> pinNotes(List<INote> notes) {
            return null;
        }

        @Override
        public Observable<IProgram> pinProgram(IProgram program) {
            return null;
        }

        @Override
        public Observable<ITalk> pinTalk(ITalk talk) {
            return null;
        }

        @Override
        public Observable<INote> pinNote(INote note) {
            return null;
        }

        @Override
        public Observable<List<IObject>> getUnsyncedObjects() {
            return null;
        }
    }

    class ParseRemoteHelper implements RemoteHelper {

        @Override
        public Observable<INote> createNote(INote note) {
            return null;
        }

        @Override
        public Observable<INote> updateNote(INote note) {
            return null;
        }

        @Override
        public Observable<INote> deleteNote(INote note) {
            return null;
        }

        @Override
        public Observable<List<IObject>> getObjects() {
            return null;
        }

        @Override
        public Observable<List<IProgram>> getPrograms() {
            return null;
        }

        @Override
        public Observable<List<ITalk>> getTalks(IProgram program) {
            return null;
        }

        @Override
        public Observable<List<INote>> getNotes(IProgram program) {
            return null;
        }

        @Override
        public Observable<List<INote>> getNotes(ITalk talk) {
            return null;
        }

        @Override
        public Observable<IObject> getObject(String objectId) {
            return null;
        }

        @Override
        public Observable<IProgram> getProgram(String programId) {
            return null;
        }

        @Override
        public Observable<ITalk> getTalk(String talkId) {
            return null;
        }

        @Override
        public Observable<ITalk> getTalkAtSequence(String sequence) {
            throw new Error("do not use getTalkAtSequence()");
        }

        @Override
        public Observable<INote> getNote(final String noteId) {
            return Observable.defer(new Func0<Observable<INote>>() {
                @Override
                public Observable<INote> call() {
                    return Observable.create(new Observable.OnSubscribe<INote>() {
                        @Override
                        public void call(Subscriber<? super INote> subscriber) {
                            try {
                                subscriber.onNext(ParseQuery.getQuery(Note.class).get(noteId));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            subscriber.onNext(null);
                        }
                    });
                }
            });
        }

        @Override
        public Observable<List<INote>> saveNotes(final List<INote> iNotes) {
            return Observable.defer(new Func0<Observable<List<INote>>>() {
                @Override
                public Observable<List<INote>> call() {
                    return Observable.create(new Observable.OnSubscribe<List<INote>>() {
                        @Override
                        public void call(Subscriber<? super List<INote>> subscriber) {
                            try {
                                Note.saveAll(ParseUtil.fromINote(iNotes));
                                subscriber.onNext(iNotes);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            subscriber.onNext(null);
                        }
                    });
                }
            });
        }
    }
}
