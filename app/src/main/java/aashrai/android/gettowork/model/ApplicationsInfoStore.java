package aashrai.android.gettowork.model;

import aashrai.android.gettowork.di.SettingsScope;
import android.content.pm.ApplicationInfo;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

@SettingsScope public class ApplicationsInfoStore {

  private final ApplicationInfoFetcher applicationInfoFetcher;
  final List<ApplicationInfo> applicationInfoList;

  @Inject public ApplicationsInfoStore(ApplicationInfoFetcher applicationInfoFetcher) {
    this.applicationInfoFetcher = applicationInfoFetcher;
    applicationInfoList = new ArrayList<>();
  }

  public Observable<List<ApplicationInfo>> getInstalledApplications(final String query) {

    if (applicationInfoList.isEmpty()) {
      if (!query.isEmpty()) {
        return Observable.error(
            new IllegalArgumentException("First search " + "query should be empty"));
      }
      return applicationInfoFetcher.getAllApplications()
          .doOnNext(new ApplicationInfoListSetter(new WeakReference<>(applicationInfoList)));
    }

    return applicationInfoFetcher.getFilteredApplications(applicationInfoList, query)
        .observeOn(AndroidSchedulers.mainThread());
  }

  private static class ApplicationInfoListSetter implements Action1<List<ApplicationInfo>> {

    private final List<ApplicationInfo> applicationInfoList;

    private ApplicationInfoListSetter(WeakReference<List<ApplicationInfo>> listWeakReference) {
      applicationInfoList = listWeakReference.get();
    }

    @Override public void call(List<ApplicationInfo> applicationInfoList) {
      this.applicationInfoList.addAll(applicationInfoList);
    }
  }
}
