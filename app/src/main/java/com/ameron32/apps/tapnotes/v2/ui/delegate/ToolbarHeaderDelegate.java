package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/9/2015.
 */
public class ToolbarHeaderDelegate extends FragmentDelegate
    implements IToolbarHeader
{

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

  public static ToolbarHeaderDelegate create(final Fragment fragment) {
    final ToolbarHeaderDelegate delegate = new ToolbarHeaderDelegate();
    delegate.setFragment(fragment);
    return delegate;
  }

  // ****************************************************
  //           WARNING
  // ----------------------------
  // DO NOT OVERRIDE onCreateView
  // ****************************************************

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (getFragment() instanceof IToolbarHeaderCallbacks) {
      mCallbacks = ((IToolbarHeaderCallbacks) getFragment());
    } else {
      throw new IllegalStateException("host fragment " +
          "should implement " + IToolbarHeaderCallbacks.class.getSimpleName() +
          "to support callback methods.");
    }
    ButterKnife.inject(this, view);
  }

  @Override
  public void onDestroyView() {
    ButterKnife.reset(this);
    mCallbacks = stubCallbacks;
    super.onDestroyView();
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
}
