package com.ameron32.apps.tapnotes.v2.di.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;

import com.ameron32.apps.tapnotes.v2.R;


/**
 * Created by Kris on 4/25/2015.
 */
public class ActivityAlertDialogController extends AbsActivityController {
  private DialogInterface.OnClickListener dialogListener;

  public ActivityAlertDialogController(Activity activity) {
    super(activity);
    dialogListener = (dialog, which) -> {
      switch(which) {
        case DialogInterface.BUTTON_POSITIVE:

          break;
        case DialogInterface.BUTTON_NEGATIVE:

          break;
        case DialogInterface.BUTTON_NEUTRAL:
        default:

      }
      dialog.dismiss();
    };
  }

  public void showInformationDialog(final String title, final String message) {
    final Resources r = getActivity().getResources();
    final AlertDialog informationDialog = new AlertDialog.Builder(getActivity())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(r.getString(R.string.dialog_ok), dialogListener)
        .create();
    informationDialog.show();

    // from MaterialDesignLibrary
//    final Dialog dialog = new Dialog(getActivity(), title, message);
//    dialog.setButtonAccept((ButtonFlat) LayoutInflater.from(getActivity()).inflate(R.layout.template_button_flat, null));
//    dialog.show();

    //
//    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//    builder
//        .setTitle(title)
//        .setMessage(message)
//        .setCancelable(false)
//        .setNeutralButton("OK", (dialog, which) -> { dialog.dismiss(); })
//        .create()
//        .show();
  }

  public void showInterruptDialog(final String title, final String message, final Runnable runnable) {
    final Resources r = getActivity().getResources();
    final AlertDialog informationDialog = new AlertDialog.Builder(getActivity())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(r.getString(R.string.dialog_ok),
            (dialog, which) -> {
          switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
              break;
            case DialogInterface.BUTTON_NEGATIVE:
              break;
            case DialogInterface.BUTTON_NEUTRAL:
            default:
          }
          dialog.dismiss();
          runnable.run();
        })
        .create();
    informationDialog.show();
  }
}
