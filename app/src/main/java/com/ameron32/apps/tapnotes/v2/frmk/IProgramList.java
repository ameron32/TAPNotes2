package com.ameron32.apps.tapnotes.v2.frmk;

import com.ameron32.apps.tapnotes.v2.frmk.object.Progress;

/**
 * Created by klemeilleur on 7/20/2015.
 */
public interface IProgramList {

  void setProgramDownloaded(String programId);
  void setProgramProgress(String programId, Progress progress);
}
