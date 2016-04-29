package aashrai.android.gettowork.model;

import aashrai.android.gettowork.di.SettingsScope;
import aashrai.android.gettowork.utils.Utils;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@SettingsScope public class ApplicationInfoFetcher {

  private final Context context;

  @Inject public ApplicationInfoFetcher(Context context) {
    this.context = context;
  }

  public Observable<List<ApplicationInfo>> getAllApplications() {
    return Utils.getAllApplications(context);
  }

  public Observable<List<ApplicationInfo>> getFilteredApplications(
      List<ApplicationInfo> applicationInfoList, String query) {
    return Observable.from(applicationInfoList)
        .observeOn(Schedulers.computation())
        .filter(new PackageQueryFilter(context, query))
        .toList()
        .observeOn(AndroidSchedulers.mainThread());
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
