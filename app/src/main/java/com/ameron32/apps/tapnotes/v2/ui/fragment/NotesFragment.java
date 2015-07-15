package com.ameron32.apps.tapnotes.v2.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.INoteHandler;
import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.di.controller.ActivitySnackBarController;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.parse.Commands;
import com.ameron32.apps.tapnotes.v2.parse.Queries;
import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.parse.object.Talk;
import com.ameron32.apps.tapnotes.v2.ui.delegate.INotesDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.IToolbarHeaderDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.NotesLayoutFragmentDelegate;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 6/15/2015.
 */
public class NotesFragment extends TAPFragment
    implements
      INoteHandler,
      IToolbarHeaderDelegate.IToolbarHeaderCallbacks,
      INotesDelegate.INotesDelegateCallbacks {

  private static final String TITLE_ARG = "TITLE_ARG";
  private static final String TALK_ID_ARG = "TALK_ID_ARG";
  private static final String IMAGEURL_ARG = "IMAGEURL_ARG";

  @InjectView(R.id.appbar)
  AppBarLayout mAppBarLayout;
  @InjectView(R.id.toolbar)
  Toolbar mToolbar;
  @InjectView(R.id.notesRecycler)
  RecyclerView mRecyclerView;
  @Inject
  ActivitySnackBarController mSnackBar;

  private Callbacks mCallbacks;

  private IToolbarHeaderDelegate mHeaderDelegate;
  private INotesDelegate mNotesDelegate;
  private String mTalkId;
  private String mToolbarTitle;
  private String mSymposiumTitle;
  private String mImageUrl;

  public NotesFragment() {
    // empty constructor
  }


  @Override
  protected FragmentDelegate createDelegate() {
    return NotesLayoutFragmentDelegate.create(NotesFragment.this);
  }

  public static NotesFragment create(
      String toolbarTitle,
      String talkId,
      String imageUrl) {
    final NotesFragment f = new NotesFragment();
    final Bundle args = new Bundle();
    args.putString(TITLE_ARG, toolbarTitle);
    args.putString(TALK_ID_ARG, talkId);
    args.putString(IMAGEURL_ARG, imageUrl);
    f.setArguments(args);
    return f;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof Callbacks) {
      mCallbacks = (Callbacks) activity;
    } else {
      throw new IllegalStateException(activity.getClass().getSimpleName()
          + "must implement " + Callbacks.class.getSimpleName() + " in order to use "
          + EditorFragment.class.getSimpleName());
    }
  }

  @Override
  public void onDetach() {
    mCallbacks = null;
    super.onDetach();
  }


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final Bundle args = getArguments();
    if (args != null) {
      mToolbarTitle = args.getString(TITLE_ARG);
      mTalkId = args.getString(TALK_ID_ARG);
      mImageUrl = args.getString(IMAGEURL_ARG);
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_mni_notes, container, false);
    return rootView;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);

    confirmToolbarDelegateHasInterface();
    confirmNotesDelegateHasInterface();
    mHeaderDelegate.onToolbarViewCreated(mToolbar);
    setTitles();

    if (isStringUsable(mTalkId)) {
      giveNotesToDelegate();
    }
  }

  private void confirmToolbarDelegateHasInterface() {
    if (getDelegate() instanceof IToolbarHeaderDelegate) {
      mHeaderDelegate = ((IToolbarHeaderDelegate) getDelegate());
    } else {
      throw new IllegalStateException("delegate " +
          "should implement " + IToolbarHeaderDelegate.class.getSimpleName() +
          " to allow necessary method calls.");
    }
  }

  private void confirmNotesDelegateHasInterface() {
    if (getDelegate() instanceof INotesDelegate) {
      mNotesDelegate = ((INotesDelegate) getDelegate());
    } else {
      throw new IllegalStateException("delegate " +
          "should implement " + INotesDelegate.class.getSimpleName() +
          " to allow necessary method calls.");
    }
  }

  private void giveNotesToDelegate() {
    // TODO hand-off received data to delegate for UI update
    try {
      // TODO consider moving off UI-Thread
      final Talk talk = Queries.Local.getTalk(mTalkId);
      mSymposiumTitle = talk.getSymposiumTitle();
      final List<Note> notes = Queries.Local.findClientOwnedNotesFor(talk);
      final List<INote> iNotes = new ArrayList<>(notes.size());
      iNotes.addAll(notes);

      Log.d(NotesFragment.class.getSimpleName(),
          "iNotes.size() : " + iNotes.size());

      // TODO give Notes to Delegate
      mNotesDelegate.synchronizeNotes(iNotes);
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onDestroyView() {
    mHeaderDelegate = null;
    mNotesDelegate = null;
    ButterKnife.reset(this);
    super.onDestroyView();
  }

  private void setTitles() {
    if (mHeaderDelegate == null) {
      // do nothing
      return;
    }

    if (isStringUsable(mToolbarTitle)) {
      mHeaderDelegate.setTalkTitle(mToolbarTitle);
    }
    if (isStringUsable(mSymposiumTitle)) {
      mHeaderDelegate.setSymposiumTitle(mSymposiumTitle);
    }
    if (isStringUsable(mImageUrl)) {
      mHeaderDelegate.setImage(mImageUrl);
    }
  }

  @Override
  public void notesChanged(List<INote> notes) {
    mNotesDelegate.replaceNotes(notes);
  }

  @Override
  public void notesAdded(List<INote> notes) {
    mNotesDelegate.addNotes(notes);
  }

  @Override
  public void onPreviousPressed() {
    // TODO: KRIS delegate callback
    mSnackBar.toast("onPreviousPressed() to be implemented.");
  }

  @Override
  public void onNextPressed() {
    // TODO: KRIS delegate callback
    mSnackBar.toast("onNextPressed() to be implemented.");
  }



  @Override
  public void onUserClickBoldNote(String noteId) {
    try {
      final Note note = Queries.Local.getNote(noteId);
      note.toggleBoldNote();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onUserClickImportantNote(String noteId) {
    try {
      final Note note = Queries.Local.getNote(noteId);
      note.toggleImportantNote();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onUserClickEditNote(String noteId) {
    mCallbacks.editNote(noteId);
  }

  @Override
  public void onUserClickDeleteNote(String noteId) {
    try {
      final Note note = Queries.Local.getNote(noteId);
      mNotesDelegate.removeNotes(listify(note));
      Commands.Local.deleteEventuallyNote(note);
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onUserRepositionNote(
      String repositionedNoteId,
      String noteIdBeforeOriginOfRepositionedNote,
      String noteIdBeforeTargetOfRepositionedNote) {
    Note mover = null;
    Note beforeOrigin = null;
    Note beforeTarget = null;
    Note afterTarget = null;
    Note afterMover = null;
    try {
      mover = Queries.Local.getNote(repositionedNoteId);
      beforeOrigin = Queries.Local.getNote(noteIdBeforeOriginOfRepositionedNote);
      beforeTarget = Queries.Local.getNote(noteIdBeforeTargetOfRepositionedNote);
      final String afterTargetId = beforeTarget.getNextNoteId();
      final String afterMoverId = mover.getNextNoteId();
      afterTarget = Queries.Local.getNote(afterTargetId);
      afterMover = Queries.Local.getNote(afterMoverId);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    if (mover == null ||
        beforeOrigin == null || beforeTarget == null ||
        afterTarget == null || afterMover == null) {
      // fail out
      return;
    }
    // proceed with all five notes
    // attach mover to new location
    mover.setNextNote(afterTarget);
    beforeTarget.setNextNote(mover);

    // connect gap left by mover
    beforeOrigin.setNextNote(afterMover);

    final List<INote> updates = listify(mover, beforeOrigin, beforeTarget);
    mNotesDelegate.replaceNotes(updates);
    Commands.Local.saveEventuallyParseNotes(updates);
  }

  private List<INote> listify(INote... notes) {
    final ArrayList<INote> list = new ArrayList<>(notes.length);
    for (int i = 0; i < notes.length; i++) {
      final INote note = notes[i];
      list.add(note);
    }
    return list;
  }



  private boolean isStringUsable(final String testString) {
    if (testString != null) {
      if (testString.length() > 0) {
        return true;
      }
    }
    return false;
  }



  public interface Callbacks {

    void editNote(String noteId);
  }
}
