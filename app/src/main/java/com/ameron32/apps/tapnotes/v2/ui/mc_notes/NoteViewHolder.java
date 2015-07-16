package com.ameron32.apps.tapnotes.v2.ui.mc_notes;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Micah on 7/6/2015.
 */
public class NoteViewHolder extends AbstractDraggableItemViewHolder {

    @InjectView(R.id.contextmenu)
    public LinearLayout popup;

    @InjectView(R.id.noteLayout)
    public RelativeLayout noteLayout;
    @InjectView(R.id.notesTextView)
    public TextView notesTextView;



    public NoteViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
        popup.setVisibility(View.GONE);
    }
}
