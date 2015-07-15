package com.ameron32.apps.tapnotes.v2.ui.mc_notes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Micah on 7/15/2015.
 */
public class NotesRecycler extends RecyclerView {
    public NotesRecycler(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void itemClicked(View view){

        int size = this.getChildCount();
        for (int i=0; i<size; i++){
            getChildAt(i).setSelected(false);
        }
        view.setSelected(true);

    }
}