package aashrai.android.gettowork.presenter;

import aashrai.android.gettowork.adapter.PackageListAdapter;
import aashrai.android.gettowork.di.SettingsScope;
import aashrai.android.gettowork.model.ApplicationsInfoStore;
import aashrai.android.gettowork.utils.Constants;
import aashrai.android.gettowork.view.SettingsView;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import rx.Subscriber;
import rx.Subscription;

@SettingsScope public class SettingsActivityPresenter
    implements PackageListAdapter.onPackageToggleListener {

  private final Context context;
  private final SharedPreferences preferences;
  private final Set<String> activatedPackages;
  private final ApplicationsInfoStore applicationsInfoStore;
  private SettingsView settingsView;
  private Subscription searchSubscription;
  private static final String TAG = "SettingsActivity";

  @Inject public SettingsActivityPresenter(Context context, SharedPreferences preferences,
      ApplicationsInfoStore applicationsInfoStore) {
    this.context = context;
    this.preferences = preferences;
    this.applicationsInfoStore = applicationsInfoStore;
    //A copy is created because of a bug in android see issue 27801
    activatedPackages = new HashSet<>(
        preferences.getStringSet(Constants.ACTIVATED_PACKAGES, new HashSet<String>()));
  }

  public void setView(SettingsView settingsView) {
    this.settingsView = settingsView;
    settingsView.configureRecyclerView(createPackageAdapter());
  }

  private PackageListAdapter createPackageAdapter() {
    PackageListAdapter packageListAdapter = new PackageListAdapter(activatedPackages, context);
    packageListAdapter.setPackageToggleListener(this);
    return packageListAdapter;
  }

  public void onSearch(final String query) {
    //Remove any pending searches
    if (searchSubscription != null) searchSubscription.unsubscribe();
    searchSubscription = performSearch(query);
  }

  private Subscription performSearch(String query) {
    return applicationsInfoStore.getInstalledApplications(query)
        .subscribe(new PackageFetchSubscriber());
  }

  public void onDestroy() {
    searchSubscription.unsubscribe();
  }

  @Override public void onPackageToggle(String packageName, boolean activated) {
    if (activated) {
      activatedPackages.add(packageName);
    } else {
      activatedPackages.remove(packageName);
    }
    preferences.edit().putStringSet(Constants.ACTIVATED_PACKAGES, activatedPackages).apply();
  }

  private class PackageFetchSubscriber extends Subscriber<List<ApplicationInfo>> {

    public PackageFetchSubscriber() {
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
      unsubscribe();
    }

    @Override public void onNext(List<ApplicationInfo> applicationInfoList) {
      settingsView.updatePackageListAdapter(applicationInfoList);
    }
  }
}
