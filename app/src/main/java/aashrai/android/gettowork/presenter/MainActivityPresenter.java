package aashrai.android.gettowork.presenter;

import aashrai.android.gettowork.R;
import aashrai.android.gettowork.di.MainActivityScope;
import aashrai.android.gettowork.utils.AccessibilityChecker;
import aashrai.android.gettowork.utils.Constants;
import aashrai.android.gettowork.utils.Utils;
import aashrai.android.gettowork.view.MainActivityView;
import aashrai.android.gettowork.view.activity.CreditActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

@MainActivityScope public class MainActivityPresenter implements DialogInterface.OnClickListener {

  private final Set<String> activatedPackages;
  private final SharedPreferences sharedPreferences;
  private final Context context;
  private final AccessibilityChecker accessibilityChecker;
  private MainActivityView mainActivityView;

  @Inject
  public MainActivityPresenter(Set<String> activatedPackages, SharedPreferences sharedPreferences,
      Context context, AccessibilityChecker accessibilityChecker) {
    this.activatedPackages = activatedPackages;
    this.context = context;
    this.sharedPreferences = sharedPreferences;
    this.accessibilityChecker = accessibilityChecker;
  }

  public void setView(MainActivityView mainActivityView) {
    this.mainActivityView = mainActivityView;
  }

  public void checkAccessibilityEnabled() {
    if (!accessibilityChecker.isAccessibilityEnabled(context)) {
      mainActivityView.showAccessibilityDialog();
    }
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
        Utils.createVectorDrawable(mainActivityView.getActivityContext(),
            R.drawable.ic_play_circle));
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
        Utils.createVectorDrawable(mainActivityView.getActivityContext(),
            R.drawable.ic_pause_circle));

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

  public void onCreditTextClick() {
    Intent intent = new Intent(context, CreditActivity.class);
    mainActivityView.launchActivity(intent);
  }

  public void onGithubClick() {
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    intent.setData(Uri.parse("https://github.com/aashrairavooru/GET-TO-WORK"));
    mainActivityView.launchActivity(intent);
  }

  @Override public void onClick(DialogInterface dialog, int which) {
    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
    mainActivityView.launchActivity(intent);
    mainActivityView.showToast(String.format("Enable for %s",
        context.getResources().getString(R.string.accessibility_name)));
  }
}
