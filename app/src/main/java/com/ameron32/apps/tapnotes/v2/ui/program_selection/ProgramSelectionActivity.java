package com.ameron32.apps.tapnotes.v2.ui.program_selection;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.ameron32.apps.tapnotes.v2.data.DataManager;
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
import com.ameron32.apps.tapnotes.v2.scripture.Bible;
import com.ameron32.apps.tapnotes.v2.scripture.ScriptureFinder;
import com.ameron32.apps.tapnotes.v2.ui.MNIActivity;
import com.ameron32.apps.tapnotes.v2.ui.mc_sanitizer.Sanitizer;
import com.ameron32.apps.tapnotes.v2.util.NetworkUtil;

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

  @Inject
  ApplicationThemeController themeController;
  @Inject
  DataManager dataManager;

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

  @Override
  public void startActivity(final String programId) {
    mProgramId = programId;
    Observable<Progress> observable;
    observable = dataManager.pinAllNewClientOwnedNotesFor(mProgramId);
    cache2 = bindLifecycle(observable, DESTROY).cache();
    cache2.subscribe(downloadNotesObserver);
  }

  @Override
  public void downloadProgram(String programId) {
    if (!NetworkUtil.isNetworkConnected(getActivity())) {
      final String message = "Server connection failed. Please check that you have an internet connection. Also, you must be logged into TAP Notes.";
//      snackBarController.toast(message);
      dialog.showInformationDialog("Connection Unavailable", message);
      return;
    }

    mProgramId = programId;
    onLoading();
    cache = bindLifecycle(dataManager.pinProgramAndTalks(mProgramId), DESTROY).cache();
    cache.subscribe(downloadProgramObserver);
  }

  @Override
  public void refreshProgramNotes(String programId) {
    if (!NetworkUtil.isNetworkConnected(getActivity())) {
      final String message = "Server connection failed. Please check that you have an internet connection. Also, you must be logged into TAP Notes.";
//      snackBarController.toast(message);
      dialog.showInformationDialog("Connection Unavailable", message);
      return;
    }

    mProgramId = programId;
    onLoading();
    cache4 = bindLifecycle(dataManager.unpinProgramAndTalksAndNotesThenRepin(mProgramId), DESTROY).cache();
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
