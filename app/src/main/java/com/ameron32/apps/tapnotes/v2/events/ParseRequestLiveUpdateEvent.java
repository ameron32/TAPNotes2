package com.ameron32.apps.tapnotes.v2.events;

/**
 * Created by kris on 7/18/2015.
 */
public class ParseRequestLiveUpdateEvent {

    private int requestType;

    public ParseRequestLiveUpdateEvent(int requestType) {
        this.requestType = requestType;
    }

    public int getRequestType() {
        return requestType;
    }
}
