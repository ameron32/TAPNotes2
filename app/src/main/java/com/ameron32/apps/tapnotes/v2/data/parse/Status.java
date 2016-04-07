package com.ameron32.apps.tapnotes.v2.data.parse;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.parse.ParseAnonymousUtils;

/**
 * Created by klemeilleur on 7/20/2015.
 */
public class Status {

  public static boolean isLoggedIn() {
    return !ParseAnonymousUtils.isLinked(ParseHelper.Commands.Local.getClientUser());
  }
}
