package com.ameron32.apps.tapnotes.v2.ui.mc_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.ui.mc_notes.NotesRecyclerAdapter;

public class NotesLayoutFragment extends Fragment {

    RecyclerView recyclerView;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        recyclerView = (RecyclerView)inflater.inflate(R.layout.insert_notes_layout, container);
        recyclerView.setAdapter(new NotesRecyclerAdapter());
        return recyclerView;

    }


}
