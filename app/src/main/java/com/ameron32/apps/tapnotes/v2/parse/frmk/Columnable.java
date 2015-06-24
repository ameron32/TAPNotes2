package com.ameron32.apps.tapnotes.v2.parse.frmk;

public interface Columnable<T extends Object> {

  public T get(int columnPosition);

  public int getColumnCount();

  public String getColumnHeader(int columnPosition);

  public String getCustomColumnHeader(int columnPosition);
}
