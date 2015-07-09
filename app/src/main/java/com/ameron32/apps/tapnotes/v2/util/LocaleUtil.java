package com.ameron32.apps.tapnotes.v2.util;

import java.util.Locale;

/**
 * Created by klemeilleur on 7/9/2015.
 */
public class LocaleUtil {

  public static Locale getUILocale() {
    return Locale.getDefault();
  }

  /**
   * The default locale is not appropriate for machine-readable output.
   * The best choice there is usually Locale.US
   *   – this locale is guaranteed to be available on all devices,
   * and the fact that it has no surprising special cases and
   * is frequently used (especially for computer-computer communication)
   * means that it tends to be the most efficient choice too.
   *
   * @return
   */
  public static Locale getMachineLocale() {
    // TODO verify Locale usage for machine
    return Locale.US;
  }
}
