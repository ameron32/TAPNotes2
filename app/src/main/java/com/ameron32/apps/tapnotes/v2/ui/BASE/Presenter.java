package com.ameron32.apps.tapnotes.v2.ui.BASE;

/**
 * Created by klemeilleur on 4/21/2016.
 */
public interface Presenter<V extends MvpView> {

  void attachView(V mvpView);

  void detachView();
}
