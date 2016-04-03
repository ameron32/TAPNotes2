package com.ameron32.apps.tapnotes.v2.data;

import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.IObject;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;

import java.util.List;

import rx.Observable;

/**
 * Created by klemeilleur on 4/1/2016.
 */
public interface DataAccess {

    Observable<List<IObject>> performSync();

    Observable<List<IProgram>> getPrograms();
    Observable<List<IProgram>> syncPrograms();

    Observable<IProgram> getProgram(String programId);
    Observable<IProgram> syncProgram(String programId);

    Observable<List<ITalk>> getTalks(IProgram program);
    Observable<List<ITalk>> syncTalks(IProgram program);

    Observable<ITalk> getTalk(String talkId);
    Observable<ITalk> getTalkAtSequence(String sequence);

    Observable<List<INote>> getNotes(ITalk talk);
    Observable<List<INote>> syncNotes(IProgram program);
    Observable<List<INote>> syncNotes(ITalk talk);

    Observable<INote> deleteNote(INote note);
}
