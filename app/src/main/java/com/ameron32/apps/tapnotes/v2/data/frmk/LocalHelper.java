package com.ameron32.apps.tapnotes.v2.data.frmk;

import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.model.IUser;

import org.joda.time.DateTime;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by klemeilleur on 3/24/2016.
 */
public interface LocalHelper extends Helper {

    Observable<List<INote>> clearNotes(IProgram program, ITalk talk, DateTime date, IUser user);
    Observable<List<INote>> setNotes(final Collection<INote> newNotes);
    Observable<List<INote>> getLocalNotes(final IProgram program, final ITalk talk, final DateTime date, final IUser user);
}
