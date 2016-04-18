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


    Observable<INote> createNote(INote note);
    Observable<INote> updateNote(INote note);
    Observable<INote> deleteNote(INote note);

    Observable<List<IObject>> getObjects();
    Observable<List<IProgram>> getPrograms();
    Observable<List<ITalk>> getTalks(IProgram program);
    Observable<List<INote>> getNotes(ITalk talk);

    Observable<IObject> getObject(String objectId);
    Observable<IProgram> getProgram(String programId);
    Observable<ITalk> getTalk(String talkId);
    @Deprecated Observable<ITalk> getTalkAtSequence(String sequence);
    Observable<INote> getNote(String noteId);



    // REMOTE CALLS
    /** sync all remote objects */
    Observable<Progress> syncObjects();

    /** sync all objects in a given scope */
    Observable<Progress> syncObjects(Scope... scopes);

    /** sync all programs */
    Observable<Progress> syncPrograms();

    /** sync program */
    Observable<Progress> syncProgram(String programId);

    /** sync all talks in a program */
    Observable<Progress> syncTalks(IProgram program);

    /** sync all notes for all talks in a program */
    Observable<Progress> syncNotes(IProgram program);

    /** sync all notes in a talk */
    Observable<Progress> syncNotes(ITalk talk);



    /**
     * Set a change to DataManager functionality settings.
     *
     * @param option    The FLAG to describe the setting to modify.
     */
    void setDataManagerSetting(String option);

    /**
     * Listen for the activities of the DataManager. Acts like a log listener
     */
    Observable<Action> getActions();

    /**
     * Trigger to start synchronization service
     */
    void initiateAndroidService();



    interface Progress {

    }

    interface Action {

    }

    interface Scope {

    }
}
