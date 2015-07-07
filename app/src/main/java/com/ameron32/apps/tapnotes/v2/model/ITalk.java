package com.ameron32.apps.tapnotes.v2.model;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.Locale;

/**
 * Created by klemeilleur on 6/29/2015.
 */
public interface ITalk {

  INote[] getNotes();
  DateTime getDateAndTime(Locale locale);
  EventType getEventType();
  String getTalkTitle();
  String getSymposiumTitle();
  int getSongNumber();
}
