package com.ameron32.apps.tapnotes.v2.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ameron32.apps.tapnotes.v2.frmk.object.Progress;
import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.IProgressHandler;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProgressFragment extends Fragment
    implements IProgressHandler
{

  // TODO: Rename and change types and number of parameters
  public static ProgressFragment create() {
    ProgressFragment fragment = new ProgressFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public ProgressFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_progress, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    resetProgressView();
  }

  @Override
  public void onDestroyView() {
    ButterKnife.reset(this);
    super.onDestroyView();
  }

  @InjectView(R.id.progressBar)
  ProgressBar progressBar;

  private void resetProgressView() {
//    progressBar.setMax(100);
    progressBar.setIndeterminate(true);
    hideProgressView();
  }

  public void setProgress(Progress progress) {
    showProgressView();
    if (progress.item == progress.total && !progress.failed) {
      setComplete();
      return;
    }

//    if (progress.total > 0) {
//      float percent = ((Integer) progress.item).floatValue() / ((Integer) progress.total).floatValue();
//      final int value = Math.round(percent * 100);
//      progressBar.setProgress(value);
//    }
  }

  public void setComplete() {
    resetProgressView();
  }

  private void showProgressView() {
    if (progressBar.getVisibility() != View.VISIBLE) {
      progressBar.setVisibility(View.VISIBLE);
    }
  }

  private void hideProgressView() {
    if (progressBar.getVisibility() != View.GONE) {
      progressBar.setVisibility(View.GONE);
    }
  }
}
