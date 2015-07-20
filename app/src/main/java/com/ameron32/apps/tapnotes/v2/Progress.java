package com.ameron32.apps.tapnotes.v2;

/**
 * Created by kris on 7/19/2015.
 */
public class Progress {
  /*
   * This class is used by Observables within the app to store the current progress of an RxJava observable process in action.
   */

  public int item;
  public int total;
  public boolean failed;

  public Progress(int item, int total, boolean failed) {
    this.item = item;
    this.total = total;
    this.failed = failed;
  }
}
