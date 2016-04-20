package aashrai.android.gettowork.view;

import aashrai.android.gettowork.adapter.PackageListAdapter;

public interface SettingsView {

  void setPacakgeListAdapter(PackageListAdapter adapter);

  void startProgressBar();

  void stopProgressBar();
}
