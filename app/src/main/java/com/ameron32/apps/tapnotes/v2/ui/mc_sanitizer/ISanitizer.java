package com.ameron32.apps.tapnotes.v2.ui.mc_sanitizer;

import android.support.annotation.Nullable;

import com.ameron32.apps.tapnotes.v2.scripture.Scripture;

import java.util.ArrayList;

/**
 * Created by Micah on 7/16/2015.
 */
public interface ISanitizer {

    public void testForScriptures(String s);

    public interface ISanitizerCallbacks{
        public void onSanitizerResults(WrappedScripture scripture);
    }

}