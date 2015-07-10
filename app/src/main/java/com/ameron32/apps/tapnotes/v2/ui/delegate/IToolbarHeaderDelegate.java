package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.support.v7.widget.Toolbar;

/**
 * Created by klemeilleur on 7/8/2015.
 */
public interface IToolbarHeaderDelegate {

  void setTalkTitle(String title);
  void setSymposiumTitle(String title);
  void setSpeakerName(String speakerName);

  void setImage(String imageUrl);
  void onToolbarViewCreated(Toolbar toolbar);



  public interface IToolbarHeaderCallbacks {

    void onPreviousPressed();
    void onNextPressed();
  }
}
