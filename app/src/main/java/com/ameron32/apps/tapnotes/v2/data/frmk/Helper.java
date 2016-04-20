package com.ameron32.apps.tapnotes.v2.data.frmk;

import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.model.IUser;

import org.joda.time.DateTime;

/**
 * Created by klemeilleur on 3/25/2016.
 */
public interface Helper {

    IProgram ALL_PROGRAMS = null;
    ITalk ALL_TALKS = null;
    DateTime FOREVER = null;
    IUser USER_ME = new IUser() {
        @Override
        public String getId() {
            return "me";
        }
    };
    IUser USER_GENERIC = new IUser() {
        @Override
        public String getId() {
            return "generic";
        }
    };
    IUser USER_ALL = null;
}
