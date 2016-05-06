package aashrai.android.gettowork.presenter;

import aashrai.android.gettowork.BaseTest;
import aashrai.android.gettowork.model.ApplicationsInfoStore;
import aashrai.android.gettowork.utils.Constants;
import aashrai.android.gettowork.view.SettingsView;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.preference.PreferenceManager;
import java.util.HashSet;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import rx.Observable;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class SettingsActivityPresenterTest extends BaseTest {

  private SettingsActivityPresenter settingsActivityPresenter;
  private String mockString;
  @Mock ApplicationsInfoStore applicationsInfoStore;
  @Mock SettingsView settingsView;
  @Mock List<ApplicationInfo> applicationInfoList;
  SharedPreferences sharedPreferences;
  Context context;

  @Before public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    mockString = "lorem";
    context = RuntimeEnvironment.application;
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    settingsActivityPresenter =
        new SettingsActivityPresenter(context, sharedPreferences, applicationsInfoStore);
    settingsActivityPresenter.setView(settingsView);
  }

  @Test public void testExecute() throws Exception {
    given(applicationsInfoStore.getInstalledApplications("")).willReturn(
        Observable.<List<ApplicationInfo>>empty());
    settingsActivityPresenter.execute();
    verify(applicationsInfoStore).getInstalledApplications("");
  }

  @Test public void testOnSearch() throws Exception {
    given(applicationsInfoStore.getInstalledApplications(mockString)).willReturn(
        Observable.just(applicationInfoList));
    settingsActivityPresenter.onSearch(mockString);
    verify(settingsView).startProgressBar();
    verify(settingsView).stopProgressBar();
    verify(settingsView).updatePackageListAdapter(applicationInfoList);
    verify(applicationsInfoStore).getInstalledApplications(mockString);
  }

  @Test public void testOnPackageToggle() throws Exception {
    assertTrue(sharedPreferences.getStringSet(Constants.ACTIVATED_PACKAGES, new HashSet<String>())
        .isEmpty());
    settingsActivityPresenter.onPackageToggle(mockString, true);
    assertTrue(sharedPreferences.getStringSet(Constants.ACTIVATED_PACKAGES, new HashSet<String>())
        .contains(mockString));
    settingsActivityPresenter.onPackageToggle(mockString, false);
    assertFalse(sharedPreferences.getStringSet(Constants.ACTIVATED_PACKAGES, new HashSet<String>())
        .contains(mockString));
  }

  @After public void tearDown() throws Exception {
    settingsActivityPresenter = null;
    context = null;
    sharedPreferences = null;
  }
}