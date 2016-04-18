package com.ameron32.apps.tapnotes.v2.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.ameron32.apps.tapnotes.v2.di.ForApplication;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by klemeilleur on 3/24/2016.
 */
@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "tapnotes_pref_file";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ForApplication Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }
}
