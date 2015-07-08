package com.ameron32.apps.tapnotes.v2.ui.mc_adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.R;
import com.levelupstudio.recyclerview.ExpandableRecyclerView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Micah on 7/7/2015.
 */
public class ProgramAdapter extends ExpandableRecyclerView.ExpandableAdapter<ProgramAdapter.ViewHolder, Integer> {


    private static final int VIEW_TYPE_CHILD = 0;
    private static final int VIEW_TYPE_GROUP_WITH_BAPTISM = 1;
    private static final int VIEW_TYPE_GROUP_WITHOUT_BAPTISM = 2;

    private final String[] dummyHeaders;
    private final String[][] dummyContent;

    public ProgramAdapter(String[] dummyHeaders, String[][] dummyContent) {
        this.dummyHeaders = dummyHeaders;
        this.dummyContent = dummyContent;
    }


    public abstract static class ViewHolder extends ExpandableRecyclerView.ExpandableViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }


    public static class GroupViewHolderWithoutBaptism extends ViewHolder {

        @InjectView(R.id.dateTextView) TextView dateTextView;

        public GroupViewHolderWithoutBaptism(@NonNull View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

        }


        @Override
        protected boolean canExpand() {
            return true;
        }
    }

    public static class GroupViewHolderWithBaptism extends ViewHolder {

        @InjectView(R.id.dateTextView) TextView dateTextView;


        public GroupViewHolderWithBaptism(@NonNull View itemView) {

            super(itemView);
            ButterKnife.inject(this, itemView);

        }


        @Override
        protected boolean canExpand() {
            return true;
        }
    }


    public static class ChildViewHolder extends ViewHolder {


        @InjectView (R.id.program_item_textview) TextView textView;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

        }


        @Override
        protected boolean canExpand() {
            return false;
        }


        @Override
        protected boolean onViewClicked(final View myView) {
            myView.setVisibility(View.INVISIBLE);
            myView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (myView != null) {
                        myView.setVisibility(View.VISIBLE);
                    }
                }
            }, 2000);
            return false;
        }
    }


    @NonNull
    @Override
    protected ProgramAdapter.ViewHolder onCreateExpandableViewHolder(ViewGroup viewGroup, int i) {
       final View v;
        switch(i) {
            case VIEW_TYPE_CHILD:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.insert_program_layout_list_item, viewGroup, false);
                return new ChildViewHolder(v);
            case VIEW_TYPE_GROUP_WITH_BAPTISM:
                v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.insert_program_layout_list_header1, viewGroup, false);
                return new GroupViewHolderWithBaptism(v);
            case VIEW_TYPE_GROUP_WITHOUT_BAPTISM:
            default:
                v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.insert_program_layout_list_header2, viewGroup, false);
                return new GroupViewHolderWithoutBaptism(v);
        }
    }


    @Override
    protected void onBindGroupView(ProgramAdapter.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof  GroupViewHolderWithBaptism){
            GroupViewHolderWithBaptism holder = (GroupViewHolderWithBaptism)viewHolder;
            holder.dateTextView.setText(dummyHeaders[i]);

        }else if (viewHolder instanceof GroupViewHolderWithoutBaptism){
            GroupViewHolderWithoutBaptism holder = (GroupViewHolderWithoutBaptism)viewHolder;
            holder.dateTextView.setText(dummyHeaders[i]);

        }else{
            return;
        }


    }


    @Override
    protected void onBindChildView(ProgramAdapter.ViewHolder viewHolder, int i, int i2) {
        if (!(viewHolder instanceof ChildViewHolder)) {
            // fail: ensure onCreate is generating the correct ChildViewHolder 
            return;
        }


        ChildViewHolder cvh = (ChildViewHolder) viewHolder;
        cvh.textView.setText(dummyContent[i][i2].toString());
    }


    @Override
    protected int getGroupCount() {
        return dummyHeaders.length;
    }


    @Override
    protected int getChildrenCount(int i) {
        return dummyContent[i].length;
    }


    @Override
    protected int getGroupViewType(int i) {
        if (i==1)
        return VIEW_TYPE_GROUP_WITH_BAPTISM;
    else
        return VIEW_TYPE_GROUP_WITHOUT_BAPTISM;
    }


    @Override
    protected int getChildViewType(int i, int i2) {
        return VIEW_TYPE_CHILD;
    }


    @Override
    public Integer getGroup(int i) {
        return i;
    }
} 
