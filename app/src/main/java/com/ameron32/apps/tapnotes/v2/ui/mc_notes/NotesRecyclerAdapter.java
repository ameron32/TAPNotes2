package com.ameron32.apps.tapnotes.v2.ui.mc_notes;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.ui.delegate.INotesDelegate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
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

    public NotesRecyclerAdapter(Context context) {
        this.mContext = context;
        mProvider = new NoteDataProvider();
        setHasStableIds(true);
    }

    public void addINotesDelegateCallbacks(INotesDelegateCallbacks callback) {
        mCallback = callback;
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
        setOnClickListener(v);
        setLongPressListener(v);
        setonTouch(v);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {

        INote note = mProvider.getItem(position);
        holder.notesTextView.setText(note.getNoteText());
        holder.noteLayout.setTag(note);
        setOnClickListener(holder.noteLayout);

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
                mCallback.onUserClickEditNote(((INote) v.getTag()));
            }
        });
    }

    private void setLongPressListener(View v) {
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
                    ((NotesRecycler)v.getParent()).itemClicked(v);
                }

                final Dialog dialog = new Dialog(mContext, R.style.CustomDialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.dimAmount=0.0f;
                lp.gravity = Gravity.CENTER_HORIZONTAL;
                lp.y = lastY;   //y position
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.getWindow().setAttributes(lp);
                dialog.setContentView(R.layout.context_menu_layout);
                ButterKnife.inject(this, v);

                dialog.show();


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


                            //lastY = (int) ev.getY();
                            lastY=y + (int)(mContext.getResources().getDimension(R.dimen.note_row_height_min));
                        break;
                    }
                }
                return false;
            }
        });
    }
}
