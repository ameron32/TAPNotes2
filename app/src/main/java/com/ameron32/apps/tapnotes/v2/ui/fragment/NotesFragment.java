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
import android.widget.ImageView;

import com.ameron32.apps.tapnotes.v2.data.DataManager;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.frmk.object.Progress;
import com.ameron32.apps.tapnotes.v2.events.LiveUpdateEvent;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.INoteHandler;
import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.di.controller.ActivitySnackBarController;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.INoteEditable;
import com.ameron32.apps.tapnotes.v2.data.parse.Commands;
import com.ameron32.apps.tapnotes.v2.data.parse.Constants;
import com.ameron32.apps.tapnotes.v2.data.parse.Queries;
import com.ameron32.apps.tapnotes.v2.data.parse.frmk.ParseLiveReceiver;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Note;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Talk;
import com.ameron32.apps.tapnotes.v2.scripture.Bible;
import com.ameron32.apps.tapnotes.v2.ui.delegate.INotesDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.IToolbarHeaderDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.NotesLayoutFragmentDelegate;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import static rx.android.lifecycle.LifecycleEvent.*;

/**
 * Created by klemeilleur on 6/15/2015.
 */
public class NotesFragment extends TAPFragment
    implements
      INoteHandler,
      IToolbarHeaderDelegate.IToolbarHeaderCallbacks,
      INotesDelegate.INotesDelegateCallbacks,
      ParseLiveReceiver
{

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

  @Inject
  DataManager dataManager;
  @Inject
  Bible bible;

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
          + NotesFragment.class.getSimpleName());
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
    bus.register(this);
    final Bundle args = getArguments();
    if (args != null) {
      mTalkId = args.getString(TALK_ID_ARG);
      mToolbarTitle = args.getString(TITLE_ARG);
      mImageUrl = args.getString(IMAGEURL_ARG);
    }
  }

  @Override
  public void onDestroy() {
    bus.unregister(this);
    super.onDestroy();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_mni_notes, container, false);
    return rootView;
  }

  @InjectView(R.id.image_toolbar_header_background)
  ImageView mToolbarImage;

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);


    confirmToolbarDelegateHasInterface();
    confirmNotesDelegateHasInterface();
    mHeaderDelegate.onToolbarViewCreated(mToolbar);
    setTitles();
    displayToolbarImage();

    if (isStringUsable(mTalkId)) {
      mNotesDelegate.onBibleCreated(bible);
      giveNotesToDelegate();
    }
  }

  private void displayToolbarImage() {
    // TODO KRIS enable toolbar imagery
//    getImageFromProgram(mTalkId);
  }

  private void getImageFromProgram(String talkId) {
    ParseQuery.getQuery(Talk.class)
        .fromLocalDatastore()
        .getInBackground(talkId, new GetCallback<Talk>() {
          @Override
          public void done(Talk program, ParseException e) {
            if (e == null) {
              final Object o = program.get(Constants.TALK_HEADERIMAGE_FILE_KEY);
              if (o != null && o instanceof ParseFile) {
                final ParseFile file = (ParseFile) o;
                Picasso.with(getContext()).load(file.getUrl())
                    .into(mToolbarImage);
              }
            }
          }
        });
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

  private ITalk mTalk;
  private List<INote> mNotes = new ArrayList<>();
  private Observable<Progress> cache;

  private void giveNotesToDelegate() {
    cache = bindLifecycle(getLocalNotesObservable(), DESTROY).cache();
    cache.subscribe(noteObserver);
  }

  private final Observer<Progress> noteObserver = new Observer<Progress>() {

    private Progress mostRecentProgress;

    @Override
    public void onCompleted() {
      if (mostRecentProgress != null) {
        if (mostRecentProgress.failed) {
          // failed to load notes locally... that's weird

          return;
        }
      }

      mNotesDelegate.synchronizeNotes(mNotes);
      getNotes(); // TODO: test command
    }

    @Override
    public void onError(Throwable e) {
      e.printStackTrace();
    }

    @Override
    public void onNext(Progress progress) {
      mostRecentProgress = progress;
    }
  };

  private Observable<List<INote>> getNotes() {
//    dataManager.syncNotes();
//    return dataManager.getNotes();
    return null;
  }

  private Observable<Progress> getLocalNotesObservable() {
    return Observable.create(new Observable.OnSubscribe<Progress>() {

      @Override
      public void call(Subscriber<? super Progress> subscriber) { // lock (next talk/prev talk)
        try {
          subscriber.onNext(new Progress(0, 1, false));
          mTalk = Queries.Local.getTalk(mTalkId);
          mSymposiumTitle = mTalk.getSymposiumTitle();
          mHeaderDelegate.setSymposiumTitle(mSymposiumTitle);

          // TODO: decouple from Parse
          final List<INote> genericNotes = Queries.Local.findGenericNotesFor((Talk) mTalk);
          final List<INote> clientNotes = Queries.Local.findClientOwnedNotesFor((Talk) mTalk);

          mNotes.clear();
          mNotes.addAll(genericNotes);
          mNotes.addAll(clientNotes);

          Log.d(NotesFragment.class.getSimpleName(),
              "mNotes.size() : " + mNotes.size());
          subscriber.onNext(new Progress(1, 1, false));
          subscriber.onCompleted(); // unlock (next talk/prev talk)
        } catch (ParseException e) {
          e.printStackTrace();
          subscriber.onNext(new Progress(0, 1, true));
          subscriber.onCompleted(); // unlock (next talk/prev talk)
        }
      }
    });
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



  @Inject
  Bus bus;

  @Subscribe
  public void onRequestComplete(LiveUpdateEvent event) {
    final int requestCode = event.getRequestType();
    onRequestComplete(requestCode);
  }

  @Override
  public void onRequestComplete(int requestCode) {
    switch(requestCode) {
      case LiveUpdateEvent.REQUEST_NOTES_REFRESH:
        Log.d(NotesFragment.class.getSimpleName(), "notes refreshed.");
        giveNotesToDelegate();
        break;
      default:
        // do nothing
    }
  }



  @Override
  public void onUserClickBoldNote(INote note) {
    if (isUserPermitted(note)) {
      if (note instanceof INoteEditable) {
        ((INoteEditable) note).toggleBoldNote();
      }
    }
  }

  @Override
  public void onUserClickImportantNote(INote note) {
    if (isUserPermitted(note)) {
      if (note instanceof INoteEditable) {
        ((INoteEditable) note).toggleImportantNote();
      }
    }
  }

  @Override
  public void onUserClickEditNote(INote note) {
    if (isUserPermitted(note)) {
      mCallbacks.dispatchEditorOn(note);
    }
  }

  @Override
  public void onUserClickDeleteNote(INote note) {
    if (isUserPermitted(note)) {
      mNotesDelegate.removeNotes(listify(note));
      if (note instanceof Note) {
        Commands.Local.deleteEventuallyNote((Note) note);
      }
    }
  }

  private boolean isUserPermitted(INote note) {
    if (note instanceof INoteEditable) {
      INoteEditable eNote = (INoteEditable) note;
      if (eNote.isNoteOwnedByClient() && eNote.isNoteEditableByClient()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void onUserRepositionNote(
      INote repositionedNote,
      INote noteBeforeOriginOfRepositionedNote,
      INote noteBeforeTargetOfRepositionedNote) {
    // TODO KRIS entire move process needs reworking
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

    void dispatchEditorOn(INote note);
  }
}
