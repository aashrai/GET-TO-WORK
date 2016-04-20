package aashrai.android.gettowork;

import android.content.Intent;
import android.os.Bundle;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public int getLayoutId() {
    return R.layout.activity_main;
  }

  @OnClick(R.id.iv_settings) public void viewSettings() {
    startActivity(new Intent(this, SettingsActivity.class));
  }
}
