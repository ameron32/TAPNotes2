package com.ameron32.apps.tapnotes.v2.ui.fragment;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.model.INoteEditable;
import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.ui.delegate.EditorLayoutFragmentDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.IEditorDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.NotesLayoutFragmentDelegate;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 6/15/2015.
 */
public class EditorFragment extends TAPFragment
    implements IEditorDelegate.IEditorDelegateCallbacks
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

  @InjectView (R.id.spinner_note_type)
  Spinner spinner;
  @InjectView(R.id.edittext_note_editor)
  EditText noteEditText;
  @InjectView(R.id.button_scripture)
  ImageView scriptureButton;
  @InjectView(R.id.button_camera)
  ImageView cameraButton;
  @InjectView(R.id.button_video)
  ImageView videoButton;
  @InjectView(R.id.button_submit_note)
  ImageView submitNote;


  @Override
  protected FragmentDelegate createDelegate() {
    return EditorLayoutFragmentDelegate.create(EditorFragment.this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_mni_editor, container, false);
    ButterKnife.inject(this, view);
    submitNote.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //Is there a note already attached to this view, if not create it.
        if (v.getTag() == null){
          INote note = getNewNote(v);

        }


      }
    });
    mToolbar.inflateMenu(R.menu.editor_overflow_menu);
    final Drawable d = DrawableCompat.wrap(mSubmitButton.getDrawable());
    int color = getColorFromAttribute(R.attr.colorAccent, R.color.teal_colorAccent);
    DrawableCompat.setTint(d, color);




    return view;
  }


  private INote getNewNote(){
    Note n = new Note();
    INote.NoteType type = convertSpinnerToNoteType(spinner.getSelectedItemPosition());
    switch (type){
      case ATTENDANCE_COUNT:
        n.
      case SPEAKER:
      case SCRIPTURE_ONLY:
      case BAPTISM_COUNT:
      case STANDARD:default:
    }

    return note;

  }


  private int getColorFromAttribute(@AttrRes int attr, @ColorRes int defaultColor) {
    TypedValue typedValue = new TypedValue();
    getActivity().getTheme()
        .resolveAttribute(attr, typedValue, true);
    int[] accentColor = new int[] { attr };
    int indexOfAttrColor = 0;
    TypedArray a = getContext()
        .obtainStyledAttributes(typedValue.data, accentColor);
    int color = a.getColor(indexOfAttrColor, defaultColor);
    a.recycle();
    return color;
  }

  @Override
  public void onDestroyView() {
    ButterKnife.reset(this);
    super.onDestroyView();
  }

  private INote.NoteType convertSpinnerToNoteType (int i){
  switch (i){

    case 0: return INote.NoteType.ATTENDANCE_COUNT;
    case 1: return INote.NoteType.BAPTISM_COUNT;
    case 2: return INote.NoteType.SCRIPTURE_ONLY;
    case 3: return INote.NoteType.SPEAKER;
    case 4:default: return INote.NoteType.STANDARD;
  }
}


  @Override
  public void onSubmitClicked(String editorText, INote.NoteType type, @Nullable String noteId) {

  }


}
