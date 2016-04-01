package com.ameron32.apps.tapnotes.v2.data.frmk;

import com.ameron32.apps.tapnotes.v2.data.model.INote;
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

    Observable<List<IProgram>> getPrograms();
    Observable<List<IProgram>> pinPrograms(List<IProgram> programs);

    Observable<IProgram> getProgram(String programId);
    Observable<IProgram> pinProgram(IProgram program);

    Observable<ITalk> getTalk(String talkId);
    Observable<ITalk> pinTalk(ITalk talk);
    Observable<ITalk> getTalkAtSequence(String sequencePosition);

    Observable<List<ITalk>> getProgramTalks(IProgram iProgram);
    Observable<List<ITalk>> pinTalks(List<ITalk> talks);

    Observable<List<INote>> getNotes(final IProgram program, final ITalk talk, final DateTime date, final IUser user);
    Observable<List<INote>> pinNotes(List<INote> notes);
}
