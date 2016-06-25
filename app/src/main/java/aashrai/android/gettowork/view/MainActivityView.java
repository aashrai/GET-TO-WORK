package aashrai.android.gettowork.view;

import android.content.Context;
import android.graphics.drawable.Drawable;

public interface MainActivityView extends BaseView {

    void showToast(String message);

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

    void showAccessibilityDialog();

    Context getActivityContext();
}
