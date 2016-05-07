package aashrai.android.gettowork;

import aashrai.android.gettowork.utils.Constants;
import aashrai.android.gettowork.utils.Utils;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationManagerCompat;
import java.util.HashSet;
import java.util.Set;
import timber.log.Timber;

public class NotificationListener extends NotificationListenerService {

  NotificationManagerCompat notificationManager;
  SharedPreferences sharedPreferences;

  @Override public void onCreate() {
    super.onCreate();
    notificationManager = NotificationManagerCompat.from(this);
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
  }

  @Override public void onNotificationPosted(StatusBarNotification sbn) {
    super.onNotificationPosted(sbn);
    checkAndCancelNotification(sbn);
  }

  private void checkAndCancelNotification(StatusBarNotification statusBarNotification) {
    if (!Utils.isAppLockActivated(sharedPreferences)) return;
    Set<String> activatedPackages =
        sharedPreferences.getStringSet(Constants.ACTIVATED_PACKAGES, new HashSet<String>());
    if (activatedPackages.contains(statusBarNotification.getPackageName())) {
      removeNotification(statusBarNotification);
    }
  }

  private void removeNotification(StatusBarNotification sbn) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      cancelNotification(sbn.getKey());
    } else {
      cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
    }
  }

  @Override public void onNotificationRemoved(StatusBarNotification sbn) {
    super.onNotificationRemoved(sbn);
    Timber.d("Notification Removed %s", sbn.getPackageName());
  }
}
