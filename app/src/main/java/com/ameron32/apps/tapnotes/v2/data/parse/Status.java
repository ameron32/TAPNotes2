package com.ameron32.apps.tapnotes.v2.data.parse;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.parse.ParseAnonymousUtils;

/**
 * Created by klemeilleur on 7/20/2015.
 */
public class Status {

  public static boolean isConnectionToServerAvailable(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo ni = cm.getActiveNetworkInfo();
    if ((ni != null) && (ni.isConnected())) {
      return true;
    }
    return false;
  }

  public static boolean isLoggedIn() {
    return !ParseAnonymousUtils.isLinked(Commands.Local.getClientUser());
  }
}
