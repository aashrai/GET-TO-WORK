package aashrai.android.gettowork.utils;

import aashrai.android.gettowork.di.MainActivityScope;
import android.content.Context;
import android.provider.Settings;
import javax.inject.Inject;

@MainActivityScope public class AccessibilityChecker {

  @Inject public AccessibilityChecker() {
  }

  public boolean isAccessibilityEnabled(Context context) {
    try {
      return Settings.Secure.getInt(context.getContentResolver(),
          Settings.Secure.ACCESSIBILITY_ENABLED) == 1;
    } catch (Settings.SettingNotFoundException e) {
      e.printStackTrace();
    }
    return false;
  }
}
