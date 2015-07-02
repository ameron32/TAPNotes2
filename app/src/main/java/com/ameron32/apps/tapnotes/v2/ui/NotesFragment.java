package com.ameron32.apps.tapnotes.v2.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.frmk.ITalkToolbar;
import com.ameron32.apps.tapnotes.v2.frmk.OnItemClickListener;
import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.adapter._DummyTestAdapter;
import com.ameron32.apps.tapnotes.v2.di.controller.ActivitySnackBarController;
import com.ameron32.apps.tapnotes.v2.di.controller.ActivityFullScreenController;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 6/15/2015.
 */
public class NotesFragment extends TAPFragment
    implements OnItemClickListener, ITalkToolbar {

  private static final String TITLE_ARG = "TITLE_ARG";
  private static final String TEXT1_ARG = "TEXT1_ARG";
  private static final String IMAGEURL_ARG = "IMAGEURL_ARG";

  @InjectView(R.id.collapsing_toolbar)
  CollapsingToolbarLayout mToolbarLayout;
  @InjectView(R.id.appbar)
  AppBarLayout mAppBarLayout;
  @InjectView(R.id.toolbar)
  Toolbar mToolbar;
  @InjectView(R.id.recycler)
  RecyclerView mRecyclerView;
  @InjectView(R.id.text_toolbar_header_item1)
  TextView mTextView1;
  @InjectView(R.id.image_toolbar_header_background)
  ImageView mHeaderImage;

  @Inject
  ActivitySnackBarController mSnackBar;

  private ITalkToolbar mTalkToolbar;
  private String mToolbarTitle;
  private String mText1;
  private String mImageUrl;
  private TestCallbacks mCallbacks;
  private ItemTouchHelper mItemTouchHelper;

  public NotesFragment() {
  }

  public static NotesFragment create(String toolbarTitle, String text1, String imageUrl) {
    final NotesFragment f = new NotesFragment();
    Bundle args = new Bundle();
    args.putString(TITLE_ARG, toolbarTitle);
    args.putString(TEXT1_ARG, text1);
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
      mText1 = args.getString(TEXT1_ARG);
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

    mTalkToolbar = this;
    onToolbarViewCreated(mToolbar);
    setTitles();
    setupRecycler();
  }

  @Override
  public void onDestroyView() {
    ButterKnife.reset(this);
    super.onDestroyView();
  }

  private void setupRecycler() {
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setLayoutManager(
        new LinearLayoutManager(getContext()));
//    final _DummyAdapter adapter = new _DummyAdapter();
//    adapter.setItemClickListener(this);
    final _DummyTestAdapter adapter = new _DummyTestAdapter();
    mRecyclerView.setAdapter(adapter);
    mItemTouchHelper = new ItemTouchHelper(new MyCallback(adapter));
    mItemTouchHelper.attachToRecyclerView(mRecyclerView);
  }

  @Override
  public void itemClicked(View v, int position) {
    if (mCallbacks != null) {
      mCallbacks.itemClicked(position);
    }
  }

  private void setToolbarTitle(String title) {
    if (isStringUsable(title)) {
      mToolbarLayout.setTitle(title);
    }
  }

  @Override
  public void setText1(String text1) {
    if (isStringUsable(text1)) {
      mTextView1.setText(text1);
    }
  }

  @Override
  public void setImage(String imageUrl) {
    if (isStringUsable(imageUrl)) {
      if (URLUtil.isValidUrl(imageUrl)) {
        Picasso.with(getContext()).load(imageUrl).into(mHeaderImage);
      }
    }
  }

  private boolean isStringUsable(String testString) {
    if (testString != null) {
      if (testString.length() > 0) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void onToolbarViewCreated(Toolbar toolbar) {
    final AppCompatActivity activity = ((AppCompatActivity) getActivity());
    activity.setSupportActionBar(toolbar);
    activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu);
    activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  private void setTitles() {
    setToolbarTitle(mToolbarTitle);
    setText1(mText1);
    setImage(mImageUrl);
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
