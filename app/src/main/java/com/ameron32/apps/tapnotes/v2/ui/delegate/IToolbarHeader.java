package com.ameron32.apps.tapnotes.v2.ui.delegate;

/**
 * Created by klemeilleur on 7/8/2015.
 */
public interface IToolbarHeader {

  void setTalkTitle(String title);
  void setSymposiumTitle(String title);
  void setSpeakerName(String speakerName);



  public interface IToolbarHeaderCallbacks {

    void onPreviousPressed();
    void onNextPressed();
  }
}
