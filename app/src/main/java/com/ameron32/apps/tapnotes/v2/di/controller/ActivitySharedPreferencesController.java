package com.ameron32.apps.tapnotes.v2.di.controller;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ActivitySharedPreferencesController extends AbsActivityController {

  private static final String SHARED_PREFERENCES_KEY = "SharedPreferencesKey";

  public ActivitySharedPreferencesController(final Activity activity) {
    super(activity);
  }

  public void saveStringPreference(String key, String value) {
    final SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
    final SharedPreferences.Editor editor = prefs.edit();
    editor.putString(key, value);
    editor.commit();
  }

  public void saveBooleanPreference(String key, boolean value) {
    final SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
    final SharedPreferences.Editor editor = prefs.edit();
    editor.putBoolean(key, value);
    editor.commit();
  }

  public void saveIntPreference(String key, int value) {
    final SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
    final SharedPreferences.Editor editor = prefs.edit();
    editor.putInt(key, value);
    editor.commit();
  }

  public String restoreStringPreference(String key, String defaultValue) {
    final SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
    return prefs.getString(key, defaultValue);
  }

  public boolean restoreBooleanPreference(String key, boolean defaultValue) {
    final SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
    return prefs.getBoolean(key, defaultValue);
  }

  public int restoreIntPreference(String key, int defaultValue) {
    final SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
    return prefs.getInt(key, defaultValue);
  }

  /**
   * @param key -- Unique key. If SharedPreference is 'true', runnable will not run.
   * @param runOnce -- Runnable that returns true if it was successful.
   * @return TRUE if runnable has runOnce. FALSE if it failed to run.
   */
  public boolean runOnce(final String key, final SuccessfulRunnable runOnce) {
    // returns isSuccessful

    if (runOnce == null) {
      // failed
      return false;
    }

    if (restoreBooleanPreference(key, false)) {
      // already happened
      return true;
    }

    // TODO: prepare

    if (!runOnce.run()) {
      // SuccessfulRunnable failed
      return false;
    }

    // success!
    saveBooleanPreference(key, true);
    return true;
  }

  public interface SuccessfulRunnable {
    public boolean run();
  }
}
