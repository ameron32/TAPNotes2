package com.ameron32.apps.tapnotes.v2.ui.program_selection;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgramViewPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgramViewPagerFragment extends Fragment implements ViewPager.OnPageChangeListener {

  private static final String ITEM_NAME = "name";
  private static final String ITEM_IMAGE_URL = "imageUrl";
  private static final String TAG = ProgramViewPagerFragment.class.getSimpleName();

  private String itemName = "none";
  private String itemImageUrl = "https://i.imgur.com/aIRRjAp.png";
  private int fragmentPosition;
  private int pagerPosition;

  private TextView text;
  private ImageView image;

  public ProgramViewPagerFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment ProgramViewPagerFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static ProgramViewPagerFragment newInstance(ProgramSelection2Fragment.Item item) {
    ProgramViewPagerFragment fragment = new ProgramViewPagerFragment();
    Bundle args = new Bundle();
    args.putString(ITEM_NAME, item.getName());
    args.putString(ITEM_IMAGE_URL, item.getImageUrl());
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      itemName = getArguments().getString(ITEM_NAME);
      itemImageUrl = getArguments().getString(ITEM_IMAGE_URL);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_page_program, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    text = (TextView) view.findViewById(R.id.text);
    image = (ImageView) view.findViewById(R.id.image);
  }

  @Override
  public void onResume() {
    super.onResume();
    text.setText(itemName);
    Picasso.with(getActivity())
        .load(itemImageUrl)
        .fit().centerCrop()
        .into(image, new Callback() {
          @Override
          public void onSuccess() {
            animateButtonInIfInFocus();
          }

          @Override
          public void onError() {

          }
        });
  }

  public void listenToPager(ViewPager pager) {
    pager.addOnPageChangeListener(this);
  }

  public void notifyPosition(int position) {
    this.fragmentPosition = position;
  }

  boolean isThinking = false;

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    if (!isThinking) {
      isThinking = true;
      animateButtonOut();
    }
  }

  @Override
  public void onPageSelected(int position) {
    pagerPosition = position;
//        Log.d(TAG, "onPageSelected: pos " + position);
    animateButtonOut();
    isThinking = false;
  }

  @Override
  public void onPageScrollStateChanged(int state) {
//        Log.d(TAG, "onPageScrollStateChanged: state " + state);
    if (state == ViewPager.SCROLL_STATE_IDLE) {
      animateButtonInIfInFocus();
    }
  }

  private void animateButtonOut() {
    text.animate().alpha(0.0f)
        .setDuration(400).start();
  }

  private void animateButtonInIfInFocus() {
    if (pagerPosition == fragmentPosition) {
      animateButtonIn();
      isThinking = false;
    }
  }

  private void animateButtonIn() {
    text.animate().alpha(1.0f)
        .setStartDelay(400)
        .setDuration(400).start();
  }
}
