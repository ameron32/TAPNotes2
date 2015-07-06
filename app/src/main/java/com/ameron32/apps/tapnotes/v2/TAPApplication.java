package com.ameron32.apps.tapnotes.v2;

import android.app.Application;
import android.content.res.Resources;

import com.ameron32.apps.tapnotes.v2.di.Injector;
import com.ameron32.apps.tapnotes.v2.di.module.ApplicationModule;
import com.ameron32.apps.tapnotes.v2.di.module.DefaultAndroidApplicationModule;
import com.ameron32.apps.tapnotes.v2.di.module.DemoApplicationModule;
import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.parse.object._NoteObject;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.parse.object.Talk;
import com.ameron32.apps.tapnotes.v2.parse.object._TestObject;
import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import net.danlew.android.joda.JodaTimeAndroid;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by klemeilleur on 6/12/2015.
 */
public class TAPApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    initializeAppModules_Dagger1(this);
    initializeTimber();
    initializeParse(this);
    initializeCalligraphy();
    initializeJodaTimeAndroid(this);
  }

  private void initializeTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    } else {
      // TODO Crashlytics.start(this);
      Fabric.with(this, new Crashlytics());
      Timber.plant(new CrashlyticsTree());
    }
  }

  private void initializeAppModules_Dagger1(Application app) {
//    Injector.INSTANCE.init(new RootModule());
    Injector.INSTANCE.init(new DefaultAndroidApplicationModule(app));
    Injector.INSTANCE.init(new ApplicationModule(app));
    Injector.INSTANCE.init(new DemoApplicationModule());
    Injector.INSTANCE.inject(app);
  }

  private void initializeCalligraphy() {
    CalligraphyConfig.initDefault(
        new CalligraphyConfig.Builder()
            .setDefaultFontPath("fonts/LiberationSans-Regular.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build());
  }

  public void initializeParse(Application app) {
    final Resources r = app.getResources();

    // Register Custom ParseObjects
    ParseObject.registerSubclass(_TestObject.class);
    ParseObject.registerSubclass(_NoteObject.class);

    ParseObject.registerSubclass(Program.class);
    ParseObject.registerSubclass(Talk.class);
    ParseObject.registerSubclass(Note.class);

    // Enable Local Data Store
    Parse.enableLocalDatastore(app);

    // Enable Crash Reporting
    ParseCrashReporting.enable(app);
    Parse.initialize(app, r.getString(R.string.APPLICATION_ID), r.getString(R.string.CLIENT_KEY));

    // Save the current Installation to Parse.
    ParseInstallation.getCurrentInstallation().saveInBackground(
        new SaveCallback() {
      @Override public void done(
          ParseException e) {
        // PushService.setDefaultPushCallback(
        // ParseApplication.this,
        // ParseStarterProjectActivityUnused.class);
      }
    });
    Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
  }

  private void initializeJodaTimeAndroid(Application app) {
    JodaTimeAndroid.init(app);
  }
}
