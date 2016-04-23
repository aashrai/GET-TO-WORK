package aashrai.android.gettowork;

import aashrai.android.gettowork.utils.Constants;
import aashrai.android.gettowork.utils.Utils;
import aashrai.android.gettowork.view.activity.AppLockActivity;
import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.accessibility.AccessibilityEvent;
import java.util.HashSet;
import java.util.Set;

public class OverlayService extends AccessibilityService {

  SharedPreferences sharedPreferences;
  private static final String TAG = "OverlayService";

  @Override protected void onServiceConnected() {
    super.onServiceConnected();
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
  }

  @Override public void onAccessibilityEvent(AccessibilityEvent event) {
    Set<String> activatedPackages =
        sharedPreferences.getStringSet(Constants.ACTIVATED_PACKAGES, new HashSet<String>());
    if (Utils.isAppLockActivated(sharedPreferences) && activatedPackages.contains(
        event.getPackageName().toString())) {
      //Log.d(TAG, "onAccessibilityEvent() called with: " + "event = [" + event + "]");
      launchAppLock();
    }
  }

  private void launchAppLock() {
    Intent intent = new Intent(this, AppLockActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        | Intent.FLAG_ACTIVITY_NO_HISTORY
        | Intent.FLAG_ACTIVITY_NO_ANIMATION
        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
    startActivity(intent);
  }

  @Override public void onInterrupt() {

  }
}
