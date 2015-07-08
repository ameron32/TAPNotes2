package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.model.ITalk;
import com.ameron32.apps.tapnotes.v2.ui.mc_notes.NotesRecyclerAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/6/2015.
 */
public class NotesLayoutFragmentDelegate extends FragmentDelegate {



  @InjectView(R.id.notesRecycler)
  RecyclerView recyclerView;
  private RecyclerView.LayoutManager layoutManager;
  private NotesRecyclerAdapter adapter;
  private  RecyclerView.Adapter wrappedAdapter;
  private RecyclerViewDragDropManager dragDropManager;

  public void onDataReceived(ITalk talk, List<INote> notes) {

  }


  public static NotesLayoutFragmentDelegate create(Fragment fragment) {
    final NotesLayoutFragmentDelegate delegate = new NotesLayoutFragmentDelegate();
    delegate.setFragment(fragment);
    return delegate;
  }


  protected NotesLayoutFragmentDelegate() {}

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.insert_program_layout, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    recyclerView = (RecyclerView)view.findViewById(R.id.notesRecycler);



  }

  @Override
  public void onPause(){
    dragDropManager.cancelDrag();
    super.onPause();
  }

  @Override
  public void onResume() {

    startRecycler();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.reset(this);

    if (dragDropManager != null) {
      dragDropManager.release();
      dragDropManager = null;
    }

    if (recyclerView != null) {
      recyclerView.setItemAnimator(null);
      recyclerView.setAdapter(null);
      recyclerView = null;
    }

    if (wrappedAdapter != null) {
      WrapperAdapterUtils.releaseAll(wrappedAdapter);
      wrappedAdapter = null;
    }
    adapter = null;
    layoutManager = null;

    super.onDestroyView();

  }

  public void startRecycler() {
    ButterKnife.inject(this.getActivity());
    recyclerView.setAdapter(getAdapter());
    recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

  }

  private NotesRecyclerAdapter getAdapter() {
    NotesRecyclerAdapter adapter = new NotesRecyclerAdapter();
    return adapter;
  }


}
