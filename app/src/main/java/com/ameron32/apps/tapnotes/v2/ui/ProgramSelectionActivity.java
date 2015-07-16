package com.ameron32.apps.tapnotes.v2.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.Menu;
import android.view.MenuItem;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.di.ForApplication;
import com.ameron32.apps.tapnotes.v2.di.controller.ActivitySnackBarController;
import com.ameron32.apps.tapnotes.v2.di.controller.ApplicationThemeController;
import com.ameron32.apps.tapnotes.v2.di.module.ActivityModule;
import com.ameron32.apps.tapnotes.v2.di.module.DefaultAndroidActivityModule;
import com.ameron32.apps.tapnotes.v2.frmk.TAPActivity;
import com.ameron32.apps.tapnotes.v2.parse.Queries;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.ui.fragment.ProgramSelectionFragment;
import com.parse.ParseException;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static rx.android.lifecycle.LifecycleEvent.*;

public class ProgramSelectionActivity
    extends TAPActivity
    implements ProgramSelectionFragment.TestCallbacks,
      Observer<ProgramSelectionActivity.Progress>
{

  @Inject
  ApplicationThemeController themeController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // setContentView() handled in super.onCreate()
    snackBarController = new ActivitySnackBarController(this);
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

  void onLoading() {
    snackBarController.toast("Starting...");
  }

  void onProgress(Progress progress) {
    if (progress.failed) {
      fail();
    }
    snackBarController.toast(progress.item + " of " + progress.total);
  }

  void onLoaded() {
    snackBarController.toast("Complete!");
  }






  String mProgramId;

  @Override
  public void startMNIActivity(final String programId) {
    // TODO precache Talks and Notes from Parse
    mProgramId = programId;
    onLoading();
    cache = bindLifecycle(getProgressObservable(), DESTROY).cache();
    cache.subscribe(ProgramSelectionActivity.this);
  }

  private Observable<ProgramSelectionActivity.Progress> cache;

  private Observable<ProgramSelectionActivity.Progress> getProgressObservable() {
    return Observable.create(new Observable.OnSubscribe<Progress>() {
      @Override
      public void call(Subscriber<? super Progress> subscriber) {
        try {
          // PRECACHE TALKS AND NOTES
          Thread.sleep(200);

          final Program program = Queries.Live.pinProgram(mProgramId);
          subscriber.onNext(new Progress(1, 3, false));
          Thread.sleep(200);

          Queries.Live.pinAllProgramTalksFor(program);
          subscriber.onNext(new Progress(2, 3, false));
          Thread.sleep(200);

          Queries.Live.pinAllClientOwnedNotesFor(program);
          subscriber.onNext(new Progress(3, 3, false));
          subscriber.onCompleted();
        } catch (InterruptedException e) {
          subscriber.onError(e);
        } catch (ParseException e) {
          e.printStackTrace();
          subscriber.onNext(new Progress(0, 0, true));
          subscriber.onCompleted();
        }
      }
    }).subscribeOn(Schedulers.io());
  }



  @Inject
  @ForApplication
  Resources mResources;

  @Override
  public void onCompleted() {
    // proceed to activity
    onLoaded();
    if (mProgramId == null || failed.get()) {
      snackBarController.toast(mResources.getString(R.string.program_failed_to_load));
      return;
    }

    startActivity(MNIActivity.makeIntent(getContext(), mProgramId));
  }

  @Override
  public void onError(Throwable e) {
    // error running task
    // notify user of failure to process to activity
    fail();
  }

  public void fail() {
    // FAIL
    failed.set(true);
  }

  final AtomicBoolean failed = new AtomicBoolean(false);

  @Override
  public void onNext(Progress progress) {
    // task complete OR piece of task complete
    onProgress(progress);
  }

  public static class Progress {
    public int item;
    public int total;
    public boolean failed;

    public Progress(int item, int total, boolean failed) {
      this.item = item;
      this.total = total;
      this.failed = failed;
    }
  }
}
