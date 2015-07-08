package com.ameron32.apps.tapnotes.v2.model;

import com.ameron32.apps.tapnotes.v2.scripture.Scripture;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by klemeilleur on 6/29/2015.
 */
public interface ITalk<T extends IScripture> {

  INote[] getNotes();
  DateTime getDateAndTime(Locale locale);
  EventType getEventType();
  String getTalkTitle();
  List<T> getTalkScriptures();
  String getSymposiumTitle();
  int getSongNumber();
}
