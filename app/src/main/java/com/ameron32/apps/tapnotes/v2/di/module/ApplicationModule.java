package com.ameron32.apps.tapnotes.v2.di.module;

/*
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Application;
import android.content.Context;

import com.ameron32.apps.tapnotes.v2.TAPApplication;

import dagger.Module;

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 */
@Module(
    injects = {
      TAPApplication.class
    },
    addsTo = DefaultAndroidApplicationModule.class,
    library = true)
public class ApplicationModule {
  private final Application mApplication;

  public ApplicationModule(Application application) {
    mApplication = application;
  }

//  /**
//   * Allow the mApplication context to be injected but require that it be annotated with
//   * {@link com.ameron32.apps.tapnotes.impl.di.me.ForApplication @Annotation} to explicitly differentiate it from an activity context.
//   */
//  @Provides
//  @Singleton
//  @ForApplication
//  Context provideApplicationContext() {
//    return mApplication;
//  }
//
//  @Provides
//  @Singleton
//  LocationManager provideLocationManager() {
//    return (LocationManager) mApplication.getSystemService(LOCATION_SERVICE);
//  }
}
