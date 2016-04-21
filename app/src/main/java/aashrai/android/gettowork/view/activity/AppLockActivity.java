package aashrai.android.gettowork.view.activity;

import aashrai.android.gettowork.R;
import aashrai.android.gettowork.Utils;

public class AppLockActivity extends BaseActivity {

  @Override public int getLayoutId() {
    return R.layout.activity_applock;
  }

  @Override public void configureDagger() {

  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    startActivity(Utils.getHomeScreenIntent());
  }
}
