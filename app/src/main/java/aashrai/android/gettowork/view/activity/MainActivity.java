package aashrai.android.gettowork.view.activity;

import aashrai.android.gettowork.GoToWorkApplication;
import aashrai.android.gettowork.R;
import aashrai.android.gettowork.adapter.TimingGridAdapter;
import aashrai.android.gettowork.di.component.MainActivityComponent;
import aashrai.android.gettowork.presenter.MainActivityPresenter;
import aashrai.android.gettowork.utils.TimingGridDecorator;
import aashrai.android.gettowork.utils.Utils;
import aashrai.android.gettowork.view.MainActivityView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class MainActivity extends BaseActivity
    implements MainActivityView, TimingGridAdapter.onTimingClickListener {

  @Inject MainActivityPresenter presenter;
  @Inject SharedPreferences sharedPreferences;
  @Bind(R.id.iv_activate) ImageView activate;
  @Bind(R.id.rv_timing_grid) RecyclerView timingsRecyclerView;
  @Bind(R.id.tv_pauseWarning) TextView pauseWarning;
  @Bind(R.id.tv_activateHeader) TextView activateHeader;
  MainActivityComponent mainActivityComponent;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter.setView(this);
    presenter.checkAccessibilityEnabled();
    setActivateDrawable();
    configureTimingsRecyclerView();
  }

  private void configureTimingsRecyclerView() {
    timingsRecyclerView.setAdapter(getTimingGridAdapter());
    timingsRecyclerView.setLayoutManager(getTimingLayoutManager());
    timingsRecyclerView.addItemDecoration(new TimingGridDecorator());
  }

  @NonNull private GridLayoutManager getTimingLayoutManager() {
    GridLayoutManager timingGridLayoutManager = new GridLayoutManager(this, 6);
    timingGridLayoutManager.setSpanSizeLookup(getSpanSizeLookup());
    return timingGridLayoutManager;
  }

  @NonNull private GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
    return new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        switch (position) {
          case 0:
            return 6;
          case 1:
          case 2:
          case 6:
          case 7:
            return 3;
          case 3:
          case 4:
          case 5:
            return 2;
        }
        return 0;
      }
    };
  }

  @NonNull private TimingGridAdapter getTimingGridAdapter() {
    List<String> timings =
        Arrays.asList("5 mins", "20 mins", "1 hr", "3 hrs", "6 hrs", "12 hrs", "24 hrs", "48 hrs");
    TimingGridAdapter timingGridAdapter = new TimingGridAdapter(timings);
    timingGridAdapter.setTimingClickListener(this);
    return timingGridAdapter;
  }

  private void setActivateDrawable() {
    activate.setImageDrawable(
        Utils.isAppLockActivated(sharedPreferences) ? Utils.createVectorDrawable(this,
            R.drawable.ic_pause_circle)
            : Utils.createVectorDrawable(this, R.drawable.ic_play_circle));
  }

  @Override public void configureDagger() {
    mainActivityComponent = ((GoToWorkApplication) getApplication()).getApplicationComponent()
        .getMainActivityComponent();
    mainActivityComponent.inject(this);
  }

  @OnClick(R.id.iv_activate) public void onActivate() {
    presenter.onAppLockActivateClick();
  }

  @Override public int getLayoutId() {
    return R.layout.activity_main;
  }

  @OnClick(R.id.iv_settings) public void viewSettings() {
    startActivity(new Intent(this, SettingsActivity.class));
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
  }

  @Override public void showToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }

  @Override public void launchActivity(Intent intent) {
    startActivity(intent);
  }

  @Override public void setActivateDrawable(Drawable drawable) {
    activate.setImageDrawable(drawable);
  }

  @OnClick(R.id.tv_credits) public void onCreditTextClick() {
    presenter.onCreditTextClick();
  }

  @OnClick(R.id.tv_github) public void onGithubClick() {
    presenter.onGithubClick();
  }

  @Override public String getWarningText() {
    return pauseWarning.getText().toString();
  }

  @Override public void showWarningText() {
    pauseWarning.setVisibility(View.VISIBLE);
  }

  @Override public void hideWarningText() {
    pauseWarning.setVisibility(View.GONE);
  }

  @Override public void setWarningText(String text) {
    pauseWarning.setText(text);
  }

  @Override public void showTimingGrid() {
    timingsRecyclerView.setVisibility(View.VISIBLE);
  }

  @Override public void hideActivateHeader() {
    activateHeader.setVisibility(View.GONE);
  }

  @Override public void showActivateHeader() {
    activateHeader.setVisibility(View.VISIBLE);
  }

  @Override public void hideTimingGrid() {
    timingsRecyclerView.setVisibility(View.GONE);
  }

  @Override public void showActivateButton() {
    activate.setVisibility(View.VISIBLE);
  }

  @Override public void hideActivateButton() {
    activate.setVisibility(View.GONE);
  }

  @Override public void showAccessibilityDialog() {
    new AlertDialog.Builder(this).setTitle("Enable Accessibility")
        .setMessage("This app requires accessibility permission for working properly")
        .setPositiveButton("SURE", presenter)
        .setCancelable(false)
        .create()
        .show();
  }

  @Override public Context getActivityContext() {
    return this;
  }

  @OnClick(R.id.tv_pauseWarning) public void onWarningTextClick() {
    presenter.onWarningTextClick();
  }

  @Override public void onTimingClick(String timing) {
    presenter.onTimingClick(timing);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mainActivityComponent = null;
  }
}
