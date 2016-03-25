package com.ameron32.apps.tapnotes.v2.ui.mc_sanitizer;

import com.ameron32.apps.tapnotes.v2.data.model.IBible;

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
