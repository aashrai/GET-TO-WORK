package aashrai.android.gettowork.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public interface MainActivityView {

  void showToast(String message);

  void launchActivity(Intent intent);

  void setActivateDrawable(Drawable drawable);

  String getWarningText();

  void showWarningText();

  void hideWarningText();

  void setWarningText(String text);

  void showTimingGrid();

  void hideActivateHeader();

  void showActivateHeader();

  void hideTimingGrid();

  void showActivateButton();

  void hideActivateButton();
}
