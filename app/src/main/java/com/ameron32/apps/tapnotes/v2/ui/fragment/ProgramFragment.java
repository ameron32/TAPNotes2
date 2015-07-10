package com.ameron32.apps.tapnotes.v2.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.parse.Queries;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.parse.object.Talk;
import com.ameron32.apps.tapnotes.v2.ui.delegate.ProgramLayoutFragmentDelegate;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 6/15/2015.
 */
public class ProgramFragment extends TAPFragment {

  private static final String PROGRAM_OBJECT_ID_ARG = "PROGRAM_OBJECT_ID_ARG";

  private Callbacks mCallbacks;
  private String mProgramId;

  public static ProgramFragment create(String programId) {
    final ProgramFragment f = new ProgramFragment();
    final Bundle args = new Bundle();
    args.putString(PROGRAM_OBJECT_ID_ARG, programId);
    f.setArguments(args);
    return f;
  }

  public ProgramFragment() {}

  @Override
  protected FragmentDelegate createDelegate() {
    return ProgramLayoutFragmentDelegate.create(ProgramFragment.this);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupProgram();
  }

  private void setupProgram() {
    if (getArguments() == null) {
      throw new IllegalStateException("should have args bundle. Generate using factory create() method.");
    }

    final Bundle args = getArguments();
    mProgramId = args.getString(PROGRAM_OBJECT_ID_ARG);
    try {
      // TODO consider moving off UI-Thread
      final Program program = Queries.Local.getProgram(mProgramId);
      final List<Talk> talks = Queries.Local.findAllProgramTalks(program);

      // TODO give Talks to Delegate
//      ((ProgramLayoutFragmentDelegate) getDelegate()).onDataReceived(talks);

    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_mni_program, container, false);
    return rootView;
  }

  @InjectView(R.id.nav_toolbar)
  Toolbar mToolbar;

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    setNavigation();
  }

  private void setNavigation() {
    mToolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
    mToolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            clickBackButton();
          }
        }
    );
  }


  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof Callbacks) {
      mCallbacks = (Callbacks) activity;
    } else {
      throw new IllegalStateException("Activity must implement " + Callbacks.class.getSimpleName());
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mCallbacks = null;
  }

  @Override
  public void onDestroyView() {
    ButterKnife.reset(this);
    super.onDestroyView();
  }

  private void clickBackButton() {
    if (mCallbacks != null) {
      mCallbacks.toggleProgramPane();
    }
  }

  public interface Callbacks {
    void toggleProgramPane();
    void changeNotesFragmentTo(Talk talk);
  }
}

