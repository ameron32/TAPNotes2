package com.ameron32.apps.tapnotes.v2.di.controller;

import android.app.Application;
import android.content.Context;

/**
 * Created by klemeilleur on 7/1/2015.
 */
public abstract class AbsApplicationController {
  private final Application mApplication;

  public AbsApplicationController(final Application application) {
    this.mApplication = application;
  }

  public Application getApplication() {
    return mApplication;
  }

  public Context getContext() {
    return getApplication();
  }
}
