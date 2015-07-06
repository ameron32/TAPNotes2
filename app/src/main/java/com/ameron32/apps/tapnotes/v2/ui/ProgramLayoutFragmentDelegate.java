package com.ameron32.apps.tapnotes.v2.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.levelupstudio.recyclerview.ExpandableRecyclerView;

/**
 * Created by klemeilleur on 7/6/2015.
 */
public class ProgramLayoutFragmentDelegate extends FragmentDelegate {

  public static ProgramLayoutFragmentDelegate create(Fragment fragment) {
    final ProgramLayoutFragmentDelegate delegate = new ProgramLayoutFragmentDelegate();
    delegate.setFragment(fragment);
    return delegate;
  }

  protected ProgramLayoutFragmentDelegate() {}

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    com.levelupstudio.recyclerview.ExpandableRecyclerView erv = (com.levelupstudio.recyclerview.ExpandableRecyclerView) inflater.inflate(R.layout.insert_program_layout, container);
    erv.setExpandableAdapter(new DummyProgramListAdapter(this.getActivity()));
    erv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    return erv;
  }









  /**
   * PLACEHOLDER ONLY
   */
  class DummyProgramListAdapter extends ExpandableRecyclerView.ExpandableAdapter {

    DummyProgramListAdapter(Activity activity) {

    }

    @NonNull
    @Override
    protected ExpandableRecyclerView.ExpandableViewHolder onCreateExpandableViewHolder(ViewGroup viewGroup, int i) {
      return null;
    }

    @Override
    protected void onBindGroupView(ExpandableRecyclerView.ExpandableViewHolder expandableViewHolder, int i) {

    }

    @Override
    protected void onBindChildView(ExpandableRecyclerView.ExpandableViewHolder expandableViewHolder, int i, int i1) {

    }

    @Override
    protected int getGroupCount() {
      return 0;
    }

    @Override
    protected int getChildrenCount(int i) {
      return 0;
    }

    @Override
    protected int getGroupViewType(int i) {
      return 0;
    }

    @Override
    protected int getChildViewType(int i, int i1) {
      return 0;
    }

    @Override
    public Object getGroup(int i) {
      return null;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link ViewHolder#itemView} to reflect the item at
     * the given position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this
     * method again if the position of the item changes in the data set unless the item itself
     * is invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside this
     * method and should not keep a copy of it. If you need the position of an item later on
     * (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will have
     * the updated adapter position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
  }
}
