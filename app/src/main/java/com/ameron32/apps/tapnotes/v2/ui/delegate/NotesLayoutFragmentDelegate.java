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
import com.ameron32.apps.tapnotes.v2.model.IBible;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.model.ITalk;
import com.ameron32.apps.tapnotes.v2.ui.mc_notes.NotesRecycler;
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

  private NotesRecyclerAdapter adapter;

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
    public void onUserClickBoldNote(INote note) {
      // stub only

    }

    @Override
    public void onUserClickImportantNote(INote note) {
      // stub only
    }

    @Override
    public void onUserClickEditNote(INote note) {
      // stub only
    }

    @Override
    public void onUserClickDeleteNote(INote note) {
      // stub only
    }

    @Override
    public void onUserRepositionNote(INote repositionedNote, INote noteBeforeOriginOfRepositionedNote, INote noteBeforeTargetOfRepositionedNote) {
      // stub only
    }
  };

  private INotesDelegateCallbacks mCallbacks2;


  public void onCreate(@Nullable Bundle savedInstanceState) {

    adapter = new NotesRecyclerAdapter(getContext());

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
    return inflater.inflate(R.layout.insert_notes_layout, container, false);
  }



  @InjectView(R.id.text_symposium_title)
  TextView mSymposiumTextView;
  @InjectView(R.id.text_talk_title)
  TextView mTalkTextView;
  @InjectView(R.id.image_toolbar_header_background)
  ImageView mHeaderImage;
  @InjectView(R.id.collapsing_toolbar)
  CollapsingToolbarLayout mToolbarLayout;
  @InjectView(R.id.notesRecycler)
  NotesRecycler recyclerView;

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    confirmHostFragmentHasNecessaryCallbacks();
    ButterKnife.inject(this, view);
    startRecycler();

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

    if (adapter==null){
      adapter = new NotesRecyclerAdapter(getContext());
    }

    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    adapter.addINotesDelegateCallbacks(mCallbacks2);
  }


  @Override
  public void setTalkTitle(String title) {
    mToolbarLayout.setTitle(title);
    mTalkTextView.setText(title);
  }

  @Override
  public void setSymposiumTitle(String title) {
    mSymposiumTextView.setText(title);
  }

  @Override
  public void setSpeakerName(String speakerName) {
    // TODO allow speaker notes
  }

  @Override
  public void onBibleCreated(IBible bible) {
    adapter.onBibleCreated(bible);
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
    // TODO: MICAH delegate method
    adapter.synchronizeNotes(allNotes);
    desselect();
//    moveToLastItem(); // TODO UX decision
  }

  @Override
  public void addNotes(List<INote> notesToAdd) {
      adapter.addNotes(notesToAdd);
    desselect();
    moveToLastItem(); // TODO UX decision
  }

  @Override
  public void removeNotes(List<INote> notesToRemove) {
      adapter.removeNotes(notesToRemove);
    desselect();
//    moveToLastItem(); // TODO UX decision
  }

  @Override
  public void replaceNotes(List<INote> notesToReplace) {
    adapter.replaceNotes(notesToReplace);
    desselect();
//    moveToLastItem(); // TODO UX decision
  }

  private void desselect(){
    int count = recyclerView.getChildCount();
    for (int i=0; i<count; i++){
    recyclerView.getChildAt(i).setSelected(false);
    }
  }

  private void moveToLastItem() {
    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
  }
}
