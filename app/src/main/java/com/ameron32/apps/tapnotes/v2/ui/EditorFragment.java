package com.ameron32.apps.tapnotes.v2.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ameron32.apps.tapnotes.v2.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 6/15/2015.
 */
public class EditorFragment extends TAPFragment {

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

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_mni_editor, container, false);
    ButterKnife.inject(this, view);
    mToolbar.inflateMenu(R.menu.editor_overflow_menu);
    final Drawable d = DrawableCompat.wrap(mSubmitButton.getDrawable());
    DrawableCompat.setTint(d, getResources().getColor(R.color.colorAccent));
    return view;
  }

  @Override
  public void onDestroyView() {
    ButterKnife.reset(this);
    super.onDestroyView();
  }
}
