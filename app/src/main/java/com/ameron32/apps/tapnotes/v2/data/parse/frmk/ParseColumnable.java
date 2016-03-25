package com.ameron32.apps.tapnotes.v2.data.parse.frmk;

/**
 * Created by klemeilleur on 6/22/2015.
 */
public interface ParseColumnable<T extends Object> extends Columnable<T> {

  public String getCustomColumnHeader(String key);

  public String getColumnHeader(String key);

  public String getKey(int columnPosition);

  public T get(String key);

  public ParseType getType(String key);

  public ParseType getType(int columnPosition);
}
