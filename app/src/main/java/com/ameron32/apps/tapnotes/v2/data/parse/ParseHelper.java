package com.ameron32.apps.tapnotes.v2.data.parse;

import com.ameron32.apps.tapnotes.v2.data.frmk.LocalHelper;
import com.ameron32.apps.tapnotes.v2.data.frmk.RemoteHelper;
import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.model.IUser;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Program;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Talk;
import com.parse.ParseException;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by klemeilleur on 3/25/2016.
 */
public class ParseHelper implements LocalHelper, RemoteHelper {

    /**
     *  TODO: REVIEW Queries.* usage to determine if I'm using the wrong ones.
     */

    public ParseHelper() {

    }

    // ------------------------------------------
    // LOCAL

    @Override
    public Observable<List<INote>> clearNotes(final IProgram program, final ITalk talk, final DateTime date, final IUser user) {
        return Observable.create(new Observable.OnSubscribe<List<INote>>() {
            @Override
            public void call(Subscriber<? super List<INote>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                List<INote> iNotes = unpinNotesAsync(program, talk, date, user);
                subscriber.onNext(iNotes);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<INote>> setNotes(Collection<INote> newNotes) {
        // TODO method
        return Observable.create(new Observable.OnSubscribe<List<INote>>() {
            @Override
            public void call(Subscriber<? super List<INote>> subscriber) {

            }
        });
    }

    @Override
    public Observable<List<INote>> getLocalNotes(final IProgram program, final ITalk talk, final DateTime date, final IUser user) {
        // TODO method
        return Observable.create(new Observable.OnSubscribe<List<INote>>() {
            @Override
            public void call(Subscriber<? super List<INote>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                List<INote> iNotes = getNotesAsync(program, talk, date, user);
                subscriber.onNext(iNotes);
                subscriber.onCompleted();
            }
        });
    }

    // ------------------------------------------
    // REMOTE

    @Override
    public Observable<IProgram> getProgram(final String programId) {
        return Observable.create(new Observable.OnSubscribe<IProgram>() {
            @Override
            public void call(Subscriber<? super IProgram> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                IProgram iProgram = pinProgramAsync(programId);
                subscriber.onNext(iProgram);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<ITalk>> getTalks(final IProgram program) {
        return Observable.create(new Observable.OnSubscribe<List<ITalk>>() {
            @Override
            public void call(Subscriber<? super List<ITalk>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                List<ITalk> talks = pinTalksAsync(program);
                subscriber.onNext(talks);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<INote>> getNotes(final IProgram program, final ITalk talk, final DateTime date, final IUser user) {
        return Observable.create(new Observable.OnSubscribe<List<INote>>() {
            @Override
            public void call(Subscriber<? super List<INote>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                List<INote> iNotes = pinNotesAsync(program, talk, date, user);
                subscriber.onNext(iNotes);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<INote>> saveNotes(final List<INote> iNotes) {
        return Observable.create(new Observable.OnSubscribe<List<INote>>() {
            @Override
            public void call(Subscriber<? super List<INote>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                saveNotesAsync(iNotes);
            }
        });
    }

    private IProgram pinProgramAsync(String programId) {
        try {
            return Queries.Live.pinProgram(programId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<ITalk> pinTalksAsync(IProgram program) {
        if (program instanceof Program) {
            try {
                return Queries.Live.pinAllProgramTalksFor((Program) program);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private List<INote> pinNotesAsync(IProgram iProgram, ITalk iTalk, DateTime dateTime, IUser iUser) {
        try {
            if (iProgram instanceof Program) {
                Program program = (Program) iProgram;
                Date date = null;
                Talk talk = (iTalk != null && iTalk instanceof Talk) ? (Talk) iTalk : null;
                if (dateTime != FOREVER) {
                    date = dateTime.toDate();
                }

                if (iUser == USER_GENERIC) {
                    if (iTalk == ALL_TALKS) {
                        return Queries.Live.pinAllGenericNotesFor(program, date);
                    } else {
                        return Queries.Live.pinAllGenericNotesFor(program, talk, date);
                    }
                }

                if (iUser == USER_ME) {
                    if (iTalk == ALL_TALKS) {
                        return Queries.Live.pinAllClientOwnedNotesFor(program, date);
                    } else {
                        return Queries.Live.pinAllClientOwnedNotesFor(program, talk, date);
                    }
                }

                if (iUser == USER_ALL) {
                    throw new IllegalStateException("FIXME");
                }
                return null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<INote> getNotesAsync(IProgram iProgram, ITalk iTalk, DateTime dateTime, IUser iUser) {
        try {
            if (iProgram instanceof Program) {
                Program program = (Program) iProgram;
                Date date = null;
                Talk talk = (iTalk != null && iTalk instanceof Talk) ? (Talk) iTalk : null;
                if (dateTime != FOREVER) {
                    date = dateTime.toDate();
                }

                if (iUser == USER_ALL) {
                    throw new IllegalStateException("FIXME");
                } else {
                    if (talk != ALL_TALKS) {
                        List<INote> results = new ArrayList<>();
                        results.addAll(Queries.Local.findGenericNotesFor(talk));
                        results.addAll(Queries.Local.findClientOwnedNotesFor(talk));
                        return results;
                    }
                }

                return null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<INote> saveNotesAsync(List<INote> iNotes) {
        return ParseUtil.toINote(
                Commands.Live.saveNotesNow(
                        ParseUtil.fromINote(iNotes)));
    }

    private List<INote> unpinNotesAsync(IProgram iProgram, ITalk iTalk, DateTime dateTime, IUser iUser) {
        try {
            if (iProgram instanceof Program) {
                Program program = (Program) iProgram;
                Date date = (dateTime != FOREVER) ? dateTime.toDate() : null;
                Talk talk = (iTalk != null && iTalk instanceof Talk) ? (Talk) iTalk : null;
                if (dateTime != FOREVER) {
                    date = dateTime.toDate();
                }

                if (iUser == USER_GENERIC) {
                    return Queries.Local.unpinAllGenericNotesFor(program);
                }

                if (iUser == USER_ME) {
                    if (iTalk == ALL_TALKS) {
                        return Queries.Local.unpinAllClientOwnedNotesFor(program, date);
                    } else {
                        return Queries.Local.unpinAllClientOwnedNotesFor(program, talk, date);
                    }
                }

                if (iUser == USER_ALL) {
                    throw new IllegalStateException("FIXME");
                }
                return null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
