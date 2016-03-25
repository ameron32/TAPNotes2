package com.ameron32.apps.tapnotes.v2.data.model;

/**
 * Created by klemeilleur on 7/6/2015.
 */
public enum EventType {

  MUSIC, SONG, TALK, SYMPOSIUMTALK;

  public static EventType valueOfAnyCase(final String name) {
    return EventType.valueOf(name.toUpperCase());
  }


}
