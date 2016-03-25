package com.ameron32.apps.tapnotes.v2.data.parse.frmk;

import android.support.annotation.Nullable;

import com.parse.ParseObject;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Kris on 3/14/2015.
 *
 * Element which combined ParseObject & Columnable
 * TODO: REMINDER needs ParseApplication subclass notification
 */
public class ColumnableParseObject
    extends ParseObject
    implements ParseColumnable<Object>
{


  /**
   *
   */
  public ColumnableParseObject() {
    // required public empty construction
  }

  // required for ParseColumnable

  public Set<String> getKeySet() {
    return this.keySet();
  }

  @Override
  public int getColumnCount() {
    return getKeySet().size();
  }

  @Override
  public Object get(String key) {
    return super.get(key);
  }

 // IS getKnown() useful?
//  public Object getKnown(String key) {
//    final Object value = super.get(key);
//    if (getType(key) != null) {
//      return value;
//    }
//    return null;
//  }

  @Override
  public ParseType getType(String key) {
    Object value = get(key);
    if (value != null) {
      return ParseType.whichType(value);
    }
    return null;
  }

  @Override
  public String getColumnHeader(String key) {
    return key;
  }

  @Override
  public String getCustomColumnHeader(String key) {
    return getColumnHeader(key);
  }



  @Nullable
  public String getKey(int columnPosition) {
    int column = 0;
    final Iterator<String> iterator = getKeySet().iterator();
    while (iterator.hasNext()) {
      if (column == columnPosition) {
        return iterator.next();
      }
      iterator.next();
      column++;
    }
    return null;
  }

  @Override
  public Object get(int columnPosition) {
    final String key = getKey(columnPosition);
    return get(key);
  }

  @Override
  public ParseType getType(int columnPosition) {
    final String key = getKey(columnPosition);
    return getType(key);
  }

  @Override
  public String getColumnHeader(int columnPosition) {
    final String key = getKey(columnPosition);
    return getColumnHeader(key);
  }

  @Override
  public String getCustomColumnHeader(int columnPosition) {
    final String key = getKey(columnPosition);
    return getCustomColumnHeader(key);
  }
}
