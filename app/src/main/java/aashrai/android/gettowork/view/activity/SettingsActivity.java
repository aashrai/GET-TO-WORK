package aashrai.android.gettowork.view.activity;

import aashrai.android.gettowork.GoToWorkApplication;
import aashrai.android.gettowork.R;
import aashrai.android.gettowork.adapter.PackageListAdapter;
import aashrai.android.gettowork.di.component.SettingsComponent;
import aashrai.android.gettowork.di.module.SettingsModule;
import aashrai.android.gettowork.presenter.SettingsActivityPresenter;
import aashrai.android.gettowork.utils.AppListDecorator;
import aashrai.android.gettowork.view.SettingsView;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class SettingsActivity extends BaseActivity
    implements SettingsView, Action1<CharSequence>, TextView.OnEditorActionListener {

  @Bind(R.id.rv_packages) RecyclerView packageList;
  @Bind(R.id.progress) ProgressBar progressBar;
  @Bind(R.id.et_search) EditText search;
  @Inject SettingsActivityPresenter presenter;
  PackageListAdapter adapter;
  Subscription searchSubscription;
  private static final String TAG = "SettingsActivity";
  boolean straySearch = true;
  SettingsComponent settingsComponent;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter.setView(this).execute();
    configureSearch();
  }

  private void configureSearch() {
    search.setCompoundDrawablesWithIntrinsicBounds(null, null,
        ContextCompat.getDrawable(this, R.drawable.ic_search), null);
    search.setOnEditorActionListener(this);
    searchSubscription = RxTextView.textChanges(search)
        .debounce(100, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Func1<CharSequence, String>() {
          @Override public String call(CharSequence charSequence) {
            return charSequence.toString();
          }
        })
        //.startWith("")
        .subscribe(new SearchBarSubscriber());
  }

  @Override public void configureDagger() {
    settingsComponent = ((GoToWorkApplication) getApplication()).getApplicationComponent()
        .getSettingsComponent(createSettingsModule());
    settingsComponent.inject(this);
  }

  @NonNull private SettingsModule createSettingsModule() {
    return new SettingsModule((SettingsActivityPresenter) getLastCustomNonConfigurationInstance());
  }

  @Override public int getLayoutId() {
    return R.layout.activity_settings;
  }

  @Override public void configureRecyclerView(PackageListAdapter adapter) {
    this.adapter = adapter;
    packageList.setLayoutManager(new LinearLayoutManager(this));
    packageList.addItemDecoration(new AppListDecorator());
    packageList.setAdapter(this.adapter);
  }

  @Override public void updatePackageListAdapter(List<ApplicationInfo> packageList) {
    adapter.updatePackageList(packageList);
  }

  @Override public void startProgressBar() {
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override public void stopProgressBar() {
    progressBar.setVisibility(View.GONE);
  }

  @Override public void call(CharSequence charSequence) {
    presenter.onSearch(charSequence.toString());
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    searchSubscription.unsubscribe();
    presenter.onDestroy();
    settingsComponent = null;
  }

  @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    if (actionId == EditorInfo.IME_ACTION_DONE) {
      InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
      return true;
    }
    return false;
  }

  @Override public Object onRetainCustomNonConfigurationInstance() {
    return presenter;
  }

  private class SearchBarSubscriber implements Action1<String> {

    @Override public void call(String query) {
      if (!straySearch) {
        presenter.onSearch(query);
      } else {
        straySearch = false;
      }
    }
  }
}


