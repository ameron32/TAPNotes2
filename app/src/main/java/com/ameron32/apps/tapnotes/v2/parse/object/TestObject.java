package com.ameron32.apps.tapnotes.v2.parse.object;

import com.parse.ParseClassName;

/**
 * Created by Kris on 3/17/2015.
 */
@ParseClassName("TestObject")
public class TestObject extends ColumnableParseObject {

  public static TestObject create(String key, String key2, int keyN) {
    final TestObject o = new TestObject();
    o.put("key", key);
    o.put("key2", key2);
    o.put("keyN", keyN);
    return o;
  }

}
