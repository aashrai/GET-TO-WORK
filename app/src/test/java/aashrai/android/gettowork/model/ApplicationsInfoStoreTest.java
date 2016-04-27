package aashrai.android.gettowork.model;

import aashrai.android.gettowork.BaseTest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import rx.Observable;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ApplicationsInfoStoreTest extends BaseTest {

  ApplicationsInfoStore applicationsInfoStore;
  Context context;
  List<ApplicationInfo> demoApplicationInfo;
  @Mock ApplicationInfoFetcher applicationInfoFetcher;
  @Mock ApplicationInfo applicationInfo;

  @Before public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    context = RuntimeEnvironment.application;
    applicationsInfoStore = new ApplicationsInfoStore(applicationInfoFetcher);
    demoApplicationInfo = Arrays.asList(applicationInfo, applicationInfo, applicationInfo);
  }

  @Test public void testGetInstalledApplications() throws Exception {
    assertTrue(applicationsInfoStore.applicationInfoList.isEmpty());
    String searchQuery = "";

    //Test for exception on first search which should be empty
    TestSubscriber<List<ApplicationInfo>> testSubscriber = new TestSubscriber<>();
    applicationsInfoStore.getInstalledApplications("test").subscribe(testSubscriber);
    testSubscriber.assertError(IllegalArgumentException.class);

    //Test for first search with empty query
    given(applicationInfoFetcher.getAllApplications()).willReturn(
        Observable.just(demoApplicationInfo));
    applicationsInfoStore.getInstalledApplications(searchQuery).subscribe();
    verify(applicationInfoFetcher).getAllApplications();
    assertEquals(3, applicationsInfoStore.applicationInfoList.size());

    //Test for later searches
    given(applicationInfoFetcher.getFilteredApplications(demoApplicationInfo,
        searchQuery)).willReturn(Observable.<List<ApplicationInfo>>empty());
    applicationsInfoStore.getInstalledApplications(searchQuery);
    verify(applicationInfoFetcher).getFilteredApplications(demoApplicationInfo, searchQuery);
  }

  @After public void tearDown() throws Exception {
    demoApplicationInfo = null;
  }
}