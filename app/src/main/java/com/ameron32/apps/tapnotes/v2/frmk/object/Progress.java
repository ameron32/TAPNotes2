package com.ameron32.apps.tapnotes.v2.frmk.object;

/**
 * Created by kris on 7/19/2015.
 */
public class Progress {
  /*
   * This class is used by Observables within the app to store the current progress of an RxJava observable process in action.
   */

  public final int item;
  public final int total;
  public final boolean failed;
  public final String label;
  public final String note;

  public Progress(int item, int total, boolean failed) {
    this.item = item;
    this.total = total;
    this.failed = failed;
    this.label = "";
    this.note = "";
  }

  public Progress(int item, int total, boolean failed, String label, String note) {
    this.item = item;
    this.total = total;
    this.failed = failed;
    this.label = label;
    this.note = note;
  }
}
