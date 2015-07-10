package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.model.ITalk;
import com.ameron32.apps.tapnotes.v2.ui.mc_adapter.ProgramAdapter;
import com.ameron32.apps.tapnotes.v2.ui.mc_notes.NotesRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/6/2015.
 */
public class NotesLayoutFragmentDelegate extends FragmentDelegate
    implements INotesDelegate, IToolbarHeader
{

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



  private static final IToolbarHeaderCallbacks stubCallbacks = new IToolbarHeaderCallbacks() {
    @Override
    public void onPreviousPressed() {
      // stub only
    }

    @Override
    public void onNextPressed() {
      // stub only
    }
  };

  private IToolbarHeaderCallbacks mCallbacks;

  @InjectView(R.id.text_toolbar_header_item1)
  TextView mTextView1;
  @InjectView(R.id.text_toolbar_header_item2)
  TextView mTextView2;
  @InjectView(R.id.image_toolbar_header_background)
  ImageView mHeaderImage;
  @InjectView(R.id.collapsing_toolbar)
  CollapsingToolbarLayout mToolbarLayout;
  @InjectView(R.id.notesRecycler)
  RecyclerView recyclerView;

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    confirmHostFragmentHasNecessaryCallbacks();
    ButterKnife.inject(this, view);
  }

  private void confirmHostFragmentHasNecessaryCallbacks() {
    if (getFragment() instanceof IToolbarHeaderCallbacks) {
      mCallbacks = ((IToolbarHeaderCallbacks) getFragment());
    } else {
      throw new IllegalStateException("host fragment " +
          "should implement " + IToolbarHeaderCallbacks.class.getSimpleName() +
          "to support callback methods.");
    }
  }

  @Override
  public void onResume() {
    startRecycler();
  }

  @Override
  public void onDestroyView() {
    ButterKnife.reset(this);
    mCallbacks = stubCallbacks;
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



  @Override
  public void setTalkTitle(String title) {
    mToolbarLayout.setTitle(title);
  }

  @Override
  public void setSymposiumTitle(String title) {
    mTextView1.setText(title);
  }

  @Override
  public void setSpeakerName(String speakerName) {

  }



  @Override
  public void setText1(String text1) {
    mTextView2.setText(text1);
  }

  @Override
  public void setImage(String imageUrl) {
    if (URLUtil.isValidUrl(imageUrl)) {
      Picasso.with(getContext()).load(imageUrl).into(mHeaderImage);
    }
  }

  @Override
  public void onToolbarViewCreated(Toolbar toolbar) {
    final AppCompatActivity activity = ((AppCompatActivity) getActivity());
    activity.setSupportActionBar(toolbar);
    activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu);
    activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }



  @Override
  public void synchronizeNotes(List<INote> allNotes) {

  }

  @Override
  public void addNotes(List<INote> notesToAdd) {

  }

  @Override
  public void removeNotes(List<INote> notesToRemove) {

  }

  @Override
  public void replaceNotes(List<INote> notesToReplace) {

  }
}
