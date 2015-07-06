package com.ameron32.apps.tapnotes.v2.ui.mc_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.ui.mc_adapter.DummyProgramListAdapter;

/**
 * Created by Micah on 7/3/2015.
 */
public class ProgramLayoutFragment extends Fragment {

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        com.levelupstudio.recyclerview.ExpandableRecyclerView erv = (com.levelupstudio.recyclerview.ExpandableRecyclerView) inflater.inflate(R.layout.insert_program_layout, container);
        erv.setExpandableAdapter(new DummyProgramListAdapter(this.getActivity()));
        erv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        return erv;

    }

}
