package com.ameron32.apps.tapnotes.v2.ui.mc_notes;

import com.ameron32.apps.tapnotes.v2.model.INote;

import java.util.LinkedList;
import java.util.List;

public class NoteDataProvider extends AbstractDataProvider {
    private List<INote> mData;
    private INote mLastRemovedData;
    private int mLastRemovedPosition = -1;

    public NoteDataProvider() {
        final String atoz = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        mData = new LinkedList<>();

    }

    public void addNote(INote note){
        mData.add(note);
    }

    public void populateWithExistingNotes(LinkedList<INote> notes){
        mData = notes;
        //TODO - Make sure Kris hands a list of notes with the appropriate data.
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public INote getItem(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("index = " + index);
        }

        return (mData.get(index));
    }

    @Override
    public int undoLastRemoval() {
        if (mLastRemovedData != null) {
            int insertedPosition;
            if (mLastRemovedPosition >= 0 && mLastRemovedPosition < mData.size()) {
                insertedPosition = mLastRemovedPosition;
            } else {
                insertedPosition = mData.size();
            }

            mData.add(insertedPosition, mLastRemovedData);

            mLastRemovedData = null;
            mLastRemovedPosition = -1;

            return insertedPosition;
        } else {
            return -1;
        }
    }

    @Override
    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        final INote item = mData.remove(fromPosition);

        mData.add(toPosition, item);
        mLastRemovedPosition = -1;
    }

    public void removeItem(INote note){
        mData.remove(note);
    }

    @Override
    public void removeItem(int position) {
        //noinspection UnnecessaryLocalVariable
        final INote removedItem = mData.remove(position);

        mLastRemovedData = removedItem;
        mLastRemovedPosition = position;
    }


    public void replaceNotes(List<INote> notesToReplace){
        for (INote note:mData){
            for (INote noteToReplace:notesToReplace){
                //TODO - Notes need an ID field?  Would like to do the following:
                //if (note.getID() == noteToReplace.getID()){
                // note = noteToReplace;
                // }
            }
        }
    }
}
