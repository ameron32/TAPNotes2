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
import com.ameron32.apps.tapnotes.v2.ui.mc_notes.NotesRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/6/2015.
 *
 * THIS DELEGATE IS IN CHARGE OF BOTH THE TOOLBAR AND THE NOTES
 */
public class NotesLayoutFragmentDelegate extends FragmentDelegate
    implements INotesDelegate, IToolbarHeaderDelegate
{

  private static final IToolbarHeaderCallbacks stubCallbacks1 = new IToolbarHeaderCallbacks() {
    @Override
    public void onPreviousPressed() {
      // stub only
    }

    @Override
    public void onNextPressed() {
      // stub only
    }
  };

  private IToolbarHeaderCallbacks mCallbacks1;

  private static final INotesDelegateCallbacks stubCallbacks2 = new INotesDelegateCallbacks() {
    @Override
    public void onUserClickBoldNote(String noteId) {
      // stub only
    }

    @Override
    public void onUserClickImportantNote(String noteId) {
      // stub only
    }

    @Override
    public void onUserClickEditNote(String noteId) {
      // stub only
    }

    @Override
    public void onUserClickDeleteNote(String noteId) {
      // stub only
    }

    @Override
    public void onUserRepositionNote(String repositionedNoteId, String noteIdBeforeOriginOfRepositionedNote, String noteIdBeforeTargetOfRepositionedNote) {
      // stub only
    }
  };

  private INotesDelegateCallbacks mCallbacks2;



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



  @InjectView(R.id.text_symposium_title)
  TextView mSymposiumTextView;
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
      mCallbacks1 = ((IToolbarHeaderCallbacks) getFragment());
    } else {
      throw new IllegalStateException("host fragment " +
          "should implement " + IToolbarHeaderCallbacks.class.getSimpleName() +
          "to support callback methods.");
    }

    if (getFragment() instanceof IToolbarHeaderCallbacks) {
      mCallbacks2 = ((INotesDelegateCallbacks) getFragment());
    } else {
      throw new IllegalStateException("host fragment " +
          "should implement " + INotesDelegateCallbacks.class.getSimpleName() +
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
    mCallbacks1 = stubCallbacks1;
    mCallbacks2 = stubCallbacks2;
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
    mSymposiumTextView.setText(title);
  }

  @Override
  public void setSpeakerName(String speakerName) {

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

public void addINotesDelegateCallback(INotesDelegateCallbacks callback){
  if (getAdapter()!=null){
    getAdapter().addINotesDelegateCallbacks(callback);
  }
}

  @Override
  public void synchronizeNotes(List<INote> allNotes) {
    // TODO: MICAH delegate method
  }

  @Override
  public void addNotes(List<INote> notesToAdd) {
    // TODO: MICAH delegate method
  }

  @Override
  public void removeNotes(List<INote> notesToRemove) {
    // TODO: MICAH delegate method
  }

  @Override
  public void replaceNotes(List<INote> notesToReplace) {
    // TODO: MICAH delegate method
  }
}
