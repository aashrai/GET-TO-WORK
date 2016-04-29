package aashrai.android.gettowork.model;

import aashrai.android.gettowork.di.SettingsScope;
import android.content.pm.ApplicationInfo;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;

@SettingsScope public class ApplicationsInfoStore {

  private final ApplicationInfoFetcher applicationInfoFetcher;
  final List<ApplicationInfo> applicationInfoList;
  private Subject<List<ApplicationInfo>, List<ApplicationInfo>> applicationInfoSubject;
  private Subscription subjectSubscription;

  @Inject public ApplicationsInfoStore(ApplicationInfoFetcher applicationInfoFetcher) {
    this.applicationInfoFetcher = applicationInfoFetcher;
    applicationInfoList = new ArrayList<>();
  }

  public Observable<List<ApplicationInfo>> getInstalledApplications(final String query) {
    //We are using Subjects here to remove the dependence on AndroidSchedulers.mainThread() to
    // report on the main thread since it causes race condition, using PublishSubject also helps
    // with restoring state across configuration changes

    if (subjectSubscription == null || subjectSubscription.isUnsubscribed()) {
      applicationInfoSubject = BehaviorSubject.create();
    }

    subjectSubscription = applicationInfoFetcher.getFilteredApplications(applicationInfoList, query)
        .subscribe(applicationInfoSubject);
    return applicationInfoSubject;
  }

  public Observable<List<ApplicationInfo>> getAllInstalledApplications() {
    applicationInfoSubject = BehaviorSubject.create();
    subjectSubscription = applicationInfoFetcher.getAllApplications()
        .doOnNext(new ApplicationInfoListSetter())
        .subscribe(applicationInfoSubject);
    return applicationInfoSubject;
  }

  private class ApplicationInfoListSetter implements Action1<List<ApplicationInfo>> {

    @Override public void call(List<ApplicationInfo> applicationInfoList) {
      //This has to be done because we are subscribing to the first observable with all
      // applications two times
      if (ApplicationsInfoStore.this.applicationInfoList.isEmpty()) {
        ApplicationsInfoStore.this.applicationInfoList.addAll(applicationInfoList);
      }
    }
  }
}
