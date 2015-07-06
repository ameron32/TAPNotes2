package com.ameron32.apps.tapnotes.v2.ui.mc_adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.R;
import com.levelupstudio.recyclerview.ExpandableRecyclerView;

import java.util.ArrayList;


public class DummyProgramListAdapter extends ExpandableRecyclerView.ExpandableAdapter{


    Context mContext;
    ArrayList<String>days;
    ArrayList<ArrayList<String>> programItems;

    public DummyProgramListAdapter(Context context){

        mContext = context;

        days = new ArrayList<>();
        programItems = new ArrayList<>();

        days.add("Day 1");
        days.add("Day 2");
        days.add("Day 3");

        programItems.add(new ArrayList<>());
        programItems.add(new ArrayList<>());
        programItems.add(new ArrayList<>());

        programItems.get(0).add("Day 1 Music");
        programItems.get(0).add("Day 1 Song");
        programItems.get(0).add("Day 1 Talk");

        programItems.get(1).add("Day 2 Music");
        programItems.get(1).add("Day 2 Song");
        programItems.get(1).add("Day 2 Talk");

        programItems.get(2).add("Day 3 Music");
        programItems.get(2).add("Day 3 Song");
        programItems.get(2).add("Day 3 Talk");


    }



    @NonNull
    @Override
    protected ExpandableRecyclerView.ExpandableViewHolder onCreateExpandableViewHolder(ViewGroup viewGroup, int i) {

        ExpandableRecyclerView.ExpandableViewHolder holder;

        if (i == 0){
            //Group heading without baptism
            View v = LayoutInflater.from(mContext).inflate(R.layout.insert_program_layout_list_header2, viewGroup);
            holder = new DummyProgramGroupHolder(v, i);
        }else if (i==1){
            View v = LayoutInflater.from(mContext).inflate(R.layout.insert_program_layout_list_header1, viewGroup);
            holder = new DummyProgramGroupHolder(v, i);
        }else {
            View v = LayoutInflater.from(mContext).inflate(R.layout.insert_program_layout_list_item, viewGroup);
            holder = new DummyProgramItemHolder(v, i);
        }

       return holder;
    }

    @Override
    protected void onBindGroupView(ExpandableRecyclerView.ExpandableViewHolder holder, int i) {

        DummyProgramGroupHolder h = (DummyProgramGroupHolder)holder;
        h.dateTextView.setText(days.get(i));
    }

    @Override
    protected void onBindChildView(ExpandableRecyclerView.ExpandableViewHolder expandableViewHolder, int i, int i1) {
        DummyProgramItemHolder h = (DummyProgramItemHolder)expandableViewHolder;
        h.title_textView.setText(programItems.get(i).get(i1));
    }

    @Override
    protected int getGroupCount() {
        return days.size();
    }

    @Override
    protected int getChildrenCount(int i) {
        return programItems.get(i).size();
    }

    @Override
    protected int getGroupViewType(int i) {
        if (i==1)
        return 1;
        return 0;
    }

    @Override
    protected int getChildViewType(int i, int i1) {
        return 2;
    }

    @Override
    public Object getGroup(int i) {
        return programItems.get(i);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
