package aashrai.android.gettowork;

import aashrai.android.gettowork.adapter.PackageListAdapter;
import aashrai.android.gettowork.presenter.SettingsActivityPresenter;
import aashrai.android.gettowork.view.SettingsView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.Bind;
import javax.inject.Inject;

public class SettingsActivity extends BaseActivity implements SettingsView {

  @Bind(R.id.rv_packages) RecyclerView packageList;
  @Bind(R.id.progress) ProgressBar progressBar;
  @Inject SettingsActivityPresenter presenter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((GoToWorkApplication) getApplication()).getApplicationComponent()
        .getSettingsComponent()
        .inject(this);
    presenter.setView(this);
  }

  @Override public int getLayoutId() {
    return R.layout.activity_settings;
  }

  @Override public void setPacakgeListAdapter(PackageListAdapter adapter) {
    packageList.setLayoutManager(new LinearLayoutManager(this));
    packageList.setAdapter(adapter);
  }

  @Override public void startProgressBar() {
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override public void stopProgressBar() {
    progressBar.setVisibility(View.GONE);
  }
}


