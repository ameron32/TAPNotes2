package com.ameron32.apps.tapnotes.v2.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ameron32.apps.tapnotes.v2.BuildConfig;
import com.ameron32.apps.tapnotes.v2.di.controller.ActivitySnackBarController;
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
import com.ameron32.apps.tapnotes.v2.parse.Constants;
import com.ameron32.apps.tapnotes.v2.parse.Queries;
import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.parse.object.Talk;
import com.ameron32.apps.tapnotes.v2.parse.ui.MyDispatchMainActivity;
import com.ameron32.apps.tapnotes.v2.ui.fragment.EditorFragment;
import com.ameron32.apps.tapnotes.v2.ui.fragment.NotesFragment;
import com.ameron32.apps.tapnotes.v2.ui.fragment.NotesPlaceholderFragment;
import com.ameron32.apps.tapnotes.v2.ui.fragment.ProgramFragment;
import com.ameron32.apps.tapnotes.v2.ui.fragment.ProgressFragment;
import com.ameron32.apps.tapnotes.v2.ui.fragment.ScripturePickerFragment;
import com.ameron32.apps.tapnotes.v2.ui.view.AnimatingPaneLayout;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
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
  private static final String EXTRA_KEY_CURRENT_TALK_ID = "EXTRA_KEY_CURRENT_TALK_ID";

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
  @InjectView(R.id.nav_header_username_text)
  TextView mUsernameTextView;

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
    mCurrentTalkId = getCurrentTalkId(savedInstanceState);
    ButterKnife.inject(this);

    setupDrawer();
    if (mCurrentTalkId == null) {
      commitNotesPlaceholder(); //blank
    } else {
      commitNotesFragmentFromTalkId(mCurrentTalkId);
    }
    commitProgramFragment(mProgramId);
    commitProgressFragment();
    // commitEditorFragment() when real NotesFragment is created
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

      case R.id.action_submit_feedback:
        // TODO app version to DI controller
        final String version = "version " + BuildConfig.VERSION_NAME;
        Toast.makeText(getContext(), "TAP Notes v2: " + version, Toast.LENGTH_LONG).show();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.LINK_SUBMIT_FEEDBACK_GOOGLE_FORMS));
        startActivity(browserIntent);
        return true;

      case R.id.action_next_talk:
        switchToNextTalk();
        return true;

      case R.id.action_prev_talk:
        switchToPreviousTalk();
        return true;

      case R.id.action_refresh_talk:
        unpinAndRepinNotesForThisTalk();
        return true;

      case R.id.action_settings:
        startActivityForResult(SettingsActivity.makeIntent(getContext()), SETTINGS_REQUEST_CODE);
        return true;

      case R.id.action_about:
        new LibsBuilder()
            //Pass the fields of your application to the lib so it can find all external lib information
            .withFields(R.string.class.getFields())
                //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
            .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
            .withAboutIconShown(true)
            .withAboutVersionShown(true)
            .withAboutDescription("<b>TAP Notes v2</b><br />" +
                "Take notes with quick references to scriptures.<br />" +
                "<b>Thank you for using this app!</b>")
                //start the activity
            .start(this);
        return true;

    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void switchToNextTalk() {
    try {
      final Talk talk = Queries.Local.getTalk(mCurrentTalkId);
      final String sequence = talk.getSequence();
      final String session = String.valueOf(sequence.charAt(0));
      final int sequenceWithinSession = Integer.valueOf(sequence.substring(1));
      final String sequenceWithinSessionString = String.format("%03d", sequenceWithinSession+1);
      Log.d(MNIActivity.class.getSimpleName(), "find sequence: " + session + sequenceWithinSessionString);
      final Talk nextTalk = Queries.Local.getTalkAtSequence(session + sequenceWithinSessionString);
      commitNotesFragmentFromTalkId(nextTalk.getId());
    } catch (ParseException e) {
      e.printStackTrace();
      Log.d(MNIActivity.class.getSimpleName(), "query failed unexpectedly");
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      Log.d(MNIActivity.class.getSimpleName(), "nextTalk not found");
    }
  }

  @Override
  public void switchToPreviousTalk() {
    try {
      final Talk talk = Queries.Local.getTalk(mCurrentTalkId);
      final String sequence = talk.getSequence();
      final String session = String.valueOf(sequence.charAt(0));
      final int sequenceWithinSession = Integer.valueOf(sequence.substring(1));
      final String sequenceWithinSessionString = String.format("%03d", sequenceWithinSession-1);
      Log.d(MNIActivity.class.getSimpleName(), "find sequence: " + session + sequenceWithinSessionString);
      final Talk nextTalk = Queries.Local.getTalkAtSequence(session + sequenceWithinSessionString);
      commitNotesFragmentFromTalkId(nextTalk.getId());
    } catch (ParseException e) {
      e.printStackTrace();
      Log.d(MNIActivity.class.getSimpleName(), "query failed unexpectedly");
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      Log.d(MNIActivity.class.getSimpleName(), "prevTalk not found");
    }
  }

  private static final int SETTINGS_REQUEST_CODE = 141;

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK) {
      // from Settings, we rebuild the activity just in case the theme was changed.
      // better safe then sorry.
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

  private String getCurrentTalkId(final Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      final String savedTalkId = savedInstanceState.getString(EXTRA_KEY_CURRENT_TALK_ID);
      if (savedTalkId != null) {
        return savedTalkId;
      }
    }
    return null;
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (mProgramId != null) {
      outState.putString(EXTRA_KEY_PROGRAM_ID, mProgramId);
      outState.putString(EXTRA_KEY_CURRENT_TALK_ID, mCurrentTalkId);
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

  private void commitNotesFragmentFromTalkId(final String talkId) {
    try {
      final Talk talk = Queries.Local.getTalk(talkId);
      if (talk != null) {
        final String talkName = talk.getTalkTitle();
        final String imageUrl = ""; // TODO implement imageUrl
        commitNotesFragment(talkId, talkName, imageUrl);
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
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
                switch(menuItem.getItemId()) {
                  case R.id.nav_home:
                    // go to dispatch, then straight thru to ProgramSelectionActivity
                    startActivity(MyDispatchMainActivity.makeIntent(getContext()));
                    finish();
                    return true;
                  case R.id.nav_logout:
                    // logout then...
                    Commands.Local.logoutClientUser();
                    // go to dispatch, then straight thru to MyDispatchLoginActivity
                    startActivity(MyDispatchMainActivity.makeIntent(getContext()));
                    finish();
                    return true;
                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
              }
            });
    mUsernameTextView.setText(Commands.Local.getClientUser().getUsername());
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
  public void editNote(String editorText, INote.NoteType type, INote note) {
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

  @Override
  public void scriptureCancelled() {
    mSCallbacks.scriptureCancelled();
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
      final String imageUrl = ""; // TODO implement imageUrl
      commitNotesFragment(talkId, talkName, imageUrl);

      displayNotesFragment();

      try {
        if (talk instanceof Talk) {
          final Program program = Queries.Local.getProgram(mProgramId);
          cache = bindLifecycle(notesController.pinAllNewClientOwnedNotesFor(program,
              (Talk) talk), DESTROY).cache();
          cache.subscribe(observer);
          // see Observer for callbacks
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
  };

  private void unpinAndRepinNotesForThisTalk() {
    try {
      final Talk talk = Queries.Local.getTalk(mCurrentTalkId);
      final Program program = Queries.Local.getProgram(mProgramId);
      if (talk instanceof Talk) {
        cache2 = bindLifecycle(
            notesController.unpinThenRepinAllClientOwnedNotesFor(program, talk),
            DESTROY).cache();
        cache2.subscribe(observer);
        // see Observer for callbacks
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }



  @Inject
  ParseNotesController notesController;

  private Observable<Progress> cache;
  private Observable<Progress> cache2;

  // SUBSCRIBE THIS OBSERVER TO ALL OPERATIONS
  // THAT SYNCHRONIZE Live.Notes (server) INTO Local.Notes (local datastore)
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
    public void editNote(String editorText, INote.NoteType type, INote iNote) {
      if (iNote instanceof Note) {
        Note note = (Note) iNote;
        if (type != note.getNoteType()) {
          note.changeNoteType(type);
        }
        note.setNoteText(editorText);

        Commands.Local.saveEventuallyNote(note);
        getNotesFragment().notesChanged(listify(note));
      }
    }

    @Override
    public void openScripturePicker() {
      commitNewScripturePickerFragment();
    }

    @Override
    public void switchToNextTalk() {
      // applied in MNIActivity
    }

    @Override
    public void switchToPreviousTalk() {
      // applied in MNIActivity
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
      closeScripturePicker();

      getEditorFragment().provideScriptureToEditor(scripture);
    }

    @Override
    public void scriptureCancelled() {
      closeScripturePicker();
    }

    private void closeScripturePicker() {
      final String tag = TAG_SCRIPTURE_PICKER;
      removeFragment(tag);
    }
  };
}
