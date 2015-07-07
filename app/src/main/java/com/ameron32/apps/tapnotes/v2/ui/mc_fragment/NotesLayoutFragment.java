package com.ameron32.apps.tapnotes.v2.ui.mc_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.R;

/**
 * Created by Micah on 7/4/2015.
 */
public class NotesLayoutFragment extends Fragment {

    RecyclerView recyclerView;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.insert_notes_layout, container);


    }

    // onDataReceived(ITalk talk, List<INote> notes);


}
