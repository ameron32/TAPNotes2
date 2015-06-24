package com.ameron32.apps.tapnotes.v2.di.controller;

import android.app.Activity;
import android.support.design.widget.Snackbar;


/**
 * Created by klemeilleur on 3/2/2015.
 */
public class ActivitySnackBarController extends AbsController {

  public ActivitySnackBarController(final Activity activity) {
    super(activity);
  }

  public void toast(String message) {

    // from Design Support Library
    Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
        message, Snackbar.LENGTH_LONG).show();

    // from package com.kenny.snackbar.SnackBar
//    SnackBar.show(getActivity(), message);

    // from package com.gc.materialdesign.widgets.SnackBar
//    new SnackBar(getActivity(), message).show();

    // from package com.nispok.snackbar.Snackbar
//    SnackbarManager.show(
//        Snackbar.with(getActivity().getApplicationContext())
//            .text(message), getActivity());

    //
//    com.rey.material.widget.
//    SnackBar.make(getActivity())
//        .applyStyle(R.style.Material_Widget_SnackBar)
//        .text(message)
//        .show();

    // fallback
//    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
  }
}
