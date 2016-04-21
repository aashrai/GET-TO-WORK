package aashrai.android.gettowork.view;

import android.content.Intent;

public interface MainActivityView {

  void showToast(String message);

  void launchActivity(Intent intent);
}
