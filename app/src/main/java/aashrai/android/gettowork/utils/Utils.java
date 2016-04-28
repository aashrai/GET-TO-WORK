package aashrai.android.gettowork.utils;

import aashrai.android.gettowork.view.activity.SettingsActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.graphics.drawable.VectorDrawableCompat;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class Utils {
  public static Intent getHomeScreenIntent() {
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_HOME);
    return intent;
  }

  public static Intent getSettingsActivityIntent(Context context) {
    return new Intent(context, SettingsActivity.class);
  }

  public static boolean isAppLockDeactivationExpired(SharedPreferences sharedPreferences) {
    long millis = sharedPreferences.getLong(Constants.OVERLAY_DEACTIVATED_MILLIS, -1L);
    long timestamp = sharedPreferences.getLong(Constants.OVERLAY_DEACTIVATED_TIMESTAMP, -1L);
    return millis != -1L && timestamp + millis < System.currentTimeMillis();
  }

  public static boolean isAppLockActivated(SharedPreferences sharedPreferences) {
    return sharedPreferences.getBoolean(Constants.APP_LOCK_ACTIVATED, false)
        || isAppLockDeactivationExpired(sharedPreferences);
  }

  public static Func1<ApplicationInfo, Boolean> removeSelfPackage(final Context context) {
    return new Func1<ApplicationInfo, Boolean>() {
      @Override public Boolean call(ApplicationInfo applicationInfo) {
        return !applicationInfo.packageName.equals(context.getPackageName());
      }
    };
  }

  public static Func2<ApplicationInfo, ApplicationInfo, Integer> getApplicationSortFunction(
      final Context context) {
    return new Func2<ApplicationInfo, ApplicationInfo, Integer>() {
      @Override
      public Integer call(ApplicationInfo applicationInfo, ApplicationInfo applicationInfo2) {
        return applicationInfo.loadLabel(context.getPackageManager())
            .toString()
            .compareTo(applicationInfo2.loadLabel(context.getPackageManager()).toString());
      }
    };
  }

  public static Func1<? super ApplicationInfo, Boolean> removeLaunchers(final Context context) {
    return new Func1<ApplicationInfo, Boolean>() {
      @Override public Boolean call(ApplicationInfo applicationInfo) {
        //Hack for filtering launchers
        return !getApplicationName(applicationInfo, context).contains("launcher");
      }
    };
  }

  public static Func1<? super ApplicationInfo, Boolean> removeSystemApps() {
    return new Func1<ApplicationInfo, Boolean>() {
      @Override public Boolean call(ApplicationInfo applicationInfo) {
        return (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0;
      }
    };
  }

  public static VectorDrawableCompat createVectorDrawable(final Context context, int resourceId) {
    return VectorDrawableCompat.create(context.getResources(), resourceId, context.getTheme());
  }

  public static String getApplicationName(ApplicationInfo applicationInfo, Context context) {
    return applicationInfo.loadLabel(context.getPackageManager()).toString().toLowerCase();
  }

  public static Observable<List<ApplicationInfo>> deferedApplicationInfoFetcher(
      final Context context) {
    return getDeferedObservable(Observable.from(
        context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA))
        .filter(Utils.removeSelfPackage(context))
        .filter(Utils.removeLaunchers(context))
        .filter(Utils.removeSystemApps())
        .toSortedList(Utils.getApplicationSortFunction(context))).observeOn(
        AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
  }

  public static <T> Observable<T> getDeferedObservable(final Observable<T> observable) {
    return Observable.defer(new Func0<Observable<T>>() {
      @Override public Observable<T> call() {
        return observable;
      }
    });
  }
}
