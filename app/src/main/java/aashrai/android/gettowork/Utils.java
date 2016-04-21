package aashrai.android.gettowork;

import aashrai.android.gettowork.view.activity.SettingsActivity;
import android.content.Context;
import android.content.Intent;

public class Utils {

  public static Intent getHomeScreenIntent() {
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_HOME);
    return intent;
  }

  public static Intent getSettingsActivityIntent(Context context){
    return new Intent(context, SettingsActivity.class);
  }
}
