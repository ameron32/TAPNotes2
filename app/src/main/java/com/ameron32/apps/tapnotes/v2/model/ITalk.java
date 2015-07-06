package com.ameron32.apps.tapnotes.v2.model;

import java.util.Date;

/**
 * Created by klemeilleur on 6/29/2015.
 */
public interface ITalk {

  INote[] getNotes();
  Date getDateAndTime();
  EventType getEventType();
  String getTalkTitle();
  String getSymposiumTitle();
}
