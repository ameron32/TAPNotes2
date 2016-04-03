package com.ameron32.apps.tapnotes.v2.frmk;

import android.app.Service;

import com.ameron32.apps.tapnotes.v2.di.Injector;

/**
 * Created by klemeilleur on 4/2/16.
 */
public abstract class TAPService extends Service {

    @Override
    public void onCreate() {
        Injector.INSTANCE.inject(this);
        super.onCreate();
    }
}
