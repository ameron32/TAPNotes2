package com.ameron32.apps.tapnotes.v2.data.frmk;

import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.IObject;
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

    Observable<INote> createNote(INote note);
    Observable<INote> updateNote(INote note);
    Observable<INote> deleteNote(INote note);

    Observable<List<IObject>> getObjects();
    Observable<List<IProgram>> getPrograms();
    Observable<List<ITalk>> getTalks(IProgram program);
    Observable<List<INote>> getNotes(IProgram program);
    Observable<List<INote>> getNotes(ITalk talk);

    Observable<IObject> getObject(String objectId);
    Observable<IProgram> getProgram(String programId);
    Observable<ITalk> getTalk(String talkId);
    @Deprecated Observable<ITalk> getTalkAtSequence(String sequence);
    Observable<INote> getNote(String noteId);

    Observable<List<INote>> saveNotes(List<INote> iNotes);
}
