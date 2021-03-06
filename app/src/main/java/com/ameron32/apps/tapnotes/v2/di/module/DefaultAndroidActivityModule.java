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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;

import com.ameron32.apps.tapnotes.v2.di.ForActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Default activity module which provides standard android classes and services.
 * <br/>
 * Use it like this:
 * <p><blockquote><pre>
 * {@literal @}Module(includes = { DefaultAndroidActivityModule.class })
 * </pre></blockquote></p>
 */
@Module(library = true)
public class DefaultAndroidActivityModule {

  private final Activity mActivity;

  public DefaultAndroidActivityModule(final Activity activity) {
    mActivity = activity;
  }

  // ==========================================================================================================================
  // Global Android Resources
  // ==========================================================================================================================

  @Provides
  @Singleton
  Activity provideActivity() {
    return mActivity;
  }

  @Provides
  @Singleton
  @ForActivity
  Context provideActivityContext() {
    return mActivity;
  }

  @Provides
  @Singleton
  @ForActivity
  Resources provideResources() {
    return mActivity.getResources();
  }

  // ==========================================================================================================================
  // Persistence
  // ==========================================================================================================================

  @Provides
  @Singleton
  @ForActivity
  SharedPreferences provideSharedPreferences(final Activity activity) {
    return activity.getPreferences(Context.MODE_PRIVATE);
  }

  @Provides
  @Singleton
  @ForActivity
  LayoutInflater provideLayoutInflater(final Activity activity) {
    return LayoutInflater.from(activity);
  }

}
