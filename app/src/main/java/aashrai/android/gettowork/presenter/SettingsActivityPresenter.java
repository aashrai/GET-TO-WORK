package aashrai.android.gettowork.presenter;

import aashrai.android.gettowork.adapter.PackageListAdapter;
import aashrai.android.gettowork.di.SettingsScope;
import aashrai.android.gettowork.model.ApplicationsInfoStore;
import aashrai.android.gettowork.utils.Constants;
import aashrai.android.gettowork.view.SettingsView;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

@SettingsScope public class SettingsActivityPresenter
    implements PackageListAdapter.onPackageToggleListener {

  private final Context context;
  private final SharedPreferences preferences;
  private final Set<String> activatedPackages;
  private final ApplicationsInfoStore applicationsInfoStore;
  private Observable<List<ApplicationInfo>> applicationInfoObservable;
  private PackageListAdapter packageListAdapter;
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

  public SettingsActivityPresenter setView(SettingsView settingsView) {
    this.settingsView = settingsView;
    settingsView.configureRecyclerView(getPackageAdapterInstance());
    return this;
  }

  public void execute() {
    if (applicationInfoObservable == null) {
      applicationInfoObservable =
          applicationsInfoStore.getAllInstalledApplications();
    }
    updateSubscription();
  }

  private void updateSubscription() {
    searchSubscription = applicationInfoObservable.subscribe(new PackageFetchSubscriber());
  }

  private PackageListAdapter getPackageAdapterInstance() {
    if (packageListAdapter == null) {
      packageListAdapter = new PackageListAdapter(activatedPackages, context);
      packageListAdapter.setPackageToggleListener(this);
    }
    return packageListAdapter;
  }

  public void onSearch(final String query) {
    //Remove any pending searches
    if (searchSubscription != null) searchSubscription.unsubscribe();
    applicationInfoObservable = applicationsInfoStore.getInstalledApplications(query);
    updateSubscription();
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

  @RxLogSubscriber private class PackageFetchSubscriber extends Subscriber<List<ApplicationInfo>> {

    public PackageFetchSubscriber() {
    }

    @Override public void onStart() {
      settingsView.startProgressBar();
    }

    @Override public void onCompleted() {
      settingsView.stopProgressBar();
    }

    @Override public void onError(Throwable e) {
      settingsView.stopProgressBar();
    }

    @Override public void onNext(List<ApplicationInfo> applicationInfoList) {
      settingsView.updatePackageListAdapter(applicationInfoList);
    }
  }
}
