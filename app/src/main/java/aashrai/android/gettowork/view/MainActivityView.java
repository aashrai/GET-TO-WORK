package aashrai.android.gettowork.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public interface MainActivityView {

  void showToast(String message);

  void launchActivity(Intent intent);

  void changeActivateDrawable(Drawable drawable);
}
