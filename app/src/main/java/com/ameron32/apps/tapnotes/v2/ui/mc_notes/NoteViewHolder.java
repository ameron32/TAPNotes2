package com.ameron32.apps.tapnotes.v2.ui.mc_notes;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.ui.view.ExpandableTextView;
import com.ameron32.apps.tapnotes.v2.ui.view.ExpandableTextView2;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Micah on 7/6/2015.
 */
public class NoteViewHolder extends AbstractDraggableItemViewHolder {

    @InjectView(R.id.contextmenu)
    public RelativeLayout popup;
    
    @InjectView(R.id.noteLayout)
    public FrameLayout noteLayout;

    @InjectView(R.id.notesTextView)
    public TextView notesTextView;

    @InjectView(R.id.bold_button)
    public ImageView boldButton;

    @InjectView(R.id.important_button)
    public ImageView importantButton;

    @InjectView(R.id.repos_button)
    public ImageView reposButton;

    @InjectView(R.id.edit_button)
    public ImageView editButton;

    @InjectView(R.id.delete_button)
    public ImageView deleteButton;

//    @InjectView(R.id.appendTextView)
//    public TextView appendTextView;

    @InjectView(R.id.expand_text_view)
    ExpandableTextView2 appendTextView;


    public NoteViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
        popup.setVisibility(View.GONE);
        setListeners();
    }
    
    private void setListeners(){

        boldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getParent().getParent().getParent().getParent() instanceof NotesRecycler){
                    NotesRecycler nr = (NotesRecycler)v.getParent().getParent().getParent().getParent();
                    INote note = (INote) noteLayout.getTag(R.string.notetag);
                    ((NotesRecyclerAdapter)nr.getAdapter()).getmCallback().onUserClickBoldNote(note);
                    nr.getAdapter().notifyDataSetChanged();
                    popup.setVisibility(View.INVISIBLE);

                }
            }
        });

        importantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getParent().getParent().getParent().getParent() instanceof NotesRecycler){
                    NotesRecycler nr = (NotesRecycler)v.getParent().getParent().getParent().getParent();
                    INote note = (INote) noteLayout.getTag(R.string.notetag);
                    ((NotesRecyclerAdapter)nr.getAdapter()).getmCallback().onUserClickImportantNote(note);
                    nr.getAdapter().notifyDataSetChanged();
                    popup.setVisibility(View.INVISIBLE);

                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getParent().getParent().getParent().getParent() instanceof NotesRecycler){
                    NotesRecycler nr = (NotesRecycler)v.getParent().getParent().getParent().getParent();
                    INote note = (INote) noteLayout.getTag(R.string.notetag);
                    ((NotesRecyclerAdapter)nr.getAdapter()).getmCallback().onUserClickDeleteNote(note);
                    nr.getAdapter().notifyDataSetChanged();
                    popup.setVisibility(View.INVISIBLE);

                }
            }
        });
    }
}
