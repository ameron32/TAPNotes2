package com.ameron32.apps.tapnotes.v2.parse.object;

import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.model.ITalk;
import com.parse.ParseClassName;

import static com.ameron32.apps.tapnotes.v2.parse.object.ParseConstants.*;

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

}
