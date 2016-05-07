package aashrai.android.gettowork.model;

import aashrai.android.gettowork.di.SettingsScope;
import android.content.pm.ApplicationInfo;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;

@SettingsScope public class ApplicationsInfoStore {

  private final ApplicationInfoFetcher applicationInfoFetcher;

  @Inject public ApplicationsInfoStore(ApplicationInfoFetcher applicationInfoFetcher) {
    this.applicationInfoFetcher = applicationInfoFetcher;
  }

  public Observable<List<ApplicationInfo>> getInstalledApplications(final String query) {
    //We are using Subjects here to remove the dependence on AndroidSchedulers.mainThread() to
    // report on the main thread since it causes race condition, using PublishSubject also helps
    // with restoring state across configuration changes

    Subject<List<ApplicationInfo>, List<ApplicationInfo>> applicationInfoSubject =
        BehaviorSubject.create();
    applicationInfoFetcher.getFilteredApplications(query).subscribe(applicationInfoSubject);
    return applicationInfoSubject;
  }
}
