package com.ameron32.apps.tapnotes.v2.parse.object;

import com.ameron32.apps.tapnotes.v2.model.ISession;
import com.parse.ParseClassName;

import static com.ameron32.apps.tapnotes.v2.parse.object.ParseConstants.*;

/**
 * Created by klemeilleur on 6/29/2015.
 */
@ParseClassName(SESSION_OBJECT_NAME)
public class Session
    extends ColumnableParseObject
    implements ISession {

  public static Session create() {
    final Session s = new Session();
    return s;
  }

  public Session() {
    // required empty
  }
}
