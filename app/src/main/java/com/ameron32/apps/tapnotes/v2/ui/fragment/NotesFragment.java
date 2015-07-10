package com.ameron32.apps.tapnotes.v2.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.OnItemClickListener;
import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.adapter._DummyTestAdapter;
import com.ameron32.apps.tapnotes.v2.di.controller.ActivitySnackBarController;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.ui.delegate.INotesDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.IToolbarHeaderDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.NotesLayoutFragmentDelegate;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 6/15/2015.
 */
public class NotesFragment extends TAPFragment
    implements OnItemClickListener,
      IToolbarHeaderDelegate.IToolbarHeaderCallbacks,
      INotesDelegate.INotesDelegateCallbacks{

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

  private IToolbarHeaderDelegate mHeader;
  private INotesDelegate mNotesDelegate;
  private String mToolbarTitle;
  private String mSymposiumTitle;
  private String mImageUrl;
  private TestCallbacks mCallbacks;
  private ItemTouchHelper mItemTouchHelper;

  public NotesFragment() {
    // empty constructor
  }


  @Override
  protected FragmentDelegate createDelegate() {
    return NotesLayoutFragmentDelegate.create(NotesFragment.this);
  }

  public static NotesFragment create(String toolbarTitle, String talkId, String imageUrl) {
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
    if (activity instanceof TestCallbacks) {
      mCallbacks = (TestCallbacks) activity;
    } else {
      throw new IllegalStateException(activity.getClass().getSimpleName()
          + "must implement " + TestCallbacks.class.getSimpleName() + "in order to use "
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
    final Bundle args = getArguments();
    if (args != null) {
      mToolbarTitle = args.getString(TITLE_ARG);
      mSymposiumTitle = args.getString(TALK_ID_ARG);
      mImageUrl = args.getString(IMAGEURL_ARG);
    }
  }

  // onDataReceived(ITalk talk, List<INote> notes);

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

    mHeader = (IToolbarHeaderDelegate) getDelegate();
    mNotesDelegate = (INotesDelegate) getDelegate();
    mHeader.onToolbarViewCreated(mToolbar);
    setTitles();

    loadData();
  }

  private void loadData() {
    // TODO hand-off received data to delegate for UI update
    // ((NotesLayoutFragmentDelegate) getDelegate).onDataReceived(talk, notes);
  }


  @Override
  public void onDestroyView() {
    ButterKnife.reset(this);
    super.onDestroyView();
  }

//  private void setupRecycler() {
//    mRecyclerView.setHasFixedSize(true);
//    mRecyclerView.setLayoutManager(
//        new LinearLayoutManager(getContext()));
////    final _DummyAdapter adapter = new _DummyAdapter();
////    adapter.setItemClickListener(this);
//    final _DummyTestAdapter adapter = new _DummyTestAdapter();
//    mRecyclerView.setAdapter(adapter);
//    mItemTouchHelper = new ItemTouchHelper(new MyCallback(adapter));
//    mItemTouchHelper.attachToRecyclerView(mRecyclerView);
//  }

  @Override
  public void itemClicked(View v, int position) {
    if (mCallbacks != null) {
      mCallbacks.itemClicked(position);
    }
  }

  private void setTitles() {
    if (mHeader == null) {
      // do nothing
      return;
    }

    if (isStringUsable(mToolbarTitle)) {
      mHeader.setTalkTitle(mToolbarTitle);
    }
    if (isStringUsable(mSymposiumTitle)) {
      mHeader.setSymposiumTitle(mSymposiumTitle);
    }
    if (isStringUsable(mImageUrl)) {
      mHeader.setImage(mImageUrl);
    }
  }



  @Override
  public void onPreviousPressed() {
    // TODO: KRIS delegate callback
  }

  @Override
  public void onNextPressed() {
    // TODO: KRIS delegate callback
  }



  @Override
  public void onUserClickBoldNote(String noteId) {
    // TODO: KRIS delegate callback
  }

  @Override
  public void onUserClickImportantNote(String noteId) {
    // TODO: KRIS delegate callback
  }

  @Override
  public void onUserClickEditNote(String noteId) {
    // TODO: KRIS delegate callback
  }

  @Override
  public void onUserClickDeleteNote(String noteId) {
    // TODO: KRIS delegate callback
  }

  @Override
  public void onUserRepositionNote(String repositionedNoteId, String noteIdBeforeOriginOfRepositionedNote, String noteIdBeforeTargetOfRepositionedNote) {
    // TODO: KRIS delegate callback
  }



  private boolean isStringUsable(final String testString) {
    if (testString != null) {
      if (testString.length() > 0) {
        return true;
      }
    }
    return false;
  }



  public interface TestCallbacks {
    void itemClicked(int position);
  }

  public class MyCallback extends ItemTouchHelper.SimpleCallback {

    private final _DummyTestAdapter mAdapter;

    public MyCallback(_DummyTestAdapter adapter) {
      super(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
          ItemTouchHelper.RIGHT);
      this.mAdapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
        RecyclerView.ViewHolder viewHolder,
        RecyclerView.ViewHolder target) {
      if (viewHolder.getItemViewType() != target.getItemViewType()) {
        return false;
      }

      mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
      return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
      mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
  }
}
