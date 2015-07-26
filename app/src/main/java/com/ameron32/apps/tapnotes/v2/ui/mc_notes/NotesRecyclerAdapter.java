package com.ameron32.apps.tapnotes.v2.ui.mc_notes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.model.IBible;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.ui.delegate.INotesDelegate;
import com.ameron32.apps.tapnotes.v2.ui.renderer.ScriptureSpanRenderer;
import com.jmpergar.awesometext.AwesomeTextHandler;

import java.util.LinkedList;
import java.util.List;

import butterknife.InjectView;


/**
 * Created by Micah on 7/4/2015.
 */
public class NotesRecyclerAdapter extends RecyclerView.Adapter<NoteViewHolder> implements INotesDelegate {

    private static final String TAG = "NotesRecyclerAdapter";
    public static final int offsetToStartDrag = 100;
    private final Context mContext;
    int lastX;
    int lastY;

    private INotesDelegateCallbacks mCallback;

    private NoteDataProvider mProvider;

    @Override
    public void onBibleCreated(IBible bible) {

    }

    public NotesRecyclerAdapter(Context context) {
        this.mContext = context;
        mProvider = new NoteDataProvider();
        setHasStableIds(true);
    }

    public void addINotesDelegateCallbacks(INotesDelegateCallbacks callback) {
        mCallback = callback;
    }

    public INotesDelegateCallbacks getmCallback(){return mCallback;}

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
        setOnClickListener(v);
        setonTouch(v);
        NoteViewHolder holder = new NoteViewHolder(v);
        setLongPressListener(v, holder.popup);
        return holder;
    }

    private static final String SCRIPTURE_PATTERN = "@\\<\\<!\\<[\\w|\\,|:|\\-|\\s]+\\>!\\>\\>";

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {

        INote note = mProvider.getItem(position);
        //holder.notesTextView.setText(note.getNoteText());
        holder.noteLayout.setTag(R.string.holdertag, holder);
        holder.noteLayout.setTag(R.string.notetag, note);
        setOnClickListener(holder.noteLayout);

        AwesomeTextHandler ath = new AwesomeTextHandler();
        ath.addViewSpanRenderer(SCRIPTURE_PATTERN, new ScriptureSpanRenderer())
            .setView(holder.notesTextView);
        ath.setText(note.getNoteText());
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
        notifyDataSetChanged();

    }

    @Override
    public void addNotes(List<INote> notesToAdd) {
        for (INote note : notesToAdd)
            mProvider.addNote(note);
        notifyDataSetChanged();

    }

    @Override
    public void removeNotes(List<INote> notesToRemove) {

        for (INote note : notesToRemove) {
            mProvider.removeItem(note);
            notifyDataSetChanged();
        }

    }


    @Override
    public void replaceNotes(List<INote> notesToReplace) {
        mProvider.replaceNotes(notesToReplace);
        notifyDataSetChanged();
    }

    private void setOnClickListener(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getParent() instanceof NotesRecycler) {
                    NotesRecycler nr = (NotesRecycler) v.getParent();
                    nr.itemClicked(v);
                }
                mCallback.onUserClickEditNote(((INote) v.getTag(R.string.notetag)));
            }
        });
    }

    private void setLongPressListener(View v, View popup) {
        v.setOnLongClickListener(new View.OnLongClickListener() {


            @InjectView(R.id.repos_button)
            ImageView reposButton;
            @InjectView(R.id.bold_button)
            ImageView boldButton;
            @InjectView(R.id.important_button)
            ImageView importantButton;
            @InjectView(R.id.edit_button)
            ImageView editButton;
            @InjectView(R.id.delete_button)
            ImageView deleteButton;


            @Override
            public boolean onLongClick(View v) {

                if (v.getParent() instanceof NotesRecycler){
                    NotesRecycler nr = ((NotesRecycler) v.getParent());
                    nr.itemClicked(v);
                    popup.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });
    }

    private void setonTouch(View v) {
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                final int action = ev.getAction();
                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: {
                        int y=0;
                        if (v.getParent() instanceof NotesRecycler){
                            NotesRecycler nr = (NotesRecycler)v.getParent();
                            y=nr.indexOfChild(v)* (int)(mContext.getResources().getDimension(R.dimen.note_row_height_min));
                        }
                            lastY = y-200;
                        break;
                    }
                }
                return false;
            }
        });
    }
}
