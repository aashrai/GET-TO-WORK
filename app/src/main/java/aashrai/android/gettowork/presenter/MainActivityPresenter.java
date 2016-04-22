package aashrai.android.gettowork.presenter;

import aashrai.android.gettowork.Constants;
import aashrai.android.gettowork.R;
import aashrai.android.gettowork.Utils;
import aashrai.android.gettowork.di.MainActivityScope;
import aashrai.android.gettowork.view.MainActivityView;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

@MainActivityScope public class MainActivityPresenter {

  private final Set<String> activatedPackages;
  private final SharedPreferences sharedPreferences;
  private final Context context;
  private MainActivityView mainActivityView;

  @Inject
  public MainActivityPresenter(Set<String> activatedPackages, SharedPreferences sharedPreferences,
      Context context) {
    this.activatedPackages = activatedPackages;
    this.context = context;
    this.sharedPreferences = sharedPreferences;
  }

  public void setView(MainActivityView mainActivityView) {
    this.mainActivityView = mainActivityView;
  }

  public void onAppLockActivateClick() {
    boolean appLockActivate = Utils.isAppLockActivated(sharedPreferences);

    if (!appLockActivate) {
      sharedPreferences.edit().putBoolean(Constants.APP_LOCK_ACTIVATED, true).apply();
      checkAndActivateAppLock();
    } else {
      showWarning();
    }
  }

  private void showWarning() {
    mainActivityView.hideActivateButton();
    mainActivityView.showWarningText();
    mainActivityView.hideActivateHeader();
    mainActivityView.setWarningText(Constants.PAUSE_WARNING_MESSAGE_FIRST);
  }

  public void onTimingClick(String timing) {
    sharedPreferences.edit().putBoolean(Constants.APP_LOCK_ACTIVATED, false).apply();
    storeTiming(timing);
    mainActivityView.showToast(Constants.OVERLAY_DEACTIVATED_MESSAGE);
    restoreToDefault();
    launchHomeScreen();
  }

  private void restoreToDefault() {
    mainActivityView.hideTimingGrid();
    mainActivityView.showActivateButton();
    mainActivityView.showActivateHeader();
    mainActivityView.setActivateDrawable(
        ContextCompat.getDrawable(context, R.drawable.ic_play_circle));
  }

  void storeTiming(String timing) {
    long millis = getMillis(timing);
    sharedPreferences.edit()
        .putLong(Constants.OVERLAY_DEACTIVATED_TIMESTAMP, System.currentTimeMillis())
        .apply();
    sharedPreferences.edit().putLong(Constants.OVERLAY_DEACTIVATED_MILLIS, millis).apply();
  }

  long getMillis(String timing) {
    String[] timeAndUnit = timing.split(" ");
    String time = timeAndUnit[0];
    String unit = timeAndUnit[1];
    long millis = 0;
    if (unit.toLowerCase().equals("min") || unit.toLowerCase().equals("mins")) {
      millis = TimeUnit.MINUTES.toMillis(Long.valueOf(time));
    } else if (unit.toLowerCase().equals("hr") || unit.toLowerCase().equals("hrs")) {
      millis = TimeUnit.HOURS.toMillis(Long.valueOf(time));
    }
    return millis;
  }

  public void onWarningTextClick() {
    String text = mainActivityView.getWarningText();
    if (text.equals(Constants.PAUSE_WARNING_MESSAGE_FIRST)) {
      mainActivityView.setWarningText(Constants.PAUSE_WARNING_MESSAGE_SECOND);
    } else {
      mainActivityView.hideWarningText();
      mainActivityView.showTimingGrid();
    }
  }

  private void checkAndActivateAppLock() {
    mainActivityView.setActivateDrawable(
        ContextCompat.getDrawable(context, R.drawable.ic_pause_circle));

    if (activatedPackages.size() == 0) {
      mainActivityView.showToast(Constants.ADD_APPS_MESSAGE);
      launchSettingsActivity();
    } else {
      mainActivityView.showToast(Constants.OVERLAY_ACTIVATED_MESSAGE);
      launchHomeScreen();
    }
  }

  private void launchHomeScreen() {
    mainActivityView.launchActivity(Utils.getHomeScreenIntent());
  }

  private void launchSettingsActivity() {
    mainActivityView.launchActivity(Utils.getSettingsActivityIntent(context));
  }
}
