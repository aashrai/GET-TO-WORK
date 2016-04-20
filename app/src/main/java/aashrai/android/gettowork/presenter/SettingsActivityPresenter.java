package aashrai.android.gettowork.presenter;

import aashrai.android.gettowork.Constants;
import aashrai.android.gettowork.adapter.PackageListAdapter;
import aashrai.android.gettowork.di.SettingsScope;
import aashrai.android.gettowork.view.SettingsView;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.util.HashSet;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@SettingsScope public class SettingsActivityPresenter extends Subscriber<List<ApplicationInfo>> {

  private final Context context;
  private final SharedPreferences preferences;
  private SettingsView settingsView;

  @Inject public SettingsActivityPresenter(Context context, SharedPreferences preferences) {
    this.context = context;
    this.preferences = preferences;
  }

  public void setView(SettingsView settingsView) {
    this.settingsView = settingsView;
    Observable.just(
        context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this);
  }

  private PackageListAdapter createPackageAdapter(List<ApplicationInfo> applicationInfoList) {
    return new PackageListAdapter(applicationInfoList,
        preferences.getStringSet(Constants.ACTIVATED_PACKAGES, new HashSet<String>()),
        context.getPackageManager());
  }

  @Override public void onStart() {
    settingsView.startProgressBar();
  }

  @Override public void onCompleted() {
    settingsView.stopProgressBar();
    unsubscribe();
  }

  @Override public void onError(Throwable e) {
    settingsView.stopProgressBar();
  }

  @Override public void onNext(List<ApplicationInfo> applicationInfoList) {
    settingsView.setPacakgeListAdapter(createPackageAdapter(applicationInfoList));
  }
}
