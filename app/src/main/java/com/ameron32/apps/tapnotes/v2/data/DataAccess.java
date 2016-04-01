package com.ameron32.apps.tapnotes.v2.data;

import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;

import java.util.List;

import rx.Observable;

/**
 * Created by klemeilleur on 4/1/2016.
 */
public interface DataAccess {

    Observable<List<IProgram>> getPrograms();
    Observable<IProgram> getProgram(String programId);
    Observable<List<ITalk>> getTalks(IProgram program);

    Observable<ITalk> getTalk(String talkId);
    Observable<ITalk> getTalkAtSequence(String sequence);

    Observable<List<INote>> getNotes(IProgram program);
    Observable<List<INote>> getNotes(ITalk talk);

    // TODO: sync smallest set possible
    Observable<List<INote>> syncNotes(IProgram program);
}
