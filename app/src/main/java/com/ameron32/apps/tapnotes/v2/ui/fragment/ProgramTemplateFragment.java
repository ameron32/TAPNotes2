package com.ameron32.apps.tapnotes.v2.ui.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.ui.delegate.ProgramTemplateLayoutFragmentDelegate;


public class ProgramTemplateFragment extends TAPFragment {

  public ProgramTemplateFragment() {
    // Required empty public constructor
  }

  @Override
  protected FragmentDelegate createDelegate() {
    return ProgramTemplateLayoutFragmentDelegate.create(ProgramTemplateFragment.this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_program_template, container, false);
  }
}
