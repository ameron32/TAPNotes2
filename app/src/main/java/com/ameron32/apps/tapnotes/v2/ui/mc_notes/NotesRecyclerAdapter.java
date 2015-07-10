package com.ameron32.apps.tapnotes.v2.ui.mc_notes;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.R;


/**
 * Created by Micah on 7/4/2015.
 */
public class NotesRecyclerAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private static final String TAG = "NotesRecyclerAdapter";
    public static final int offsetToStartDrag = 100;

    private NoteDataProvider mProvider;

   public NotesRecyclerAdapter(){
       mProvider = new NoteDataProvider();
       setHasStableIds(true);
   }


    @Override
    public long getItemId(int position) {
        return mProvider.getItem(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return mProvider.getItem(position).getViewType();
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
}
