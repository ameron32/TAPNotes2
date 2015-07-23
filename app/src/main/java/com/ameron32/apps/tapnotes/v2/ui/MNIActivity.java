package com.ameron32.apps.tapnotes.v2.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ameron32.apps.tapnotes.v2.di.controller.ParseNotesController;
import com.ameron32.apps.tapnotes.v2.frmk.object.Progress;
import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.di.controller.ApplicationThemeController;
import com.ameron32.apps.tapnotes.v2.di.module.DefaultAndroidActivityModule;
import com.ameron32.apps.tapnotes.v2.di.module.MNIActivityModule;
import com.ameron32.apps.tapnotes.v2.events.ParseRequestLiveUpdateEvent;
import com.ameron32.apps.tapnotes.v2.frmk.IEditHandler;
import com.ameron32.apps.tapnotes.v2.frmk.INoteHandler;
import com.ameron32.apps.tapnotes.v2.frmk.IProgressHandler;
import com.ameron32.apps.tapnotes.v2.frmk.TAPActivity;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.model.IScripture;
import com.ameron32.apps.tapnotes.v2.model.ITalk;
import com.ameron32.apps.tapnotes.v2.parse.Commands;
import com.ameron32.apps.tapnotes.v2.parse.Queries;
import com.ameron32.apps.tapnotes.v2.parse.Rx;
import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.parse.object.Talk;
import com.ameron32.apps.tapnotes.v2.ui.fragment.EditorFragment;
import com.ameron32.apps.tapnotes.v2.ui.fragment.NotesFragment;
import com.ameron32.apps.tapnotes.v2.ui.fragment.NotesPlaceholderFragment;
import com.ameron32.apps.tapnotes.v2.ui.fragment.ProgramFragment;
import com.ameron32.apps.tapnotes.v2.ui.fragment.ProgressFragment;
import com.ameron32.apps.tapnotes.v2.ui.fragment.ScripturePickerFragment;
import com.ameron32.apps.tapnotes.v2.ui.view.AnimatingPaneLayout;
import com.parse.ParseException;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Observer;

import static rx.android.lifecycle.LifecycleEvent.*;

/**
 * Activity -- Main Notetaking Interface.
 * <p>
 * Use {@link MNIActivity#makeIntent(Context, String)} to startActivity.
 */
public class MNIActivity extends TAPActivity
    implements
      ProgramFragment.Callbacks,
      EditorFragment.Callbacks,
      NotesFragment.Callbacks,
      ScripturePickerFragment.Callbacks
{



  private static final String EXTRA_KEY_PROGRAM_ID = "EXTRA_KEY_PROGRAM_ID";

  /**
   * @param context
   *        required context for Intent
   * @param programId
   *        the objectId of the ParseObject for the Program to load
   * @return the intent ready to startActivity
   */
  public static Intent makeIntent(final Context context,
      final String programId) {
    final Intent i = new Intent(context, MNIActivity.class);
    i.putExtra(EXTRA_KEY_PROGRAM_ID, programId);
    return i;
  }

  @InjectView(R.id.drawer_layout)
  DrawerLayout mDrawerLayout;
  @InjectView(R.id.nav_view)
  NavigationView mNavigationView;

  @InjectView(R.id.pane_animating_layout)
  AnimatingPaneLayout mAnimatingPane;

  private String mProgramId;
  private String mCurrentTalkId;



  // ---------------------------------------------------
  // LIFECYCLE
  // ---------------------------------------------------

  @Inject
  ApplicationThemeController themeController;

  @Override
  protected Object[] getModules() {
    return new Object[] {
        new MNIActivityModule(this),
        new DefaultAndroidActivityModule(this)
    };
  }

  @Override
  protected @LayoutRes int getLayoutResource() {
    // rather than setContentView(), provide inflatable layout here
    return R.layout.activity_mni;
  }

  @Override
  protected int provideThemeResource() {
    return themeController.getTheme();
  }

  @Inject
  Bus bus;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    bus.register(this);
    // setContentView() handled in super.onCreate()
    mProgramId = getProgramId(savedInstanceState);
    ButterKnife.inject(this);

    setupDrawer();
    commitNotesPlaceholder(); //blank
    commitProgramFragment(mProgramId);
    commitProgressFragment();
    // commitEditorFragment when real NotesFragment is created
  }

  @Override
  protected void onDestroy() {
    bus.unregister(this);
    ButterKnife.reset(this);
    super.onDestroy();
  }

  // ---------------------------------------------------
  // MENU
  // ---------------------------------------------------

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_tap, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    switch (id) {
      case android.R.id.home:
        toggleProgramPane();
        return true;
      case R.id.action_settings:
        startActivityForResult(SettingsActivity.makeIntent(getContext()), SETTINGS_REQUEST_CODE);
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private static final int SETTINGS_REQUEST_CODE = 141;

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK) {
      // Settings
      this.recreate();
    }
  }

  // ---------------------------------------------------
  //
  // ---------------------------------------------------

  private String getProgramId(final Bundle savedInstanceState) {
    final String intentProgramId = getIntent().getStringExtra(EXTRA_KEY_PROGRAM_ID);
    if (intentProgramId != null) {
      return intentProgramId;
    }

    if (savedInstanceState != null) {
      final String savedProgramId = savedInstanceState.getString(EXTRA_KEY_PROGRAM_ID);
      if (savedProgramId != null) {
        return savedProgramId;
      }
    }
    throw new IllegalStateException("no StringExtra exists for programId. " +
        "Did you use the static factory to create the MNIActivity intent?");
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (mProgramId != null) {
      outState.putString(EXTRA_KEY_PROGRAM_ID, mProgramId);
    }
  }

  private void setupDrawer() {
    setupDrawerContent(mNavigationView);
  }

  private static final String TAG_PROGRESS = "progress";
  private static final String TAG_NOTES = "notes";
  private static final String TAG_PROGRAM = "program";
  private static final String TAG_EDITOR = "editor";
  private static final String TAG_SCRIPTURE_PICKER = "scripturepicker";

  private void commitProgressFragment() {
    final String tag = TAG_PROGRESS;
    removeFragment(tag);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.progress_container,
            ProgressFragment.create(), tag)
        .commit();
  }

  private IProgressHandler getProgressFragment() {
    final Fragment fragmentByTag = getSupportFragmentManager()
        .findFragmentByTag(TAG_PROGRESS);
    if (fragmentByTag != null) {
      return (IProgressHandler) fragmentByTag;
    }
    return null;
  }

  private void commitNotesPlaceholder() {
    final String tag = TAG_NOTES;
    removeFragment(tag);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.notes_xcontainer,
            NotesPlaceholderFragment.create(), tag)
        .commit();
  }

  private void commitNotesFragment(final String talkId, final String toolbarTitle, final String imageUrl) {
    final String tag = TAG_NOTES;
    removeFragment(tag);
    getSupportFragmentManager().beginTransaction()
        .addToBackStack(talkId)
        .replace(R.id.notes_xcontainer,
            NotesFragment.create(toolbarTitle, talkId, imageUrl), tag)
        .commit();
    mCurrentTalkId = talkId;

    // if editor isn't open, open it
    final IEditHandler editorFragment = getEditorFragment();
    if (editorFragment == null) {
      commitEditorFragment();  //blank
    }
  }

  private INoteHandler getNotesFragment() {
    final Fragment fragmentByTag = getSupportFragmentManager()
        .findFragmentByTag(TAG_NOTES);
    if (fragmentByTag != null) {
      return (INoteHandler) fragmentByTag;
    }
    return null;
  }

  private void commitProgramFragment(final String programId) {
    final String tag = TAG_PROGRAM;
    removeFragment(tag);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.program_container,
            ProgramFragment.create(programId), tag)
        .commit();
  }

  private void commitEditorFragment() {
    final String tag = TAG_EDITOR;
    removeFragment(tag);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.editor_container,
            EditorFragment.create(), tag)
        .commit();
  }

  private IEditHandler getEditorFragment() {
    final Fragment fragmentByTag = getSupportFragmentManager()
        .findFragmentByTag(TAG_EDITOR);
    if (fragmentByTag != null) {
      return (IEditHandler) fragmentByTag;
    }
    return null;
  }

  private void commitNewScripturePickerFragment() {
    final String tag = TAG_SCRIPTURE_PICKER;
    removeFragment(tag);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.scripture_picker_container,
            ScripturePickerFragment.create(), tag)
        .addToBackStack(tag)
        .commit();
  }

  private void removeFragment(final String tag) {
    final Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
    if (fragment != null) {
      getSupportFragmentManager().beginTransaction()
          .remove(fragment)
          .commit();
    }
  }

  private void setupDrawerContent(NavigationView navigationView) {
    navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
              }
            });
  }

  private void postLiveUpdateEvent(int requestCode) {
    bus.post(new ParseRequestLiveUpdateEvent(requestCode));
  }



  private void openNavigationDrawer() {
    mDrawerLayout.openDrawer(GravityCompat.START);
  }

  private void toggleAnimatingLayout() {
    mAnimatingPane.toggleLayout();
  }

  private void displayNotesFragment() {
    mAnimatingPane.displayMainPane();
  }

  private List<INote> listify(INote... notes) {
    final ArrayList<INote> list = new ArrayList<>(notes.length);
    for (int i = 0; i < notes.length; i++) {
      final INote note = notes[i];
      list.add(note);
    }
    return list;
  }



  // ---------------------------------------------------
  // CALLBACKS -- DISTRIBUTE
  // ---------------------------------------------------

  @Override
  public void createNote(String editorText, INote.NoteType type) {
    mECallbacks.createNote(editorText, type);
  }

  @Override
  public void editNote(String editorText, INote.NoteType type, Note note) {
    mECallbacks.editNote(editorText, type, note);
  }

  @Override
  public void openScripturePicker() {
    mECallbacks.openScripturePicker();
  }

  @Override
  public void toggleProgramPane() {
    mPCallbacks.toggleProgramPane();
  }

  @Override
  public void changeNotesFragmentTo(ITalk talk) {
    mPCallbacks.changeNotesFragmentTo(talk);
  }

  @Override
  public void dispatchEditorOn(INote note) {
    mNCallbacks.dispatchEditorOn(note);
  }

  @Override
  public void scripturePrepared(IScripture scripture) {
    mSCallbacks.scripturePrepared(scripture);
  }

  private ProgramFragment.Callbacks mPCallbacks = new ProgramFragment.Callbacks() {

    @Override
    public void toggleProgramPane() {
      toggleAnimatingLayout();
    }

    @Override
    public void changeNotesFragmentTo(ITalk talk) {
      if (talk == null) {
        // do nothing
        return;
      }

      // TODO add an imageUrl
      final String talkId = talk.getId();
      final String talkName = talk.getTalkTitle();
      final String imageUrl = ""; // TODO update
      commitNotesFragment(talkId, talkName, imageUrl);

      displayNotesFragment();

      try {
        if (talk instanceof Talk) {
          final Program program = Queries.Local.getProgram(mProgramId);
          cache = bindLifecycle(Rx.Live.pinAllClientOwnedNotesFor(program,
              (Talk) talk, notesController.getLastCheckedThenUpdateToNow()), DESTROY).cache();
          cache.subscribe(observer);
          // see Observer for callbacks
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
  };

  @Inject
  ParseNotesController notesController;

  private Observable<Progress> cache;

  private Observer<Progress> observer = new Observer<Progress>() {

    @Override
    public void onCompleted() {
      // as soon as request is complete
      postLiveUpdateEvent(ParseRequestLiveUpdateEvent.REQUEST_NOTES_REFRESH);
    }

    @Override
    public void onError(Throwable e) {
      e.printStackTrace();
    }

    @Override
    public void onNext(Progress progress) {
      Log.d("MNI:observer", progress.item + " of " + progress.total + " with status " +
          ((progress.failed) ? "FAILED" : "good"));
      getProgressFragment().setProgress(progress);
    }
  };

  private EditorFragment.Callbacks mECallbacks = new EditorFragment.Callbacks() {

    @Override
    public void createNote(String editorText, INote.NoteType type) {
//      Note prevNote = null;
//      try {
//        final Talk talk = Queries.Local.getTalk(mCurrentTalkId);
//        prevNote = Queries.Local.findLastClientOwnedNoteFor(talk);
//      } catch (ParseException e) {
//        e.printStackTrace();
//      }
      final Note note = Note.create(editorText, mProgramId, mCurrentTalkId, Commands.Local.getClientUser());
//      final Note lastNote = prevNote;
//
//      if (lastNote != null) {
//        lastNote.setNext(note);
//        Commands.Local.saveEventuallyNote(lastNote);
//      }

      if (note != null) {
        // TODO replace with Observable!!!
        Commands.Local.saveEventuallyNote(note);
//        getNotesFragment().notesChanged(listify(lastNote));
        getNotesFragment().notesAdded(listify(note));
      }
    }

    @Override
    public void editNote(String editorText, INote.NoteType type, Note note) {
      if (type != note.getNoteType()) {
        note.changeNoteType(type);
      }
      note.setNoteText(editorText);

      Commands.Local.saveEventuallyNote(note);
      getNotesFragment().notesChanged(listify(note));
    }

    @Override
    public void openScripturePicker() {
      commitNewScripturePickerFragment();
    }
  };

  private NotesFragment.Callbacks mNCallbacks = new NotesFragment.Callbacks() {

    @Override
    public void dispatchEditorOn(INote note) {
      getEditorFragment().displayNoteToEdit(note);
    }
  };

  private ScripturePickerFragment.Callbacks mSCallbacks = new ScripturePickerFragment.Callbacks() {

    @Override
    public void scripturePrepared(IScripture scripture) {
      // TODO scripture picker generated scripture
      final String tag = TAG_SCRIPTURE_PICKER;
      removeFragment(tag);

      // TODO scripture to editor
//      getEditorFragment().provideScriptureToEditor();
    }
  };
}
