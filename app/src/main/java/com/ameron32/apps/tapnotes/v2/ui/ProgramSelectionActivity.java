package com.ameron32.apps.tapnotes.v2.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.parse.Commands;
import com.ameron32.apps.tapnotes.v2.data.parse.Constants;
import com.ameron32.apps.tapnotes.v2.data.parse.ParseHelper;
import com.ameron32.apps.tapnotes.v2.data.parse.ParseUtil;
import com.ameron32.apps.tapnotes.v2.data.parse.Queries;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Note;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Program;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Talk;
import com.ameron32.apps.tapnotes.v2.di.controller.NotesController;
import com.ameron32.apps.tapnotes.v2.frmk.object.Progress;
import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.di.ForApplication;
import com.ameron32.apps.tapnotes.v2.di.controller.ActivityAlertDialogController;
import com.ameron32.apps.tapnotes.v2.di.controller.ActivitySnackBarController;
import com.ameron32.apps.tapnotes.v2.di.controller.ApplicationThemeController;
import com.ameron32.apps.tapnotes.v2.di.module.ActivityModule;
import com.ameron32.apps.tapnotes.v2.di.module.DefaultAndroidActivityModule;
import com.ameron32.apps.tapnotes.v2.frmk.IProgramList;
import com.ameron32.apps.tapnotes.v2.frmk.TAPActivity;
import com.ameron32.apps.tapnotes.v2.data.parse.Status;
import com.ameron32.apps.tapnotes.v2.scripture.Bible;
import com.ameron32.apps.tapnotes.v2.scripture.ScriptureFinder;
import com.ameron32.apps.tapnotes.v2.ui.fragment.ProgramSelectionFragment;
import com.ameron32.apps.tapnotes.v2.ui.mc_sanitizer.Sanitizer;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import dagger.Lazy;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static rx.android.lifecycle.LifecycleEvent.*;

public class ProgramSelectionActivity
    extends TAPActivity
    implements ProgramSelectionFragment.Callbacks
{
//  void _perform2016upload() {
//    new AsyncTask<Void, Void, Void>() {
//
//      final String[][] parse = new String[][] {{"a001","10","7/1/2016 9:20","Music-Video Presentation","","","Music"},{"a002","10","7/1/2016 9:30","Song Number 102 and Prayer","","#102","Song"},{"a003","30","7/1/2016 9:40","Jehovah Deserves \"Undivided Loyalty\"","(1 Chronicles 12:33; Psalm 86:11)","Chairman's Address: ","SymposiumTalk"},{"a004","20","7/1/2016 10:10","Thought!","(2 Corinthians 10:5)","Symposium: Maintain Loyalty in ...","SymposiumTalk"},{"a005","20","7/1/2016 10:30","Word!","(Numbers 14:2, 26, 27)","Symposium: Maintain Loyalty in ...","SymposiumTalk"},{"a006","15","7/1/2016 10:45","Action!","(Psalm 37:27, 28)","Symposium: Maintain Loyalty in ...","SymposiumTalk"},{"a007","10","7/1/2016 11:05","Song Number 66 and Announcements","","#66 ","Song"},{"a008","30","7/1/2016 11:15","\"Who Is on Jehovah's Side?\"","(Exodus 20:1-7; 24:3-18; 32:1-35; 34:1-14)","Dramatic Bible Reading: ","SymposiumTalk"},{"a009","30","7/1/2016 11:45","Jehovah's \"Loyal Love Is Better Than Life\"","(Psalm 63:3; Revelation 15:4)","","Talk"},{"a010","5","7/1/2016 12:15","Song Number 97 and Intermission","","#97 ","Song"},{"a011","10","7/1/2016 13:25","Music-Video Presentation","","","Music"},{"a012","5","7/1/2016 13:35","Song Number 18","","#18","Song"},{"a013","15","7/1/2016 13:40","When Young","(Acts 2:27)(Luke 2:51, 52)","Symposium: Be Loyal, as Jesus Was","SymposiumTalk"},{"a014","12","7/1/2016 13:52","When Persecuted","(Acts 2:27)(1 Peter 2:21-23)","Symposium: Be Loyal, as Jesus Was","SymposiumTalk"},{"a015","12","7/1/2016 14:04","When Tempted","(Acts 2:27)(Matthew 4:3-10)","Symposium: Be Loyal, as Jesus Was","SymposiumTalk"},{"a016","12","7/1/2016 14:16","When Praised","(Acts 2:27)(Mark 10:17, 18)","Symposium: Be Loyal, as Jesus Was","SymposiumTalk"},{"a017","12","7/1/2016 14:28","When Tired","(Acts 2:27)(Mark 6:31-34; John 4:6, 34)","Symposium: Be Loyal, as Jesus Was","SymposiumTalk"},{"a018","12","7/1/2016 14:40","When Abandoned","(Acts 2:27)(John 16:32)","Symposium: Be Loyal, as Jesus Was","SymposiumTalk"},{"a019","15","7/1/2016 14:55","When Facing Death","(Acts 2:27)(Hebrews 12:2)","Symposium: Be Loyal, as Jesus Was","SymposiumTalk"},{"a020","10","7/1/2016 15:10","Song Number 29 and Announcements","","#29 ","Song"},{"a021","10","7/1/2016 15:20","Hate What Jehovah Hates","(Psalm 97:10; 119:104)","Symposium: Loyally Uphold Jehovah's Judgments","SymposiumTalk"},{"a022","10","7/1/2016 15:30","Shun Unrepentant Wrongdoers","(1 Corinthians 5:11-13)","Symposium: Loyally Uphold Jehovah's Judgments","SymposiumTalk"},{"a023","15","7/1/2016 15:45","Be Forgiving","(Psalm 86:5; Matthew 18:35)","Symposium: Loyally Uphold Jehovah's Judgments","SymposiumTalk"},{"a024","20","7/1/2016 15:55","Loyalty—Part of the New Personality","(Ephesians 4:23, 24)","","Talk"},{"a025","35","7/1/2016 16:15","How Christ's Loyalty as High Priest Helps Us","(Hebrews 7:26)","","Talk"},{"a026","5","7/1/2016 16:50","Song Number 108 and Closing Prayer","","#108","Song"},{"b001","10","7/2/2016 9:20","Music-Video Presentation","","","Music"},{"b002","10","7/2/2016 9:30","Song Number 145 and Prayer","","#145","Song"},{"b003","9","7/2/2016 9:40","Unbelieving Mates","(1 Peter 3:1, 2)","Symposium: Helping Others to Become Loyal to Jehovah","SymposiumTalk"},{"b004","7","7/2/2016 9:47","Former Bible Students","(Acts 26:28, 29)","Symposium: Helping Others to Become Loyal to Jehovah","SymposiumTalk"},{"b005","7","7/2/2016 9:54","Inactive Ones","(1 Peter 2:25)","Symposium: Helping Others to Become Loyal to Jehovah","SymposiumTalk"},{"b006","9","7/2/2016 10:03","Relatives","(John 1:40-42)","Symposium: Helping Others to Become Loyal to Jehovah","SymposiumTalk"},{"b007","10","7/2/2016 10:15","Adam","(1 Timothy 2:14)","Symposium: Do Not Imitate Disloyal Ones","SymposiumTalk"},{"b008","10","7/2/2016 10:25","Absalom","(2 Samuel 15:2-6)","Symposium: Do Not Imitate Disloyal Ones","SymposiumTalk"},{"b009","10","7/2/2016 10:35","Solomon","(1 Kings 11:4-10)","Symposium: Do Not Imitate Disloyal Ones","SymposiumTalk"},{"b010","10","7/2/2016 10:45","Judas Iscariot","(Matthew 26:14-16)","Symposium: Do Not Imitate Disloyal Ones","SymposiumTalk"},{"b011","10","7/2/2016 10:55","Song Number 86 and Announcements","","#86 ","Song"},{"b012","10","7/2/2016 11:05","Ruth","(Ruth 1:16, 17)","Symposium: Imitate Loyal Ones","SymposiumTalk"},{"b013","10","7/2/2016 11:15","David","(2 Samuel 23:14-17)","Symposium: Imitate Loyal Ones","SymposiumTalk"},{"b014","10","7/2/2016 11:25","Hushai","(2 Samuel 15:30-37)","Symposium: Imitate Loyal Ones","SymposiumTalk"},{"b015","10","7/2/2016 11:35","Shiphrah and Puah","(Exodus 1:15-21)","Symposium: Imitate Loyal Ones","SymposiumTalk"},{"b016","30","7/2/2016 11:45","Never Abandon Your Loyal Love for Jehovah","(Psalm 89:33)","Baptism: ","SymposiumTalk"},{"b017","5","7/2/2016 12:15","Song Number 7 and Intermission","","#7","Song"},{"b018","10","7/2/2016 13:35","Music-Video Presentation","","","Music"},{"b019","5","7/2/2016 13:45","Song Number 106","","#106","Song"},{"b020","8","7/2/2016 13:50","Loyal Despite Direct Attacks","(Job 1:6-12)","Symposium: Lessons on Loyalty From the Book of Job","SymposiumTalk"},{"b021","5","7/2/2016 13:55","Loyal Despite Subtle Attacks","(Job 2:11)","Symposium: Lessons on Loyalty From the Book of Job","SymposiumTalk"},{"b022","5","7/2/2016 14:00","What We Learn From Natural Forces","(Job 37:1–38:41)","Symposium: Lessons on Loyalty From the Book of Job","SymposiumTalk"},{"b023","7","7/2/2016 14:07","What the Animal Creation Teaches Us","(Job 39:1–41:34)","Symposium: Lessons on Loyalty From the Book of Job","SymposiumTalk"},{"b024","10","7/2/2016 15:15","Song Number 123 and Announcements","","#123","Song"},{"b025","50","7/2/2016 15:25","\"Hope for What We Do Not See\"","(Romans 8:25)","Drama: ","SymposiumTalk"},{"b026","35","7/2/2016 16:15","Keep Waiting Eagerly With Endurance!","(2 Corinthians 4:16-18)","","Talk"},{"b027","5","7/2/2016 16:50","Song Number 105 and Closing Prayer","","#105","Song"},{"c001","10","7/3/2016 9:20","Music-Video Presentation","","","Music"},{"c002","10","7/3/2016 9:30","Song Number 61 and Prayer","","#61 ","Song"},{"c003","10","7/3/2016 9:40","Pride","(1 Peter 5:5, 6)","Symposium: Avoid What Erodes Loyalty","SymposiumTalk"},{"c004","10","7/3/2016 9:50","Improper Entertainment","(Galatians 6:7, 8)","Symposium: Avoid What Erodes Loyalty","SymposiumTalk"},{"c005","10","7/3/2016 10:00","Bad Associations","(Proverbs 13:20; 1 Corinthians 15:33)","Symposium: Avoid What Erodes Loyalty","SymposiumTalk"},{"c006","15","7/3/2016 10:15","Fear of Man","(Proverbs 29:25)","Symposium: Avoid What Erodes Loyalty","SymposiumTalk"},{"c007","10","7/3/2016 10:25","Appreciation","(Psalm 116:12, 14)","Symposium: Pursue What Builds Loyalty","SymposiumTalk"},{"c008","10","7/3/2016 10:35","Self-Control","(Galatians 5:16, 22, 23)","Symposium: Pursue What Builds Loyalty","SymposiumTalk"},{"c009","10","7/3/2016 10:45","Love","(1 Corinthians 10:24; Colossians 3:14)","Symposium: Pursue What Builds Loyalty","SymposiumTalk"},{"c010","15","7/3/2016 11:00","Faith","(Hebrews 11:1, 17-19)","Symposium: Pursue What Builds Loyalty","SymposiumTalk"},{"c011","10","7/3/2016 11:10","Song Number 133 and Announcements","","#133","Song"},{"c012","30","7/3/2016 11:20","When Will Loyal Love Triumph Over Hatred?","(Matthew 5:38-45)","Public Bible Discourse: ","SymposiumTalk"},{"c013","30","7/3/2016 11:50","Summary of The Watchtower","","","Talk"},{"c014","5","7/3/2016 12:20","Song Number 125 and Intermission","","#125","Song"},{"c015","10","7/3/2016 13:35","Music-Video Presentation","","","Music"},{"c016","5","7/3/2016 13:45","Song Number 49","","#49","Song"},{"c017","50","7/3/2016 13:50","\"O Jehovah, ... I Trust in You\"","(Psalm 25:1, 2; 2 Kings 16:1–19:37; 2 Chronicles 31:1–32:33)","Drama: ","SymposiumTalk"},{"c018","10","7/3/2016 14:40","Song Number 131 and Announcements","","#131","Song"},{"c019","55","7/3/2016 14:50","\"Jehovah Will Treat His Loyal One in a Special Way\"","(Psalm 4:3, 7, 8)","","Talk"},{"c020","5","7/3/2016 15:45","Song Number 63 and Closing Prayer","","#63 ","Song"}};
//
//      @Override
//      protected Void doInBackground(Void... params) {
//        try {
//          final Program program = Queries.Live.pinProgram(Constants.CONVENTION2016_PROGRAM_OBJECT_ID);
//          if (program == null) { return null; }
//
//          String[] rowA;
//          String colA;
//          Talk talk;
//          List<Talk> newTalks = new ArrayList<>();
//          for (int row = 0; row < parse.length; row++) {
//            rowA = parse[row];
//            talk = Talk.create();
//            newTalks.add(talk);
//            talk.put("oProgram", program);
//
//            for (int col = 0; col < rowA.length; col++) {
//              colA = rowA[col];
//              Log.v("_xx", "test count: " + row + " " + col);
//
//              switch(col) {
//                case 0:
//                  // sequence
//talk.put("sequence", colA);
//                  break;
//                case 1:
//                  // durationMinutes
//talk.put("durationMinutes", Integer.valueOf(colA));
//                  break;
//                case 2:
//                  // date
//talk.put("date", colA);
//                  break;
//                case 3:
//                  // title
//talk.put("title", colA);
//                  break;
//                case 4:
//                  // scriptures
//
//                  break;
//                case 5:
//                  // metadata
//talk.put("metadata", colA);
//                  break;
//                case 6:
//                  // type
//talk.put("type", colA);
//                  break;
//              }
//            }
//          }
//
//          Talk.saveAll(newTalks);
//        } catch (ParseException e) {
//          e.printStackTrace();
//        }
//        return null;
//      }
//
//      @Override
//      protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//        snackBarController.toast("upload 2016 talks done");
//      }
//    }.execute();
//  }


  @Inject
  ApplicationThemeController themeController;

  private IProgramList programList;
  private ActivityAlertDialogController dialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // setContentView() handled in super.onCreate()
    snackBarController = new ActivitySnackBarController(this);
    dialog = new ActivityAlertDialogController(this);
  }

  @Override
  protected void onResume() {
    super.onResume();

    cache3 = bindLifecycle(buildBibleObservable(), DESTROY).cache();
    cache3.subscribe(bibleLoadingObserver);
  }

  private final Observer<Progress> bibleLoadingObserver = new Observer<Progress>() {

    @Override
    public void onCompleted() {
//      snackBarController.toast("Bible loaded.");
    }

    @Override
    public void onError(Throwable e) {
      e.printStackTrace();
    }

    @Override
    public void onNext(Progress progress) {
      onProgress(progress);
    }
  };

  @Inject Lazy<Bible> mBible;
  @Inject Lazy<Sanitizer> mSanitizer;
  @Inject Lazy<ScriptureFinder> mFinder;

  private Observable<Progress> buildBibleObservable() {
    return Observable.create(new Observable.OnSubscribe<Progress>() {
      @Override
      public void call(Subscriber<? super Progress> subscriber) {
        subscriber.onNext(new Progress(0, 3, false, "Loading Bible", "Loading Bible... (may take several minutes)"));
        mBible.get();
        subscriber.onNext(new Progress(1, 3, false, "Loading Bible", "Bible complete. Loading user input module..."));
        mSanitizer.get();
        subscriber.onNext(new Progress(2, 3, false, "Loading Bible", "Input module complete. Loading reference lookup..."));
        mFinder.get();
        subscriber.onNext(new Progress(3, 3, false, "Loading Bible", "Complete!"));
        subscriber.onCompleted();
      }
    }).subscribeOn(Schedulers.io());
  }

  @Override
  @LayoutRes
  protected int getLayoutResource() {
    // rather than setContentView(), provide inflatable layout here
    return R.layout.activity_program_selection;
  }

  @Override
  protected void onDestroy() {
    snackBarController = null;
    super.onDestroy();
  }

  @Override
  public void onAttachFragment(Fragment fragment) {
    super.onAttachFragment(fragment);
    final Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment);
    if (f != null && f instanceof IProgramList) {
      programList = (IProgramList) f;
    }
  }

  @Override
  protected int provideThemeResource() {
    return themeController.getTheme();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_program_selection, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
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



  ActivitySnackBarController snackBarController;

  @Override
  protected Object[] getModules() {
    return new Object[] {
        new ActivityModule(this),
        new DefaultAndroidActivityModule(this)
    };
  }

  private void onLoading() {
//    snackBarController.toast("Downloading Program...");
  }

  private void onProgress(Progress progress) {
    programList.setProgramProgress(mProgramId, progress);
//    snackBarController.toast("Step " + progress.item + " of " + progress.total + " complete.");
  }

  private void onLoaded() {
//    snackBarController.toast("Complete!");
    if (programList != null) {
      programList.setProgramDownloaded(mProgramId);
    }
  }



  private Observable<Progress> cache;
  private Observable<Progress> cache2;
  private Observable<Progress> cache3;
  private Observable<Progress> cache4;
  private String mProgramId;

  @Inject
  NotesController notesController;

  @Override
  public void startActivity(final String programId) {
    mProgramId = programId;
    Observable<Progress> observable;
    observable = notesController.pinAllNewClientOwnedNotesFor(mProgramId);
    cache2 = bindLifecycle(observable, DESTROY).cache();
    cache2.subscribe(downloadNotesObserver);
  }

  @Override
  public void downloadProgram(String programId) {
    if (!Status.isConnectionToServerAvailable(getActivity())) {
      final String message = "Server connection failed. Please check that you have an internet connection. Also, you must be logged into TAP Notes.";
//      snackBarController.toast(message);
      dialog.showInformationDialog("Connection Unavailable", message);
      return;
    }

    mProgramId = programId;
    onLoading();
    cache = bindLifecycle(notesController.pinProgramAndTalks(mProgramId), DESTROY).cache();
    cache.subscribe(downloadProgramObserver);
  }

  @Override
  public void refreshProgramNotes(String programId) {
    if (!Status.isConnectionToServerAvailable(getActivity())) {
      final String message = "Server connection failed. Please check that you have an internet connection. Also, you must be logged into TAP Notes.";
//      snackBarController.toast(message);
      dialog.showInformationDialog("Connection Unavailable", message);
      return;
    }

    mProgramId = programId;
    onLoading();
    cache4 = bindLifecycle(notesController.unpinProgramAndTalksAndNotesThenRepin(mProgramId), DESTROY).cache();
    cache4.subscribe(downloadProgramObserver);
  }

  @Inject
  @ForApplication
  Resources mResources;

  private final Observer<Progress> downloadProgramObserver = new Observer<Progress>() {

    @Override
    public void onCompleted() {
      // proceed to activity
      if (mProgramId == null || failed.get()) {
        snackBarController.toast(mResources.getString(R.string.program_failed_to_load));
        return;
      } else {
        onLoaded();
      }
    }

    @Override
    public void onError(Throwable e) {
      // error running task
      // notify user of failure to process to activity
      fail();
      e.printStackTrace();
    }

    public void fail() {
      // FAIL
      failed.set(true);
    }

    final AtomicBoolean failed = new AtomicBoolean(false);

    @Override
    public void onNext(Progress progress) {
      if (progress.failed) {
        fail();
      }
      // task complete OR piece of task complete
      onProgress(progress);
    }
  };

  private final Observer<Progress> downloadNotesObserver = new Observer<Progress>() {

    @Override
    public void onCompleted() {
      if (mProgramId == null || failed.get()) {
        snackBarController.toast(mResources.getString(R.string.program_failed_to_load));
        return;
      }
      else {
        startActivity(MNIActivity.makeIntent(getContext(), mProgramId));
      }
    }

    @Override
    public void onError(Throwable e) {
      fail();
      e.printStackTrace();
    }

    public void fail() {
      // FAIL
      failed.set(true);
    }

    final AtomicBoolean failed = new AtomicBoolean(false);

    @Override
    public void onNext(Progress progress) {
      if (progress.failed) {
        fail();
      }
      onProgress(progress);
    }
  };
}
