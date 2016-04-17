package com.ameron32.apps.tapnotes.v2.data.model;

import java.util.Date;

/**
 * Created by klemeilleur on 7/13/2015.
 */
public interface IObject {

  String getId();

  /**
   * All objects should be timestamped by the UI at the time of create, update, or delete.
   */
  void setUserTimestamp(Date date);
}
