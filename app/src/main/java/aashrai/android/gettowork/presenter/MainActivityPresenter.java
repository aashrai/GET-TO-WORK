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
    boolean appLockActivate = sharedPreferences.getBoolean(Constants.APP_LOCK_ACTIVATED, false);
    sharedPreferences.edit().putBoolean(Constants.APP_LOCK_ACTIVATED, !appLockActivate).apply();

    if (!appLockActivate) {
      checkAndActivateAppLock();
    } else {
      mainActivityView.changeActivateDrawable(
          ContextCompat.getDrawable(context, R.drawable.ic_play_circle));
      mainActivityView.showToast(Constants.OVERLAY_DEACTIVATED_MESSAGE);
      launchHomeScreen();
    }
  }

  private void checkAndActivateAppLock() {
    mainActivityView.changeActivateDrawable(
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
