package com.ameron32.apps.tapnotes.v2.di.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.ameron32.apps.tapnotes.v2.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.joda.time.DateTime;

/**
 * Created by klemeilleur on 6/22/2015.
 */
public class ActivityCalendarViewDialogController extends AbsActivityController {
  private DialogInterface.OnClickListener dialogListener;

  public ActivityCalendarViewDialogController(Activity activity) {
    super(activity);
    dialogListener = (dialog, which) -> {
      switch (which) {
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

  public void showCalendarDialog(final String title, final String message) {
    final Resources r = getActivity().getResources();
    final AlertDialog calendarDialog = new AlertDialog.Builder(getContext())
        .setView(getCalendarView())
        .setPositiveButton(r.getString(R.string.dialog_select_text), dialogListener)
        .create();
    calendarDialog.show();
  }

  private View getCalendarView() {
    View view = LayoutInflater.from(getContext()).inflate(R.layout.frame_dialog_calendar, null);
    MaterialCalendarView calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
    calendarView.setSelectedDate(DateTime.now().toDate());
    return view;
  }
}
