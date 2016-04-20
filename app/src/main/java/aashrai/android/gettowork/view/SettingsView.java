package aashrai.android.gettowork.view;

import aashrai.android.gettowork.adapter.PackageListAdapter;
import android.content.pm.ApplicationInfo;
import java.util.List;

public interface SettingsView {

  void setPackageListAdapter(PackageListAdapter adapter);

  void updatePackageListAdapter(List<ApplicationInfo> packageList);

  void startProgressBar();

  void stopProgressBar();
}
