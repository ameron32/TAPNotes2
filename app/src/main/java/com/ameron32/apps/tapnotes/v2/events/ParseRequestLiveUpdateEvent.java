package com.ameron32.apps.tapnotes.v2.events;

/**
 * Created by kris on 7/18/2015.
 */
public class ParseRequestLiveUpdateEvent {

    public static final int REQUEST_NOTES_REFRESH = 1;
    public static final int REQUEST_TALKS_REFRESH = 2;
    public static final int REQUEST_PROGRAM_REFRESH = 3;

    private int requestType;

    public ParseRequestLiveUpdateEvent(int requestType) {
        this.requestType = requestType;
    }

    public int getRequestType() {
        return requestType;
    }
}
