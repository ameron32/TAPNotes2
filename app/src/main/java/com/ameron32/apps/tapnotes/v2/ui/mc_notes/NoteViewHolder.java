package com.ameron32.apps.tapnotes.v2.ui.mc_notes;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import butterknife.InjectView;

/**
 * Created by Micah on 7/6/2015.
 */
public class NoteViewHolder extends AbstractDraggableItemViewHolder {

    @InjectView (R.id.container)
    public FrameLayout mContainer;
    public TextView textView;

    public NoteViewHolder(View itemView) {
        super(itemView);

    }
}
