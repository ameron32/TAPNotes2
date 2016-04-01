package com.ameron32.apps.tapnotes.v2.data.frmk;

import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.model.IUser;

import org.joda.time.DateTime;

import java.util.List;

import rx.Observable;

/**
 * Created by klemeilleur on 3/24/2016.
 */
public interface RemoteHelper extends Helper {

    Observable<List<IProgram>> getPrograms();
    Observable<IProgram> getProgram(String programId);
    Observable<List<ITalk>> getTalks(IProgram program);
    Observable<List<INote>> getNotes(IProgram program, ITalk talk, DateTime date, IUser user);
    Observable<List<INote>> getProgramNotes(IProgram program);

    Observable<List<INote>> saveNotes(List<INote> iNotes);

    Observable<INote> deleteNote(INote note);
}
