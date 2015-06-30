package com.ameron32.apps.tapnotes.v2.parse.object;

import com.ameron32.apps.tapnotes.v2.model.IDay;
import com.ameron32.apps.tapnotes.v2.model.ISession;
import com.ameron32.apps.tapnotes.v2.parse.frmk.ColumnableParseObject;
import com.parse.ParseClassName;

import static com.ameron32.apps.tapnotes.v2.parse.ParseConstants.*;

/**
 * Created by klemeilleur on 6/29/2015.
 */
@ParseClassName(DAY_OBJECT_NAME)
public class Day
    extends ColumnableParseObject
    implements IDay {

  public static Day create() {
    final Day d = new Day();
    return d;
  }

  public Day() {
    // required empty
  }

  @Override
  public ISession[] getSessions() {
    // FIXME
    return null;
  }
}
