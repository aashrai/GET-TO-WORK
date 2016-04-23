package aashrai.android.gettowork.view.activity;

import aashrai.android.gettowork.utils.Constants;
import aashrai.android.gettowork.R;
import aashrai.android.gettowork.utils.Utils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import butterknife.Bind;
import java.util.Random;

public class AppLockActivity extends BaseActivity {

  @Bind(R.id.tv_fuckOff) TextView fuckOff;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Random random = new Random();
    int index = random.nextInt(Constants.FUCK_OFF_MESSAGES.length);
    fuckOff.setText(Constants.FUCK_OFF_MESSAGES[index]);
  }

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
