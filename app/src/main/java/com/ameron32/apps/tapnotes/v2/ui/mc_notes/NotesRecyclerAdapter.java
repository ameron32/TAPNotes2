package com.ameron32.apps.tapnotes.v2.ui.mc_notes;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.ui.delegate.INotesDelegate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Micah on 7/4/2015.
 */
public class NotesRecyclerAdapter extends RecyclerView.Adapter<NoteViewHolder> implements INotesDelegate {

    private static final String TAG = "NotesRecyclerAdapter";
    public static final int offsetToStartDrag = 100;

    private ArrayList<INotesDelegateCallbacks> mCallbacks;

    private NoteDataProvider mProvider;

   public NotesRecyclerAdapter(){
       mProvider = new NoteDataProvider();
       setHasStableIds(true);
       mCallbacks = new ArrayList<>();
   }

    public void addINotesDelegateCallbacks(INotesDelegateCallbacks callback){
        mCallbacks.add(callback);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.insert_notes_list_item, parent, false);

        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {

        DummyNote note = (DummyNote)mProvider.getItem(position);
        holder.notesTextView.setText(note.getText());

    }

    @Override
    public int getItemCount() {
        return mProvider.getCount();
    }




    //INotesDelegate methods:
    @Override
    public void synchronizeNotes(List<INote> allNotes) {
        LinkedList<INote> ll = new LinkedList<INote>(allNotes);
        mProvider.populateWithExistingNotes(ll);
    }

    @Override
    public void addNotes(List<INote> notesToAdd) {
        for (INote note:notesToAdd)
        mProvider.addNote(note);
    }

    @Override
    public void removeNotes(List<INote> notesToRemove) {

        for (INote note:notesToRemove){
            mProvider.removeItem(note);
        }

    }

    @Override
    public void replaceNotes(List<INote> notesToReplace) {

    }
}
