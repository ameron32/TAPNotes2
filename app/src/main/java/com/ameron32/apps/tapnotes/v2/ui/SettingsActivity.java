package com.ameron32.apps.tapnotes.v2.ui;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.di.controller.ApplicationThemeController;
import com.ameron32.apps.tapnotes.v2.di.module.ActivityModule;
import com.ameron32.apps.tapnotes.v2.di.module.DefaultAndroidActivityModule;

import java.util.List;

import javax.inject.Inject;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

  public static Intent makeIntent(final Context context) {
    final Intent i = new Intent(context, SettingsActivity.class);
    return i;
  }

  /**
   * Determines whether to always show the simplified settings UI, where
   * settings are presented in a single list. When false, settings are shown
   * as a master/detail two-pane view on tablets. When true, a single pane is
   * shown on tablets.
   */
  private static final boolean ALWAYS_SIMPLE_PREFS = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupActionBar();
  }

  /**
   * Set up the {@link android.app.ActionBar}, if the API is available.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  private void setupActionBar() {
//    setSupportActionBar(toolbar);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      NavUtils.navigateUpFromSameTask(this);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);

    setupSimplePreferencesScreen();
  }

  /**
   * Shows the simplified settings UI if the device configuration if the
   * device configuration dictates that a simplified, single-pane UI should be
   * shown.
   */
  private void setupSimplePreferencesScreen() {
    if (!isSimplePreferences(this)) {
      return;
    }

    // In the simplified UI, fragments are not used at all and we instead
    // use the older PreferenceActivity APIs.

    // Add 'general' preferences.
    addPreferencesFromResource(R.xml.pref_general);

    bindPreferenceSummaryToTheme(findPreference("theme_list"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onIsMultiPane() {
    return isXLargeTablet(this) && !isSimplePreferences(this);
  }

  /**
   * Helper method to determine if the device has an extra-large screen. For
   * example, 10" tablets are extra-large.
   */
  private static boolean isXLargeTablet(Context context) {
    return (context.getResources().getConfiguration().screenLayout
        & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
  }

  /**
   * Determines whether the simplified settings UI should be shown. This is
   * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
   * doesn't have newer APIs like {@link PreferenceFragment}, or the device
   * doesn't have an extra-large screen. In these cases, a single-pane
   * "simplified" settings UI should be shown.
   */
  private static boolean isSimplePreferences(Context context) {
    return ALWAYS_SIMPLE_PREFS
        || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
        || !isXLargeTablet(context);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public void onBuildHeaders(List<Header> target) {
    if (!isSimplePreferences(this)) {
      loadHeadersFromResource(R.xml.pref_headers, target);
    }
  }

  @Inject
  ApplicationThemeController themeController;

  @Override
  protected Object[] getModules() {
    return new Object[]{
        new ActivityModule(this),
        new DefaultAndroidActivityModule(this)
    };
  }

  private Preference.OnPreferenceChangeListener mBindPreferenceSummaryToThemeListener;

  public void bindPreferenceSummaryToTheme(Preference preference) {
    if (mBindPreferenceSummaryToThemeListener == null) {
      mBindPreferenceSummaryToThemeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
          String stringValue = value.toString();
          String key = preference.getKey();

          if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference.setSummary(
                index >= 0
                    ? listPreference.getEntries()[index]
                    : ApplicationThemeController.THEME_MISSING);

            Log.d(SettingsActivity.class.getSimpleName(),
                "preference | key: " + key + " value:" + stringValue);
            setApplicationTheme(index);
            setResult(RESULT_OK);
//            SettingsActivity.this.recreate();
          }
          return true;
        }
      };
    }

    // Set the listener to watch for value changes.
    preference.setOnPreferenceChangeListener(mBindPreferenceSummaryToThemeListener);

    // Trigger the listener immediately with the preference's
    // current value.
    mBindPreferenceSummaryToThemeListener.onPreferenceChange(preference,
        PreferenceManager
            .getDefaultSharedPreferences(preference.getContext())
            .getString(preference.getKey(), ApplicationThemeController.THEME_MISSING));
  }

  private void setApplicationTheme(int index) {
    themeController.setTheme(index);
  }

  @Override
  protected boolean isValidFragment(String fragmentName) {
    if (fragmentName.equals(GeneralPreferenceFragment.class.getName())){
      return true;
    } else {
      return false;
    }
  }

  /**
   * This fragment shows general preferences only. It is used when the
   * activity is showing a two-pane settings UI.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static class GeneralPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_general);

      ((SettingsActivity) getActivity()).
          bindPreferenceSummaryToTheme(findPreference("theme_list"));
    }
  }
}
