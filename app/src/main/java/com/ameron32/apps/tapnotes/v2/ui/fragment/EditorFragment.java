package com.ameron32.apps.tapnotes.v2.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.di.controller.ActivitySnackBarController;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.IEditHandler;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.model.IScripture;
import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.scripture.Bible;
import com.ameron32.apps.tapnotes.v2.scripture.ScriptureFinder;
import com.ameron32.apps.tapnotes.v2.ui.delegate.EditorLayoutFragmentDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.IEditorDelegate;
import com.ameron32.apps.tapnotes.v2.ui.mc_sanitizer.ISanitizer;
import com.ameron32.apps.tapnotes.v2.ui.mc_sanitizer.Sanitizer;
import com.ameron32.apps.tapnotes.v2.util.ColoredDrawableUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 6/15/2015.
 */
public class EditorFragment extends TAPFragment
    implements
      IEditHandler,
      IEditorDelegate.IEditorDelegateCallbacks,
      Toolbar.OnMenuItemClickListener
{

  public static EditorFragment create() {
    final EditorFragment f = new EditorFragment();
    f.setArguments(new Bundle());
    return f;
  }



  public EditorFragment() {}

  @InjectView(R.id.toolbar)
  Toolbar mToolbar;
  @InjectView(R.id.button_submit_note)
  ImageView mSubmitButton;

  private Callbacks mCallbacks;
  private IEditorDelegate mDelegate;

  @Inject
  ActivitySnackBarController mSnackBar;



  @Override
  protected FragmentDelegate createDelegate() {
    return EditorLayoutFragmentDelegate.create(EditorFragment.this);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof Callbacks) {
      mCallbacks = (Callbacks) activity;
    } else {
      throw new IllegalStateException(activity.getClass().getSimpleName()
          + "must implement " + Callbacks.class.getSimpleName() + " in order to use "
          + EditorFragment.class.getSimpleName());
    }
  }

  @Override
  public void onDetach() {
    mCallbacks = null;
    super.onDetach();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_mni_editor, container, false);
    ButterKnife.inject(this, view);
    ColoredDrawableUtil.setDrawableColor(getActivity(),
        mSubmitButton.getDrawable(), R.attr.colorAccent,
        R.color.teal_colorAccent);
    return view;
  }

  @Inject Sanitizer mSanitizer;
  @Inject ScriptureFinder mFinder;
  @Inject Bible mBible;

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    confirmDelegateHasInterface();
    mToolbar.inflateMenu(R.menu.editor_overflow_menu);
    mToolbar.setOnMenuItemClickListener(this);

    if (mBible != null) { mDelegate.onBibleCreated(mBible); }
    if (mSanitizer != null) { mDelegate.onSanitizerCreated(mSanitizer); }
    if (mFinder != null) { /* TODO: KRIS does EditorDelegate need finder? */ }
  }

  private void confirmDelegateHasInterface() {
    if (getDelegate() instanceof IEditorDelegate) {
      mDelegate = ((IEditorDelegate) getDelegate());
    } else {
      throw new IllegalStateException("delegate " +
          "should implement " + IEditorDelegate.class.getSimpleName() +
          " to allow necessary method calls.");
    }
  }

  @Override
  public void onDestroyView() {
    mDelegate = null;
    ButterKnife.reset(this);
    super.onDestroyView();
  }



  @Override
  public void displayNoteToEdit(INote note) {
    mDelegate.updateEditorText(note.getNoteText(), note);
  }

  @Override
  public void onSubmitClicked(String editorText, INote.NoteType type, @Nullable INote note) {
    if (note == null) {
      // existing note was null -- proceed to create new note
      if (editorText == null || editorText.isEmpty()) {
        editorText = "---";
      }
      mCallbacks.createNote(editorText, type);
      return;
    }

    // FOUND EXISTING NOTE -- PROCEED TO EDIT NOTE
    if (note instanceof Note) {
      editNote(editorText, type, (Note) note);
    }
  }

  @Override
  public void setSanitizerCallbacks(ISanitizer.ISanitizerCallbacks callbacks) {
    mSanitizer.setCallbacks(callbacks);
  }

  private void editNote(String editorText, INote.NoteType type, Note note) {
    mCallbacks.editNote(editorText, type, note);
  }

  @Override
  public boolean onMenuItemClick(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.editor_item_scripture:
        mCallbacks.openScripturePicker();
        return true;
      case R.id.editor_item_picture:
        final Intent toPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(toPhoto, REQUEST_PHOTO);
        return true;
      case R.id.editor_item_video:
        final Intent toVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(toVideo, REQUEST_VIDEO);
        return true;
    }
    return false;
  }

  private static final int REQUEST_PHOTO = 7747;
  private static final int REQUEST_VIDEO = 7748;

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == REQUEST_PHOTO) {
        mSnackBar.toast("Picture stored in Gallery.");
      }
      if (requestCode == REQUEST_VIDEO) {
        mSnackBar.toast("Video stored in Gallery.");
      }
    }
  }

  @Override
  public void provideScriptureToEditor(IScripture scripture) {
    mDelegate.onInjectScriptureFromPicker(scripture);
  }

  public interface Callbacks {

    void createNote(String editorText, INote.NoteType type);
    void editNote(String editorText, INote.NoteType type, INote note);
    void openScripturePicker();
    void switchToNextTalk();
    void switchToPreviousTalk();
  }

  @Override
  public void onKeyNextTalk() {
    mCallbacks.switchToNextTalk();
  }

  @Override
  public void onKeyPreviousTalk() {
    mCallbacks.switchToPreviousTalk();
  }
}
