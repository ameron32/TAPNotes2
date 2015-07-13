package com.ameron32.apps.tapnotes.v2.ui.mc_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.levelupstudio.recyclerview.ExpandableRecyclerView;

/**
 * Created by Micah on 7/13/2015.
 */
public class ProgramRecycler extends ExpandableRecyclerView {


    public ProgramRecycler(Context context, AttributeSet attrs) {
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




