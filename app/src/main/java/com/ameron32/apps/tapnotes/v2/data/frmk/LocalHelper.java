package com.ameron32.apps.tapnotes.v2.data.frmk;

import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.IObject;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.model.IUser;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Program;

import org.joda.time.DateTime;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by klemeilleur on 3/24/2016.
 */
public interface LocalHelper extends Helper {

    Observable<INote> createNote(INote note);
    Observable<INote> updateNote(INote note);
    Observable<INote> deleteNote(INote note);

    Observable<List<IProgram>> getPrograms();
    Observable<List<ITalk>> getTalks(IProgram iProgram);
    Observable<List<INote>> getNotes(final IProgram program, final ITalk talk, final DateTime date, final IUser user);

    Observable<IProgram> getProgram(String programId);
    Observable<ITalk> getTalk(String talkId);
    @Deprecated Observable<ITalk> getTalkAtSequence(String sequencePosition);
    Observable<INote> getNote(String noteId);

    Observable<List<IProgram>> pinPrograms(List<IProgram> programs);
    Observable<List<ITalk>> pinTalks(List<ITalk> talks);
    Observable<List<INote>> pinNotes(List<INote> notes);

    Observable<IProgram> pinProgram(IProgram program);
    Observable<ITalk> pinTalk(ITalk talk);
    Observable<INote> pinNote(INote note);

    Observable<List<IObject>> getUnsyncedObjects();
}
