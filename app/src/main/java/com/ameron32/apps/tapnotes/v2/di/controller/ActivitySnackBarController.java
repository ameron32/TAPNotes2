package com.ameron32.apps.tapnotes.v2.di.controller;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.ameron32.apps.tapnotes.v2.R;


/**
 * Created by klemeilleur on 3/2/2015.
 */
public class ActivitySnackBarController extends AbsActivityController {

  public ActivitySnackBarController(final Activity activity) {
    super(activity);
  }

  public void toast(String message) {

    // from Design Support Library
    final int snackBarView = R.id.snackbar_location;
    final CoordinatorLayout preferredView = (CoordinatorLayout) getActivity().findViewById(snackBarView);
    final View fallbackView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
    Snackbar.make((preferredView == null) ? fallbackView : preferredView,
        message, Snackbar.LENGTH_LONG).show();

    final String pV = (preferredView == null) ? "null" : Integer.toString(preferredView.getId());
    final String fbV = (fallbackView == null) ? "null" : Integer.toString(fallbackView.getId());

    Log.d(ActivitySnackBarController.class.getSimpleName(),
        "toast (" + getActivity().getClass().getSimpleName() + "): " + message + " with (" +
            pV + "|" +
            fbV + ")");

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
