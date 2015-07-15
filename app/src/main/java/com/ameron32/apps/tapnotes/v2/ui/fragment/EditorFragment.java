package com.ameron32.apps.tapnotes.v2.ui.fragment;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.di.controller.ActivitySnackBarController;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.parse.Queries;
import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.ui.delegate.EditorLayoutFragmentDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.IEditorDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.NotesLayoutFragmentDelegate;
import com.parse.ParseException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 6/15/2015.
 */
public class EditorFragment extends TAPFragment
    implements IEditorDelegate.IEditorDelegateCallbacks,
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
    final Drawable d = DrawableCompat.wrap(mSubmitButton.getDrawable());
    int color = getColorFromAttribute(R.attr.colorAccent, R.color.teal_colorAccent);
    DrawableCompat.setTint(d, color);
    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mToolbar.inflateMenu(R.menu.editor_overflow_menu);
    mToolbar.setOnMenuItemClickListener(this);
  }

  private int getColorFromAttribute(@AttrRes int attr, @ColorRes int defaultColor) {
    final TypedValue typedValue = new TypedValue();
    getActivity().getTheme()
            .resolveAttribute(attr, typedValue, true);
    final int[] accentColor = new int[] { attr };
    final int indexOfAttrColor = 0;
    final TypedArray a = getContext()
            .obtainStyledAttributes(typedValue.data, accentColor);
    final int color = a.getColor(indexOfAttrColor, defaultColor);
    a.recycle();
    return color;
  }

  @Override
  public void onDestroyView() {
    ButterKnife.reset(this);
    super.onDestroyView();
  }



  @Override
  public void onSubmitClicked(String editorText, INote.NoteType type, @Nullable String noteId) {
    Note note = null;
    if (noteId != null) {
      try {
        note = Queries.Local.getNote(noteId);

        // FOUND EXISTING NOTE -- PROCEED TO EDIT NOTE -- then return
        editNote(editorText, type, note);
        return;
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    // failed to find existing note -- proceed to create new note
    mCallbacks.createNote(editorText, type);
  }

  private void editNote(String editorText, INote.NoteType type, Note note) {
    if (type != note.getNoteType()) {
      note.changeNoteType(type);
    }
    note.setNoteText(editorText);
  }

  @Override
  public boolean onMenuItemClick(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.editor_item_scripture:
        // TODO KRIS push to scripture picker
        mSnackBar.toast("Scripture--to be implemented.");
        return true;
      case R.id.editor_item_picture:
        // TODO KRIS open camera activity
        mSnackBar.toast("Picture--to be implemented.");
        return true;
      case R.id.editor_item_video:
        // TODO KRIS open video activity
        mSnackBar.toast("Video--to be implemented.");
        return true;
    }
    return false;
  }

  public interface Callbacks {

    void createNote(String editorText, INote.NoteType type);
  }
}
