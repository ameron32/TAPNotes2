package com.ameron32.apps.tapnotes.v2.parse.object;

import com.ameron32.apps.tapnotes.v2.parse.frmk.ColumnableParseObject;
import com.parse.ParseClassName;

/**
 * Created by Kris on 3/17/2015.
 */
@ParseClassName("TestObject")
public class _TestObject extends ColumnableParseObject {

  public static _TestObject create(String key, String key2, int keyN) {
    final _TestObject o = new _TestObject();
    o.put("key", key);
    o.put("key2", key2);
    o.put("keyN", keyN);
    return o;
  }

}
