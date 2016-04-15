package com.ameron32.apps.tapnotes.v2.ui.program_selection;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.BuildConfig;
import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.parse.ParseHelper;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.IProgramList;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.frmk.object.Progress;
import com.ameron32.apps.tapnotes.v2.data.parse.Constants;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Program;
import com.ameron32.apps.tapnotes.v2.ui.delegate.ProgramSelectionLayoutFragmentDelegate;
import com.ameron32.apps.tapnotes.v2.util.ColoredDrawableUtil;
import com.ameron32.apps.tapnotes.v2.util.NetworkUtil;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProgramSelectionFragment
    extends TAPFragment
    implements IProgramList {

  public static ProgramSelectionFragment create() {
    ProgramSelectionFragment f = new ProgramSelectionFragment();
    // set Arguments
    return f;
  }

  Callbacks mCallbacks;
  private String mProgramId;

  private String mAppVersion;

  public ProgramSelectionFragment() {}

  @Override
  protected FragmentDelegate createDelegate() {
    return ProgramSelectionLayoutFragmentDelegate.create(ProgramSelectionFragment.this);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // TODO replace with DI controller
    mAppVersion = "version " + BuildConfig.VERSION_NAME;
    mProgramId = Constants.CONVENTION2015_PROGRAM_OBJECT_ID;
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_program_selection, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    setStatusImage();
    setRefreshButtonImage();
    setAppVersion();

//    loadPlaceholderView();
    getImageFromProgram(mProgramId);
  }

  private void getImageFromProgram(final String programId) {
    // attempt to get image from local datastore
    ParseQuery.getQuery(Program.class)
        .fromLocalDatastore()
        .getInBackground(programId, new GetCallback<Program>() {
          @Override
          public void done(Program program, ParseException e) {
            if (e == null && program != null) {
              loadProgramImage(program);
            } else {
              // if that fails, attempt from internet connection
              getProgramRemote(programId);
            }
          }
        });
  }

  private void getProgramRemote(String programId) {
    if (!NetworkUtil.isNetworkConnected(getContext())) {
      // do nothing
      return;
    }
    // connection confirmed
    ParseQuery.getQuery(Program.class)
        .getInBackground(programId, new GetCallback<Program>() {
          @Override
          public void done(Program program, ParseException e) {
            if (e == null && program != null) {
//              program.pinInBackground();
              loadProgramImage(program);
            }
          }
        });
  }

  private void loadProgramImage(Program program) {
    Object o = program.get(Constants.PROGRAM_PROGRAMIMAGE_FILE_KEY);
    if (o != null && o instanceof ParseFile) {
      final ParseFile file = (ParseFile) o;
      if (!NetworkUtil.isNetworkConnected(getContext())) {
        // do nothing
        return;
      }
      // TODO undo placeholder2
      Picasso.with(getContext()).load(file.getUrl())
          .error(R.drawable.placeholder)
          .placeholder(R.drawable.placeholder)
          .into(programButton);
    }
  }

  @InjectView(R.id.testing_button_mni)
  ImageButton programButton;
  @InjectView(R.id.refresh_button)
  ImageButton refreshButton;
  @InjectView(R.id.status_image)
  ImageView statusImage;
  @InjectView(R.id.program_progress_bar)
  ProgressBar programProgress;
  @InjectView(R.id.program_progress_bar_text)
  TextView programProgressText;

  @InjectView(R.id.program_app_version)
  TextView appVersionText;

  @OnClick(R.id.testing_button_mni)
  void onClick() {
    if (!isProgramSaved(mProgramId)) {
      getCallbacks().downloadProgram(mProgramId);
    } else {
      getCallbacks().startActivity(mProgramId);
    }
  }

  @OnClick(R.id.refresh_button)
  void onClickRefresh() {
//    getCallbacks().downloadProgram(mProgramId);
    getCallbacks().refreshProgramNotes(mProgramId);
  }

  private void setStatusImage() {
    if (isProgramSaved(mProgramId)) {
      statusImage.setImageResource(R.drawable.ic_action_done);
    } else {
      statusImage.setImageResource(R.drawable.ic_action_get_app);
    }
    ColoredDrawableUtil.setDrawableColor(getActivity(),
        statusImage.getDrawable(),
        R.attr.colorAccent, R.color.teal_colorAccent);
  }

  private void setRefreshButtonImage() {
    if (isProgramSaved(mProgramId)) {
      refreshButton.setImageResource(R.drawable.ic_action_sync);
      ColoredDrawableUtil.setDrawableColor(getActivity(),
          refreshButton.getDrawable(),
          R.attr.colorAccent, R.color.teal_colorAccent);
    } else {
      refreshButton.setImageDrawable(null);
    }
  }

  private void setAppVersion() {
    appVersionText.setText(mAppVersion);
  }

  private boolean isProgramSaved(String programId) {
    try {
      final IProgram program = ParseHelper.Queries.Local.getProgram(programId);
      if (program != null) {
        return true;
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public void setProgramDownloaded(String programId) {
    mProgramId = programId;
    setStatusImage();
    setRefreshButtonImage();
  }

  @Override
  public void setProgramProgress(String programId, Progress progress) {
    if (programId != null) {
      mProgramId = programId;
    }
    setProgress(progress);
  }

  private void setProgress(Progress progress) {
    if (progress.item >= 0) {
      programProgress.setVisibility(View.VISIBLE);
      programProgressText.setVisibility(View.VISIBLE);
      programProgressText.setText(progress.note);
    }
    if (progress.item == progress.total) {
      programProgress.setVisibility(View.INVISIBLE);
      programProgressText.setVisibility(View.INVISIBLE);
    }
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof Callbacks) {
      mCallbacks = (Callbacks) activity;
    } else {
      final String message = activity.getClass().getSimpleName() + " must implement " + Callbacks.class.getSimpleName();
      throw new IllegalStateException(message);
    }
  }

  @Override
  public void onDetach() {
    mCallbacks = null;
    super.onDetach();
  }

  public interface Callbacks {
    void startActivity(final String programId);
    void downloadProgram(final String programId);
    void refreshProgramNotes(final String programId);
  }

  private Callbacks getCallbacks() {
    if (mCallbacks == null) {
      Log.e(ProgramSelectionFragment.class.getSimpleName(),
          Callbacks.class.getSimpleName() + "#mCallbacks was null");
      return null;
    }
    return mCallbacks;
  }
}
