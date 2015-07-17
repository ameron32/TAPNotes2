package com.ameron32.apps.tapnotes.v2.ui.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.parse.Constants;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.ui.delegate.ProgramSelectionLayoutFragmentDelegate;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProgramSelectionFragment extends TAPFragment {

  public static ProgramSelectionFragment create() {
    ProgramSelectionFragment f = new ProgramSelectionFragment();
    // set Arguments
    return f;
  }

  TestCallbacks mCallbacks;

  public ProgramSelectionFragment() {}

  @Override
  protected FragmentDelegate createDelegate() {
    return ProgramSelectionLayoutFragmentDelegate.create(ProgramSelectionFragment.this);
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

    getImageFromParseProgram(Constants.CONVENTION2015_PROGRAM_OBJECT_ID);
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

  @OnClick(R.id.testing_button_mni)
  void onClick() {
    getCallbacks().startMNIActivity(Constants.CONVENTION2015_PROGRAM_OBJECT_ID);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof TestCallbacks) {
      mCallbacks = (TestCallbacks) activity;
    } else {
      final String message = activity.getClass().getSimpleName() + " must implement " + TestCallbacks.class.getSimpleName();
      throw new IllegalStateException(message);
    }
  }

  @Override
  public void onDetach() {
    mCallbacks = null;
    super.onDetach();
  }

  public interface TestCallbacks {
    void startMNIActivity(final String programId);
  }

  private TestCallbacks getCallbacks() {
    if (mCallbacks == null) {
      Log.e(ProgramSelectionFragment.class.getSimpleName(),
          TestCallbacks.class.getSimpleName() + "#mCallbacks was null");
      return null;
    }
    return mCallbacks;
  }
}
