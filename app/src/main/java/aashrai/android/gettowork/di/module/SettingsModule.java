package aashrai.android.gettowork.di.module;

import aashrai.android.gettowork.di.SettingsScope;
import aashrai.android.gettowork.model.ApplicationsInfoStore;
import aashrai.android.gettowork.presenter.SettingsActivityPresenter;
import android.content.Context;
import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;

@Module public class SettingsModule {

  @Provides @SettingsScope SettingsActivityPresenter getSettingsActivityPresenter(Context context,
      SharedPreferences sharedPreferences, ApplicationsInfoStore applicationsInfoStore) {
    return new SettingsActivityPresenter(context, sharedPreferences, applicationsInfoStore);
  }
}
