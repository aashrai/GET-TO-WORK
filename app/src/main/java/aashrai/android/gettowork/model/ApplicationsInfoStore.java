package aashrai.android.gettowork.model;

import aashrai.android.gettowork.di.SettingsScope;
import aashrai.android.gettowork.utils.Utils;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@SettingsScope public class ApplicationsInfoStore {

  private final Context context;
  private final List<ApplicationInfo> applicationInfoList;

  @Inject public ApplicationsInfoStore(Context context) {
    this.context = context;
    applicationInfoList = new ArrayList<>();
  }

  public Observable<List<ApplicationInfo>> getInstalledApplications(final String query) {

    if (applicationInfoList.isEmpty()) {
      return Utils.deferedApplicationInfoFetcher(context)
          .doOnNext(new ApplicationInfoListSetter(new WeakReference<>(applicationInfoList)));
    }

    return Utils.getDeferedObservable(Observable.from(applicationInfoList)
        .filter(new PackageQueryFilter(context, query))
        .toList()).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
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

  private static class PackageQueryFilter implements Func1<ApplicationInfo, Boolean> {
    private final Context context;
    private final String query;

    private PackageQueryFilter(Context context, String query) {
      this.query = query.toLowerCase().trim();
      this.context = context;
    }

    @Override public Boolean call(ApplicationInfo applicationInfo) {
      return query.isEmpty() || Utils.getApplicationName(applicationInfo, context).contains(query);
    }
  }
}
