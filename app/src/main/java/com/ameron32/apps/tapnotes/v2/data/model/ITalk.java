package com.ameron32.apps.tapnotes.v2.data.model;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Locale;

/**
 * Created by klemeilleur on 6/29/2015.
 */
public interface ITalk<T extends IScripture, U extends IBible> extends IObject {

  DateTime getDateAndTime(Locale locale);
  EventType getEventType();
  String getTalkTitle();
  List<T> getTalkScriptures(U bible);
  String getSymposiumTitle();
  int getSongNumber();
  int getDurationInMinutes();
  String getSequence();
  String getHeaderImageUrl();
}
