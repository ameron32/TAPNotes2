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
import android.widget.ImageButton;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.model.INote;
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
  ImageButton mSubmitButton;


  @Override
  protected FragmentDelegate createDelegate() {
    return EditorLayoutFragmentDelegate.create(EditorFragment.this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_mni_editor, container, false);
    ButterKnife.inject(this, view);
    mToolbar.inflateMenu(R.menu.editor_overflow_menu);
    final Drawable d = DrawableCompat.wrap(mSubmitButton.getDrawable());
    int color = getColorFromAttribute(R.attr.colorAccent, R.color.teal_colorAccent);
//    getResources().getColor(R.color.teal_colorAccent)
    DrawableCompat.setTint(d, color);
    return view;
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



  @Override
  public void onSubmitClicked(String editorText, INote.NoteType type, @Nullable String noteId) {
    // TODO: KRIS delegate callback
  }
}
