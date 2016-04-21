package com.ameron32.apps.tapnotes.v2.ui.BASE;

import com.ameron32.apps.tapnotes.v2.data.model.ITalk;

import java.util.List;

/**
 * Created by klemeilleur on 4/21/2016.
 */
public interface MainMvpView extends MvpView {

  void showTalks(List<ITalk> talks);

  void showTalksEmpty();

  void showTalksError();
}
