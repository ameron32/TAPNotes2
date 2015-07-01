package com.ameron32.apps.tapnotes.v2.di.controller;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ameron32.apps.tapnotes.v2.R;

/**
 * Created by klemeilleur on 7/1/2015.
 */
public class ApplicationThemeController extends AbsApplicationController {

  public static final int THEME_NOT_FOUND = -2;

  private static final String SHARED_PREFERENCES_KEY = "SharedPreferencesKey";
  private static final String THEME_PREF_KEY = "ThemePreferencesKey";

  public ApplicationThemeController(Application application) {
    super(application);

  }

  public int getTheme() {
    int preferencesTheme = getThemeFromPreferences();
    int packageTheme = getThemeFromPackage(getContext());
    if (preferencesTheme == THEME_NOT_FOUND) {
      return packageTheme;
    }

    return preferencesTheme;
  }

  public void setTheme(int theme) {
    if (theme == THEME_NOT_FOUND) {
      // do nothing
      return;
    }

    switch(theme) {
      case R.style.Teal2015Theme:
      setThemeInPreferences(theme);
    }
  }

  private int getThemeFromPreferences() {
    return restoreIntPreference(THEME_PREF_KEY, THEME_NOT_FOUND);
  }

  private void setThemeInPreferences(int theme) {
    saveIntPreference(THEME_PREF_KEY, theme);
  }

  private int getThemeFromPackage(final Context context) {
    try {
      String packageName = context.getPackageName();
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
      int theme = packageInfo.applicationInfo.theme;
      return theme;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return THEME_NOT_FOUND;
  }

  private void saveIntPreference(String key, int value) {
    final SharedPreferences prefs = getApplication().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
    final SharedPreferences.Editor editor = prefs.edit();
    editor.putInt(key, value);
    editor.commit();
  }

  private int restoreIntPreference(String key, int defaultValue) {
    final SharedPreferences prefs = getApplication().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
    return prefs.getInt(key, defaultValue);
  }
}
