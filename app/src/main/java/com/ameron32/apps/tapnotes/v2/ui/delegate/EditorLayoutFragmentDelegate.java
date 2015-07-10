package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.model.INote;

import butterknife.ButterKnife;

/**
 * Created by klemeilleur on 7/6/2015.
 */
public class EditorLayoutFragmentDelegate extends FragmentDelegate
    implements IEditorDelegate
{

  // *********************************************
  // MICAH: You must
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


  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    confirmHostFragmentHasNecessaryCallbacks();
    ButterKnife.inject(this, view);
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
  public void updateEditorText(String newEditorText, @Nullable String noteId) {
    // THIS METHOD SHOULD BE CALLED WHEN THE USER EDITS A NOTE
    // THE NOTE ID IS PROVIDED HERE.
    // RETURN THE NOTE ID AS A PARAMETER WITH THE CALLBACK METHOD
    // onSubmitClicked(text, type, noteId) TO NOTIFY APP TO MODIFY
    // EXISTING NOTE.
    // TODO: MICAH delegate method
  }
}
