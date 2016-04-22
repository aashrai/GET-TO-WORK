package aashrai.android.gettowork.presenter;

import aashrai.android.gettowork.Constants;
import aashrai.android.gettowork.adapter.PackageListAdapter;
import aashrai.android.gettowork.di.SettingsScope;
import aashrai.android.gettowork.view.SettingsView;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

@SettingsScope public class SettingsActivityPresenter extends Subscriber<List<ApplicationInfo>> {

  private final Context context;
  private final SharedPreferences preferences;
  private final Set<String> activatedPackages;
  private SettingsView settingsView;
  private List<ApplicationInfo> packageList;
  private final CompositeSubscription compositeSubscription;
  private Subscription searchSubscription;

  @Inject public SettingsActivityPresenter(Context context, SharedPreferences preferences) {
    this.context = context;
    this.preferences = preferences;
    compositeSubscription = new CompositeSubscription();
    packageList = new ArrayList<>();
    //A copy is created because of a bug in android see issue 27801
    activatedPackages = new HashSet<>(
        preferences.getStringSet(Constants.ACTIVATED_PACKAGES, new HashSet<String>()));
  }

  public void setView(SettingsView settingsView) {
    this.settingsView = settingsView;
    Subscription subscription = Observable.just(
        context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this);
    compositeSubscription.add(subscription);
  }

  private PackageListAdapter createPackageAdapter(List<ApplicationInfo> applicationInfoList) {
    packageList.addAll(applicationInfoList);
    return new PackageListAdapter(applicationInfoList, activatedPackages,
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
    settingsView.setPackageListAdapter(createPackageAdapter(applicationInfoList));
  }

  public void onSearch(final String query) {
    //Remove any pending searches
    if (searchSubscription != null) searchSubscription.unsubscribe();
    searchSubscription = performSearch(query);
    compositeSubscription.add(searchSubscription);
  }

  private Subscription performSearch(String query) {
    final String modifiedQuery = query.toLowerCase().trim();
    return Observable.from(packageList)
        .filter(new Func1<ApplicationInfo, Boolean>() {
          @Override public Boolean call(ApplicationInfo applicationInfo) {
            return modifiedQuery.isEmpty() || applicationInfo.loadLabel(context.getPackageManager())
                .toString()
                .toLowerCase()
                .contains(modifiedQuery);
          }
        })
        .toList()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SearchSubscription(new WeakReference<>(settingsView)));
  }

  public void onDestroy() {
    compositeSubscription.unsubscribe();
  }

  public static class SearchSubscription extends Subscriber<List<ApplicationInfo>> {

    private final SettingsView settingsView;

    public SearchSubscription(WeakReference<SettingsView> weakReference) {
      this.settingsView = weakReference.get();
    }

    @Override public void onStart() {
      super.onStart();
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

  public void onBackPressed() {
    //Log.d(TAG, "onBackPressed() " + activatedPackages);
    preferences.edit().putStringSet(Constants.ACTIVATED_PACKAGES, activatedPackages).apply();
  }
}
