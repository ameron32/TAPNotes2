package com.ameron32.apps.tapnotes.v2.parse.frmk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.INoteHandler;
import com.ameron32.apps.tapnotes.v2.model.INote;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/17/2015.
 */
public class NotesPlaceholderFragment extends Fragment
    implements INoteHandler
{

  public NotesPlaceholderFragment() {
    // empty constructor
  }

  public static NotesPlaceholderFragment create() {
    final NotesPlaceholderFragment f = new NotesPlaceholderFragment();
    final Bundle args = new Bundle();
    f.setArguments(args);
    return f;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_mni_notes_placeholder, container, false);
    return rootView;
  }

  @InjectView(R.id.toolbar)
  Toolbar mToolbar;

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);

    final AppCompatActivity activity = ((AppCompatActivity) getActivity());
    activity.setSupportActionBar(mToolbar);
    activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu);
    activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public void onDestroyView() {
    ButterKnife.reset(this);
    super.onDestroyView();
  }

  @Override
  public void notesChanged(List<INote> notes) {
    // stub
  }

  @Override
  public void notesAdded(List<INote> notes) {
    // stub
  }
}
