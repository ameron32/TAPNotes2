package com.ameron32.apps.tapnotes.v2.ui.delegate;

import com.ameron32.apps.tapnotes.v2.model.ITalk;

import java.util.List;

/**
 * Created by klemeilleur on 7/8/2015.
 */
public interface IProgramDelegate {

  void loadProgramTalks(List<ITalk> talks);



  public interface IProgramDelegateCallbacks {

    void onTalkClicked(String talkId);
  }
}
