package com.ameron32.apps.tapnotes.v2.parse.object;

import com.ameron32.apps.tapnotes.v2.model.EventType;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.model.ITalk;
import com.ameron32.apps.tapnotes.v2.parse.frmk.ColumnableParseObject;
import com.parse.ParseClassName;

import java.util.Date;

import static com.ameron32.apps.tapnotes.v2.parse.ParseConstants.*;

/**
 * Created by klemeilleur on 6/29/2015.
 */
@ParseClassName(TALK_OBJECT_NAME)
public class Talk
    extends ColumnableParseObject
    implements ITalk {

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
  public Date getDateAndTime() {
    return null;
  }

  @Override
  public EventType getEventType() {
    return null;
  }

  @Override
  public String getTalkTitle() {
    return null;
  }

  @Override
  public String getSymposiumTitle() {
    return null;
  }
}
