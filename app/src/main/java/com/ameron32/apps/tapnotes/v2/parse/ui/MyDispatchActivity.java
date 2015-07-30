package com.ameron32.apps.tapnotes.v2.parse.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.ameron32.apps.tapnotes.v2.di.controller.ActivityAlertDialogController;
import com.ameron32.apps.tapnotes.v2.parse.Commands;
import com.ameron32.apps.tapnotes.v2.parse.Constants;
import com.parse.Parse;
import com.parse.ParseConfig;
import com.parse.ParseException;

/**
 * Created by klemeilleur on 6/18/2015.
 */
public abstract class MyDispatchActivity extends Activity {

  protected abstract Class<?> getTargetClass();

  protected abstract Class<?> getLoginActivityClass();

  private static final int LOGIN_REQUEST = 0;
  private static final int TARGET_REQUEST = 1;

  private static final String LOG_TAG = "MyDispatchActivity";

  @Override
  final protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    runDispatch();
  }

  private final Context getContext() {
    return MyDispatchActivity.this;
  }

  private void checkVersionAgainstMinimumOnTheServer() {
    try {
      final float minimumVersion = ParseConfig.get()
          .getNumber(Constants.CONFIG_MINIMUM_VERSION).floatValue();
      final float clientVersion = getClientApplicationVersion();
      Log.d(MyDispatchActivity.class.getSimpleName(), "clientVersion: " + clientVersion + " | minimumVersion: " + minimumVersion);
      if (minimumVersion > clientVersion) {
        final String message = "This version of TAP Notes ( " + clientVersion + " ) " +
            "is older than the minimum version compatible with the server ( " + minimumVersion + " ). " +
            "Please update your version to continue using TAP Notes. " +
            "Our humblest apologies for any inconvenience this may have caused you.";
        new ActivityAlertDialogController(this).showInterruptDialog("Outdated Version", message,
            new Runnable() {
              @Override
              public void run() {
                finish();
              }
            });
      } else {
        // proceed to application
        startActivityForResult(new Intent(this, getTargetClass()), TARGET_REQUEST);
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  private float getClientApplicationVersion() {
    try {
      PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
      return Float.valueOf(pInfo.versionName);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return 0.0f;
  }

  @Override
  final protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    setResult(resultCode);
    if (requestCode == LOGIN_REQUEST && resultCode == RESULT_OK) {
      runDispatch();
    } else {
      finish();
    }
  }

  private void checkVersionThenStartActivity() {
    checkVersionAgainstMinimumOnTheServer();
  }

  private void runDispatch() {
    if (Commands.Local.getClientUser() != null) {
      debugLog("user logged in " + getTargetClass());
      checkVersionThenStartActivity();
    } else {
      debugLog("user not logged in" + getTargetClass());
      startActivityForResult(new Intent(this, getLoginActivityClass()), LOGIN_REQUEST);
    }
  }

  private void debugLog(String message) {
    if (Parse.getLogLevel() <= Parse.LOG_LEVEL_DEBUG &&
        Log.isLoggable(LOG_TAG, Log.DEBUG)) {
      Log.d(LOG_TAG, message);
    }
  }
}
