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

import com.ameron32.apps.tapnotes.v2.data.DataAccess;
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
import com.ameron32.apps.tapnotes.v2.data.parse.Constants;
import com.ameron32.apps.tapnotes.v2.data.parse.frmk.LiveReceiver;
import com.ameron32.apps.tapnotes.v2.scripture.Bible;
import com.ameron32.apps.tapnotes.v2.ui.delegate.INotesDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.IToolbarHeaderDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.NotesLayoutFragmentDelegate;
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
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

import static rx.android.lifecycle.LifecycleEvent.*;

/**
 * Created by klemeilleur on 6/15/2015.
 */
public class NotesFragment extends TAPFragment
    implements
      INoteHandler,
      IToolbarHeaderDelegate.IToolbarHeaderCallbacks,
      INotesDelegate.INotesDelegateCallbacks,
      LiveReceiver
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
  @InjectView(R.id.image_toolbar_header_background)
  ImageView mToolbarImage;
  @Inject
  Bus bus;

  private Callbacks mCallbacks;
  private IToolbarHeaderDelegate mHeaderDelegate;
  private INotesDelegate mNotesDelegate;
  private String mTalkId;
  private String mToolbarTitle;
  private String mSymposiumTitle;
  private String mImageUrl;
  private Observable<ITalk> loadImageCache;
  private ITalk mTalk;
  private List<INote> mNotes = new ArrayList<>();
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
      // getNotes().subscribe(); // TODO: test command
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
  private Observable<Progress> cache;

  public NotesFragment() {
    // empty constructor
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
  protected FragmentDelegate createDelegate() {
    return NotesLayoutFragmentDelegate.create(NotesFragment.this);
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
//      giveNotesToDelegate(); // moved to subscription
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    dataManager.syncNotes(mTalk).subscribe(new Observer<DataAccess.Progress>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onNext(DataAccess.Progress progress) {

      }
    });
  }

  private void displayToolbarImage() {
    // TODO KRIS enable toolbar imagery
//    getImageFromProgram(mTalkId);
  }

  private void getImageFromProgram(String talkId) {
    // TODO: confirm if this even works
    loadImageCache = bindLifecycle(dataManager.getTalk(talkId), DESTROY).cache();
    loadImageCache.subscribe(new Observer<ITalk>() {
      @Override
      public void onCompleted() {}
      @Override
      public void onError(Throwable e) {}
      @Override
      public void onNext(ITalk iTalk) {
        Picasso.with(getContext())
                .load(iTalk.getHeaderImageUrl())
                .into(mToolbarImage);
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

  private void giveNotesToDelegate() {
    cache = bindLifecycle(getLocalNotesObservable(), DESTROY).cache();
    cache.subscribe(noteObserver);
  }

  private Observable<List<INote>> getNotes() {
    // TODO: rethink method
    return null;
  }

  private Observable<Progress> getLocalNotesObservable() {
    return Observable.create(new Observable.OnSubscribe<Progress>() {

      @Override
      public void call(Subscriber<? super Progress> subscriber) { // lock (next talk/prev talk)
        subscriber.onNext(new Progress(0, 1, false));
        dataManager.getTalk(mTalkId).subscribe(new Observer<ITalk>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {
            e.printStackTrace();
          }

          @Override
          public void onNext(ITalk iTalk) {
            mTalk = iTalk;
//              mTalk = ParseHelper.Queries.Local.getTalk(mTalkId);
            mSymposiumTitle = mTalk.getSymposiumTitle();
            mHeaderDelegate.setSymposiumTitle(mSymposiumTitle);

            // TODO: decouple from Parse
//          final List<INote> genericNotes = ParseHelper.Queries.Local.findGenericNotesFor((Talk) mTalk);
//          final List<INote> clientNotes = ParseHelper.Queries.Local.findClientOwnedNotesFor((Talk) mTalk);
            dataManager.getNotes(mTalk).subscribe(new Observer<List<INote>>() {
              @Override
              public void onCompleted() {

              }

              @Override
              public void onError(Throwable e) {
                e.printStackTrace();
              }

              @Override
              public void onNext(List<INote> iNotes) {
                mNotes.clear();
                if (iNotes != null) {
                  mNotes.addAll(iNotes);
                }

                Log.d(NotesFragment.class.getSimpleName(),
                        "mNotes.size() : " + mNotes.size());
              }
            });

//              mNotes.clear();
//              mNotes.addAll(genericNotes);
//              mNotes.addAll(clientNotes);
          }
        });

        subscriber.onNext(new Progress(1, 1, false));
        subscriber.onCompleted(); // unlock (next talk/prev talk)
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
      dataManager.deleteNote(note);
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
