package aashrai.android.gettowork.view.activity;

import aashrai.android.gettowork.Constants;
import aashrai.android.gettowork.GoToWorkApplication;
import aashrai.android.gettowork.R;
import aashrai.android.gettowork.presenter.MainActivityPresenter;
import aashrai.android.gettowork.view.MainActivityView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainActivityView {

  @Inject MainActivityPresenter presenter;
  @Inject SharedPreferences sharedPreferences;
  @Bind(R.id.iv_activate) ImageView activate;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter.setView(this);
    setActivateDrawable();
  }

  private void setActivateDrawable() {
    activate.setImageDrawable(sharedPreferences.getBoolean(Constants.APP_LOCK_ACTIVATED, false)
        ? ContextCompat.getDrawable(this, R.drawable.ic_pause_circle)
        : ContextCompat.getDrawable(this, R.drawable.ic_play_circle));
  }

  @Override public void configureDagger() {
    ((GoToWorkApplication) getApplication()).getApplicationComponent()
        .getMainActivityComponent()
        .inject(this);
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

  @Override public void changeActivateDrawable(Drawable drawable) {
    activate.setImageDrawable(drawable);
  }
}
