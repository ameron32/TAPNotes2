package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.model.ITalk;
import com.ameron32.apps.tapnotes.v2.ui.mc_adapter.ProgramAdapter;
import com.levelupstudio.recyclerview.ExpandableRecyclerView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/6/2015.
 *
 * EXAMPLE OF FragmentDelegate USAGE. NOTICE THE SIMILARITY TO REGULAR FRAGMENT.
 */
public class ProgramLayoutFragmentDelegate extends FragmentDelegate {
  public void onDataReceived(List<ITalk> talks) {
    startRecycler(talks);
  }

  @InjectView(R.id.programRecycler)
  ExpandableRecyclerView erv;

  private static final String[] dummyHeaders = {
          "Friday",
          "Saturday",
          "Sunday"
  };


  private static final String[][] dummyContent = {
          {"fri 1","fri 2","fri 3"},
          {"sat 4","sat 5","sat 6","sat 7","sat 8"},
          {"sun 9","sun 10"}
  };


  public static ProgramLayoutFragmentDelegate create(Fragment fragment) {
    final ProgramLayoutFragmentDelegate delegate = new ProgramLayoutFragmentDelegate();
    delegate.setFragment(fragment);
    return delegate;
  }

  protected ProgramLayoutFragmentDelegate() {}

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.insert_program_layout, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.reset(this);
  }

  public void startRecycler(List<ITalk> talks) {
    ButterKnife.inject(this.getActivity());
    erv.setExpandableAdapter(getAdapter());
    erv.setLayoutManager(new LinearLayoutManager(this.getActivity()));

  }

  private ProgramAdapter getAdapter() {
    ProgramAdapter adapter = new ProgramAdapter(dummyHeaders, dummyContent);
    adapter.setStableIdsMode(2); // important command for restoring state
    return adapter;
  }


}
