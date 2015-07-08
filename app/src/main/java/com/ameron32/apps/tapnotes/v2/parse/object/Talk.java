package com.ameron32.apps.tapnotes.v2.parse.object;

import com.ameron32.apps.tapnotes.v2.model.EventType;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.model.IScripture;
import com.ameron32.apps.tapnotes.v2.model.ITalk;
import com.ameron32.apps.tapnotes.v2.parse.frmk.ColumnableParseObject;
import com.ameron32.apps.tapnotes.v2.scripture.Scripture;
import com.parse.ParseClassName;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Locale;

import static com.ameron32.apps.tapnotes.v2.parse.Constants.*;

/**
 * Created by klemeilleur on 6/29/2015.
 */
@ParseClassName(TALK_OBJECT_NAME)
public class Talk
    extends ColumnableParseObject
    implements ITalk<Scripture> {

  public static final int NO_SONG_NUMBER = -1;

  public static Talk create() {
    final Talk t = new Talk();
    return t;
  }

  public Talk() {
    // required empty
  }

  @Override
  public INote[] getNotes() {
    // FIXME
    return null;
  }

  @Override
  public DateTime getDateAndTime(Locale locale) {
    final String dateTime = this.getString(TALK_DATE_STRING_KEY);
    final DateTimeFormatter formatter = DateTimeFormat.forPattern("d/MM/yyyy H:mm")
        .withLocale(locale);
    return DateTime.parse(dateTime, formatter);
  }

  @Override
  public EventType getEventType() {
    return EventType.valueOfAnyCase(this.getString(TALK_TYPE_STRING_KEY));
  }

  @Override
  public String getTalkTitle() {
    return this.getString(TALK_TITLE_STRING_KEY);
  }

  @Override
  public List<Scripture> getTalkScriptures() {
    return Scripture.generateAll(getTalkThemeScriptures());
  }

  @Override
  public String getSymposiumTitle() {
    if (getEventType() == EventType.SYMPOSIUMTALK) {
      return getMetadata();
    }
    return "";
  }

  @Override
  public int getSongNumber() {
    if (getEventType() == EventType.SONG) {
      return getSongNumberWithinMetadata();
    }
    return NO_SONG_NUMBER;
  }

  private String getTalkThemeScriptures() {
    return this.getString(TALK_SCRIPTURES_STRING_KEY);
  }

  private int getSongNumberWithinMetadata() {
    return Integer.valueOf(getMetadata().substring(1));
  }

  private String getMetadata() {
    return this.getString(TALK_METADATA_STRING_KEY);
  }
}
