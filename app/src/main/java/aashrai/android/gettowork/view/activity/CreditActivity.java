package aashrai.android.gettowork.view.activity;

import aashrai.android.gettowork.R;
import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;
import butterknife.OnClick;

public class CreditActivity extends BaseActivity {

  @Override public int getLayoutId() {
    return R.layout.activity_credits;
  }

  @Override public void configureDagger() {

  }

  @OnClick({ R.id.tv_creditAlex, R.id.tv_creditsAash })
  public void onTwitterHandleClick(TextView twitterHandle) {
    launchTwitterProfile(twitterHandle.getText().toString().substring(1));
  }

  private void launchTwitterProfile(String handle) {
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    intent.setData(Uri.parse("https://twitter.com/" + handle));
    startActivity(intent);
  }
}
