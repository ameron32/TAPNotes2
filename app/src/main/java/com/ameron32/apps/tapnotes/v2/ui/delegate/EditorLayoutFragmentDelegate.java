package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.model.INote;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/6/2015.
 */
public class EditorLayoutFragmentDelegate extends FragmentDelegate
    implements IEditorDelegate
{

  // *********************************************
  // MICAH: You must

  //KRIS: Must I?
  // *********************************************


  public static EditorLayoutFragmentDelegate create(Fragment fragment) {
    final EditorLayoutFragmentDelegate delegate = new EditorLayoutFragmentDelegate();
    delegate.setFragment(fragment);
    return delegate;
  }

  private static final IEditorDelegate.IEditorDelegateCallbacks stubCallbacks
      = new IEditorDelegate.IEditorDelegateCallbacks() {

    @Override
    public void onSubmitClicked(String editorText,
                                INote.NoteType type,
                                @Nullable String noteId) {
      // stub only
    }
  };

  private IEditorDelegate.IEditorDelegateCallbacks mCallbacks;

  protected EditorLayoutFragmentDelegate() {}

  @InjectView(R.id.spinner_note_type)
  Spinner spinner;
  @InjectView(R.id.button_submit_note)
  ImageView submitButton;
  @InjectView(R.id.edittext_note_editor)
  EditText noteText;


  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    confirmHostFragmentHasNecessaryCallbacks();
    ButterKnife.inject(this, view);
    setupSpinner();
    setOnClicks();
  }

  @Override
  public void onDestroyView() {
    ButterKnife.reset(this);
    mCallbacks = stubCallbacks;
    super.onDestroyView();
  }

  private void confirmHostFragmentHasNecessaryCallbacks() {
    if (getFragment() instanceof IEditorDelegate.IEditorDelegateCallbacks) {
      mCallbacks = ((IEditorDelegate.IEditorDelegateCallbacks) getFragment());
    } else {
      throw new IllegalStateException("host fragment " +
          "should implement " + IEditorDelegate.IEditorDelegateCallbacks.class.getSimpleName() +
          "to support callback methods.");
    }
  }



  @Override
  public void updateEditorText(String newEditorText, @Nullable String noteID) {
    // THIS METHOD SHOULD BE CALLED WHEN THE USER EDITS A NOTE
    // THE NOTE ID IS PROVIDED HERE.
    // RETURN THE NOTE ID AS A PARAMETER WITH THE CALLBACK METHOD
    // onSubmitClicked(text, type, noteId) TO NOTIFY APP TO MODIFY
    // EXISTING NOTE.
    // TODO: MICAH delegate method
    noteText.setText(newEditorText);
    noteText.setTag(noteID);


  }

  private void setOnClicks(){

    submitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if (noteText.getTag() != null)
        {mCallbacks.onSubmitClicked(noteText.getText().toString(), getNoteType(), (String) noteText.getTag());
        } else {
          mCallbacks.onSubmitClicked(noteText.getText().toString(), getNoteType(), null);
        }

        noteText.setText("");
        noteText.setTag(null);


      }
    });
  }

  private void setupSpinner(){
    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
            R.array.spinner_options, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
    spinner.setAdapter(adapter);
  }

  private INote.NoteType getNoteType(){
    switch(spinner.getSelectedItemPosition()){

      case 4: return INote.NoteType.BAPTISM_COUNT;
      case 3: return INote.NoteType.ATTENDANCE_COUNT;
      case 2: return INote.NoteType.SCRIPTURE_ONLY;
      case 1: return INote.NoteType.SPEAKER;
      case 0: default: return INote.NoteType.STANDARD;

    }
  }

}
