package com.ameron32.apps.tapnotes.v2.model;

import com.ameron32.apps.tapnotes.v2.scripture.Scripture;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by klemeilleur on 6/29/2015.
 */
public interface ITalk<T extends IScripture, U extends IBible> extends IObject {

  INote[] getNotes();
  DateTime getDateAndTime(Locale locale);
  EventType getEventType();
  String getTalkTitle();
  List<T> getTalkScriptures(U bible);
  String getSymposiumTitle();
  int getSongNumber();
  int getDurationInMinutes();
  String getSequence();
}
