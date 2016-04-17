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

    Observable<PROGRESS> sync(SCOPES);

    Observable<List<IProgram>> getPrograms();
    Observable<PROGRESS> syncPrograms();

    Observable<IProgram> getProgram(String programId);
    Observable<PROGRESS> syncProgram(String programId);

    Observable<List<ITalk>> getTalks(IProgram program);
    Observable<PROGRESS> syncTalks(IProgram program);

    Observable<ITalk> getTalk(String talkId);
    Observable<ITalk> getTalkAtSequence(String sequence);

    Observable<List<INote>> getNotes(ITalk talk);
    Observable<PROGRESS> syncNotes(IProgram program);
    Observable<PROGRESS> syncNotes(ITalk talk);

    Observable<INote> createNote(INote note);
    Observable<INote> updateNote(INote note);
    Observable<INote> deleteNote(INote note);

    /**
     * Set a change to DataManager functionality settings.
     *
     * @param option    The FLAG to describe the setting to modify.
     */
    void setDataManagerSetting(String option);

    /**
     * Listen for the activities of the DataManager, like a log
     */
    Observable<Action> getActions();

    /**
     * Trigger to start synchronization service
     */
    void initiateAndroidService();
}
