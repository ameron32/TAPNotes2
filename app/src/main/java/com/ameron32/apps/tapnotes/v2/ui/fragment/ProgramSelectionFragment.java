package com.ameron32.apps.tapnotes.v2.ui.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.IProgramList;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.parse.Constants;
import com.ameron32.apps.tapnotes.v2.parse.Queries;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.ui.delegate.ProgramSelectionLayoutFragmentDelegate;
import com.ameron32.apps.tapnotes.v2.util.ColoredDrawableUtil;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProgramSelectionFragment extends TAPFragment
    implements IProgramList {

  public static ProgramSelectionFragment create() {
    ProgramSelectionFragment f = new ProgramSelectionFragment();
    // set Arguments
    return f;
  }

  Callbacks mCallbacks;
  private String mProgramId;

  public ProgramSelectionFragment() {}

  @Override
  protected FragmentDelegate createDelegate() {
    return ProgramSelectionLayoutFragmentDelegate.create(ProgramSelectionFragment.this);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mProgramId = Constants.CONVENTION2015_PROGRAM_OBJECT_ID;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_program_selection, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    setStatusImage();

    getImageFromParseProgram(mProgramId);
  }

  private void getImageFromParseProgram(String programId) {
    ParseQuery.getQuery(Program.class)
        .getInBackground(programId,
            new GetCallback<Program>() {
              @Override
              public void done(Program program, ParseException e) {
                final ParseFile parseFile = (ParseFile) program.get(Constants.PROGRAM_PROGRAMIMAGE_FILE_KEY);
                parseFile.getDataInBackground(new GetDataCallback() {
                  @Override
                  public void done(byte[] bytes, ParseException e) {
                    if (e == null) {
                      putBytesIntoView(bytes);
                    }
                  }
                });
              }
            });
  }

  private void putBytesIntoView(byte[] bytes) {
    final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    programButton.setImageBitmap(bitmap);
  }

  @InjectView(R.id.testing_button_mni)
  ImageButton programButton;
  @InjectView(R.id.status_image)
  ImageView statusImage;

  @OnClick(R.id.testing_button_mni)
  void onClick() {
    if (!programSaved(mProgramId)) {
      getCallbacks().downloadProgram(mProgramId);
//      getCallbacks().startActivity(mProgramId);
    } else {
      getCallbacks().startActivity(mProgramId);
    }
  }

  private void setStatusImage() {
    if (programSaved(mProgramId)) {
      statusImage.setImageResource(R.drawable.ic_action_done);
    } else {
      statusImage.setImageResource(R.drawable.ic_action_get_app);
    }
    ColoredDrawableUtil.setDrawableColor(getActivity(),
        statusImage.getDrawable(),
        R.attr.colorAccent, R.color.teal_colorAccent);
  }

  private boolean programSaved(String programId) {
    try {
      final Program program = Queries.Local.getProgram(programId);
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
