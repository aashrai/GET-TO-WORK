package aashrai.android.gettowork;

import aashrai.android.gettowork.view.activity.SettingsActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class Utils {

  public static Intent getHomeScreenIntent() {
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_HOME);
    return intent;
  }

  public static Intent getSettingsActivityIntent(Context context) {
    return new Intent(context, SettingsActivity.class);
  }

  public static boolean isAppLockDeactivationExpired(SharedPreferences sharedPreferences) {
    long millis = sharedPreferences.getLong(Constants.OVERLAY_DEACTIVATED_MILLIS, 0L);
    long timestamp = sharedPreferences.getLong(Constants.OVERLAY_DEACTIVATED_TIMESTAMP, 0L);
    return timestamp + millis < System.currentTimeMillis();
  }
}
