package aashrai.android.gettowork.view;

import aashrai.android.gettowork.GoToWorkApplication;
import aashrai.android.gettowork.R;
import aashrai.android.gettowork.presenter.MainActivityPresenter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import butterknife.OnClick;
import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainActivityView {

  @Inject MainActivityPresenter presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((GoToWorkApplication) getApplication()).getApplicationComponent()
        .getMainActivityComponent()
        .inject(this);
    presenter.setView(this);
  }

  @OnClick(R.id.iv_activate) public void onActivate() {
    presenter.onAppLockActivate();
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
}
