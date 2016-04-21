package aashrai.android.gettowork.presenter;

import aashrai.android.gettowork.BuildConfig;
import aashrai.android.gettowork.Constants;
import aashrai.android.gettowork.Utils;
import aashrai.android.gettowork.view.MainActivityView;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class MainActivityPresenterTest {

  SharedPreferences sharedPreferences;
  @Mock Set<String> activatedPackages;
  @Mock MainActivityView mainActivityView;

  MainActivityPresenter mainActivityPresenter;

  @Before public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application);
    mainActivityPresenter = new MainActivityPresenter(activatedPackages, sharedPreferences,
        RuntimeEnvironment.application);
    mainActivityPresenter.setView(mainActivityView);
  }

  @SuppressLint("CommitPrefEdits") @Test public void testOnAppLockActivate_NO_INCLUDEDPACKAGES()
      throws Exception {

    //Test for no included packages
    sharedPreferences.edit().putBoolean(Constants.APP_LOCK_ACTIVATED, false).commit();
    given(activatedPackages.size()).willReturn(0);
    mainActivityPresenter.onAppLockActivate();
    verify(mainActivityView).showToast(Constants.ADD_APPS_MESSAGE);
    verify(mainActivityView).launchActivity(
        Utils.getSettingsActivityIntent(RuntimeEnvironment.application));
    assertTrue(sharedPreferences.getBoolean(Constants.APP_LOCK_ACTIVATED, false));
  }

  @SuppressLint("CommitPrefEdits") @Test public void testOnAppLockActivate_INCLUDEDPACKAGES() {
    sharedPreferences.edit().putBoolean(Constants.APP_LOCK_ACTIVATED, false).commit();
    given(activatedPackages.size()).willReturn(5);
    mainActivityPresenter.onAppLockActivate();
    verify(mainActivityView).showToast(Constants.OVERLAY_ACTIVATED_MESSAGE);
    verify(mainActivityView).launchActivity(Utils.getHomeScreenIntent());
    assertTrue(sharedPreferences.getBoolean(Constants.APP_LOCK_ACTIVATED, false));
  }

  @SuppressLint("CommitPrefEdits") @Test public void testOnAppLockActivate_APPLOCK_DEACTIVATED() {
    sharedPreferences.edit().putBoolean(Constants.APP_LOCK_ACTIVATED, true).commit();
    mainActivityPresenter.onAppLockActivate();
    verify(mainActivityView).launchActivity(Utils.getHomeScreenIntent());
    verify(mainActivityView).showToast(Constants.OVERLAY_DEACTIVATED_MESSAGE);
    assertFalse(sharedPreferences.getBoolean(Constants.APP_LOCK_ACTIVATED, false));
  }
}