package aashrai.android.gettowork.presenter;

import aashrai.android.gettowork.BuildConfig;
import aashrai.android.gettowork.Constants;
import aashrai.android.gettowork.R;
import aashrai.android.gettowork.utils.Utils;
import aashrai.android.gettowork.view.MainActivityView;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class MainActivityPresenterTest {

  SharedPreferences sharedPreferences;
  @Mock Set<String> activatedPackages;
  @Mock MainActivityView mainActivityView;
  Drawable pauseDrawable;
  Drawable playDrawable;

  MainActivityPresenter mainActivityPresenter;

  @Before public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application);
    mainActivityPresenter = new MainActivityPresenter(activatedPackages, sharedPreferences,
        RuntimeEnvironment.application);
    mainActivityPresenter.setView(mainActivityView);

    pauseDrawable =
        ContextCompat.getDrawable(RuntimeEnvironment.application, R.drawable.ic_pause_circle);
    playDrawable =
        ContextCompat.getDrawable(RuntimeEnvironment.application, R.drawable.ic_play_circle);
  }

  @SuppressLint("CommitPrefEdits") @Test public void testOnAppLockActivate_NO_INCLUDEDPACKAGES()
      throws Exception {
    sharedPreferences.edit().putBoolean(Constants.APP_LOCK_ACTIVATED, false).commit();
    given(activatedPackages.size()).willReturn(0);
    mainActivityPresenter.onAppLockActivateClick();
    verify(mainActivityView).showToast(Constants.ADD_APPS_MESSAGE);
    verify(mainActivityView).launchActivity(
        Utils.getSettingsActivityIntent(RuntimeEnvironment.application));
    verify(mainActivityView).setActivateDrawable(pauseDrawable);
    assertTrue(sharedPreferences.getBoolean(Constants.APP_LOCK_ACTIVATED, false));
  }

  @SuppressLint("CommitPrefEdits") @Test public void testOnAppLockActivate_INCLUDEDPACKAGES() {
    sharedPreferences.edit().putBoolean(Constants.APP_LOCK_ACTIVATED, false).commit();
    given(activatedPackages.size()).willReturn(5);
    mainActivityPresenter.onAppLockActivateClick();
    verify(mainActivityView).showToast(Constants.OVERLAY_ACTIVATED_MESSAGE);
    verify(mainActivityView).launchActivity(Utils.getHomeScreenIntent());
    verify(mainActivityView).setActivateDrawable(pauseDrawable);
    assertTrue(sharedPreferences.getBoolean(Constants.APP_LOCK_ACTIVATED, false));
  }

  @SuppressLint("CommitPrefEdits") @Test public void testOnAppLockActivate_APPLOCK_DEACTIVATED() {
    sharedPreferences.edit().putBoolean(Constants.APP_LOCK_ACTIVATED, true).commit();
    mainActivityPresenter.onAppLockActivateClick();
    verify(mainActivityView).hideActivateButton();
    verify(mainActivityView).hideActivateHeader();
    verify(mainActivityView).showWarningText();
    verify(mainActivityView).setWarningText(Constants.PAUSE_WARNING_MESSAGE_FIRST);
  }

  @Test public void testOnWarningTextClick() throws Exception {
    //Test first click
    given(mainActivityView.getWarningText()).willReturn(Constants.PAUSE_WARNING_MESSAGE_FIRST);
    mainActivityPresenter.onWarningTextClick();
    verify(mainActivityView).setWarningText(Constants.PAUSE_WARNING_MESSAGE_SECOND);

    //Test second click
    given(mainActivityView.getWarningText()).willReturn(Constants.PAUSE_WARNING_MESSAGE_SECOND);
    mainActivityPresenter.onWarningTextClick();
    verify(mainActivityView).hideWarningText();
    verify(mainActivityView).showTimingGrid();
  }

  @After public void tearDown() throws Exception {
    pauseDrawable = null;
    playDrawable = null;
  }

  @Test public void testOnTimingClick() throws Exception {
    mainActivityPresenter.onTimingClick("3 min");
    verify(mainActivityView).showToast(Constants.OVERLAY_DEACTIVATED_MESSAGE);
    verify(mainActivityView).launchActivity(Utils.getHomeScreenIntent());
    verify(mainActivityView).hideTimingGrid();
    verify(mainActivityView).showActivateButton();
    verify(mainActivityView).showActivateHeader();
  }

  @Test public void testStoreTiming() throws Exception {
    mainActivityPresenter.storeTiming("3 min");
    assertNotSame(sharedPreferences.getLong(Constants.OVERLAY_DEACTIVATED_TIMESTAMP, -1L), -1L);
    assertNotSame(sharedPreferences.getLong(Constants.OVERLAY_DEACTIVATED_MILLIS, -1L), -1L);
  }

  @Test public void testGetMillis() throws Exception {
    long millis;
    millis = mainActivityPresenter.getMillis("1 min");
    assertEquals(TimeUnit.MINUTES.toMillis(1), millis);

    millis = mainActivityPresenter.getMillis("5 mins");
    assertEquals(TimeUnit.MINUTES.toMillis(5), millis);

    millis = mainActivityPresenter.getMillis("1 hr");
    assertEquals(TimeUnit.HOURS.toMillis(1), millis);

    millis = mainActivityPresenter.getMillis("24 hrs");
    assertEquals(TimeUnit.HOURS.toMillis(24), millis);
  }
}