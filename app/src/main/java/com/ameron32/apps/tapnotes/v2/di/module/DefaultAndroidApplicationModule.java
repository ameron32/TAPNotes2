package com.ameron32.apps.tapnotes.v2.di.module;

/*
 * Copyright 2013 Philip Schiffer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.nfc.NfcManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.storage.StorageManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;

import com.ameron32.apps.tapnotes.v2.data.DataManager;
import com.ameron32.apps.tapnotes.v2.di.ForApplication;
import com.ameron32.apps.tapnotes.v2.di.controller.ApplicationThemeController;
import com.ameron32.apps.tapnotes.v2.di.controller.NotesController;
import com.ameron32.apps.tapnotes.v2.scripture.Bible;
import com.ameron32.apps.tapnotes.v2.scripture.BibleBuilder;
import com.ameron32.apps.tapnotes.v2.scripture.BibleResourceNotFoundException;
import com.ameron32.apps.tapnotes.v2.scripture.ScriptureFinder;
import com.ameron32.apps.tapnotes.v2.ui.mc_sanitizer.Sanitizer;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Default application module which provides standard android classes.
 * <br/>
 * Use it like this:
 * <p><blockquote><pre>
 * {@literal @}Module(includes = { DefaultAndroidApplicationModule.class })
 * </pre></blockquote><p>
 */
@Module(library = true)
public class DefaultAndroidApplicationModule {

  private final Application mApplication;

  public DefaultAndroidApplicationModule(final Application application) {
    mApplication = application;
  }

  // ==========================================================================================================================
  // Global Android Resources
  // ==========================================================================================================================

  @Provides
  @Singleton
  Application provideApplication() {
    return mApplication;
  }

  @Provides
  @Singleton
  @ForApplication
  Context provideApplicationContext() {
    return mApplication;
  }

  @Provides
  @Singleton
  @ForApplication
  Resources provideResources() {
    return mApplication.getResources();
  }

  // ==========================================================================================================================
  // Persistence
  // ==========================================================================================================================

  @Provides
  @Singleton
  @ForApplication
  SharedPreferences provideSharedPreferences(final Application application) {
    return PreferenceManager.getDefaultSharedPreferences(application);
  }

  @Provides
  @Singleton
  AssetManager provideAssetManager(final Application application) {
    return application.getAssets();
  }

  // ==========================================================================================================================
  // Android Services
  // ==========================================================================================================================

  @Provides
  @Singleton
  AccountManager provideAccountManager(final Application application) {
    return getSystemService(application, Context.ACCOUNT_SERVICE);
  }

  @Provides
  @Singleton
  ActivityManager provideActivityManager(final Application application) {
    return getSystemService(application, Context.ACTIVITY_SERVICE);
  }

  @Provides
  @Singleton
  AlarmManager provideAlarmManager(final Application application) {
    return getSystemService(application, Context.ALARM_SERVICE);
  }

  @Provides
  @Singleton
  AudioManager provideAudioManager(final Application application) {
    return getSystemService(application, Context.AUDIO_SERVICE);
  }

  @Provides
  @Singleton
  ClipboardManager provideClipboardManager(final Application application) {
    return getSystemService(application, Context.CLIPBOARD_SERVICE);
  }

  @Provides
  @Singleton
  ConnectivityManager provideConnectivityManager(final Application application) {
    return getSystemService(application, Context.CONNECTIVITY_SERVICE);
  }

  @Provides
  @Singleton
  DownloadManager provideDownloadManager(final Application application) {
    return getSystemService(application, Context.DOWNLOAD_SERVICE);
  }

  @Provides
  @Singleton
  InputManager provideInputManager(final Application application) {
    return getSystemService(application, Context.INPUT_SERVICE);
  }

  @Provides
  @Singleton
  LocationManager provideLocationManager(final Application application) {
    return getSystemService(application, Context.LOCATION_SERVICE);
  }

  @Provides
  @Singleton
  NfcManager provideNfcManager(final Application application) {
    return getSystemService(application, Context.NFC_SERVICE);
  }

  @Provides
  @Singleton
  NotificationManager provideNotificationManager(final Application application) {
    return getSystemService(application, Context.NOTIFICATION_SERVICE);
  }

  @Provides
  @Singleton
  PowerManager providePowerManager(final Application application) {
    return getSystemService(application, Context.POWER_SERVICE);
  }

  @Provides
  @Singleton
  SearchManager provideSearchManager(final Application application) {
    return getSystemService(application, Context.SEARCH_SERVICE);
  }

  @Provides
  @Singleton
  SensorManager provideSensorManager(final Application application) {
    return getSystemService(application, Context.SENSOR_SERVICE);
  }

  @Provides
  @Singleton
  StorageManager provideStorageManager(final Application application) {
    return getSystemService(application, Context.STORAGE_SERVICE);
  }

  @Provides
  @Singleton
  TelephonyManager provideTelephonyManager(final Application application) {
    return getSystemService(application, Context.TELEPHONY_SERVICE);
  }

  @Provides
  @Singleton
  UsbManager provideUsbManager(final Application application) {
    return getSystemService(application, Context.USB_SERVICE);
  }

  @Provides
  @Singleton
  Vibrator provideVibrator(final Application application) {
    return getSystemService(application, Context.VIBRATOR_SERVICE);
  }

  @Provides
  @Singleton
  WifiManager provideWifiManager(final Application application) {
    return getSystemService(application, Context.WIFI_SERVICE);
  }

  //

  @Provides
  @Singleton
  @ForApplication
  LayoutInflater provideLayoutInflater() {
    return (LayoutInflater) mApplication.getSystemService(LAYOUT_INFLATER_SERVICE);
  }

  @Provides
  @Singleton
  Bus provideOttoEventBusOnUIThread() {
    return new Bus(ThreadEnforcer.MAIN);
  }

  @Provides
  @Singleton
  ApplicationThemeController provideThemeController(final Application application) {
    return new ApplicationThemeController(application);
  }

  @Provides
  @Singleton
  DataManager provideDataManager(final Application application) {
    return new DataManager();
  }

  @Provides
  @Singleton
  NotesController provideNotesController(final Application application) {
    return new NotesController(application);
  }

  @Provides
  @Singleton
  Bible provideBible(final Application application) {
    try {
      return new BibleBuilder(application).getBible();
    } catch (BibleResourceNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Provides
  @Singleton
  Sanitizer provideSanitizer(final Application application) {
    // TODO update to Bible instead of Context as soon as Sanitizer is repaired
    return new Sanitizer(application);
  }

  @Provides
  @Singleton
  ScriptureFinder provideScriptureFinder() {
    // TODO update parameters if needed
    return new ScriptureFinder();
  }

  // ==========================================================================================================================
  // Private API
  // ==========================================================================================================================

  private <T> T getSystemService(final Application application, final String service) {
    //noinspection unchecked
    return (T) application.getSystemService(service);
  }
}
