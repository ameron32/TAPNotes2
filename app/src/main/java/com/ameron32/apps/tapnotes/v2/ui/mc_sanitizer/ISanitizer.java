package com.ameron32.apps.tapnotes.v2.ui.mc_sanitizer;

import android.support.annotation.Nullable;

import com.ameron32.apps.tapnotes.v2.model.IBible;
import com.ameron32.apps.tapnotes.v2.scripture.Bible;
import com.ameron32.apps.tapnotes.v2.scripture.Scripture;

import java.util.ArrayList;

/**
 * Created by Micah on 7/16/2015.
 */
public interface ISanitizer {

    public void testForScriptures(IBible b, String s);
    public void setCallbacks(ISanitizerCallbacks callback);

    public interface ISanitizerCallbacks{
        public void onSanitizerResults(WrappedScripture scripture);
    }

}
