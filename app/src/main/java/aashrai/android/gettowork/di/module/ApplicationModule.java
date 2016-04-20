package aashrai.android.gettowork.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class ApplicationModule {

  private final Context context;
  private final SharedPreferences sharedPreferences;

  public ApplicationModule(Context context) {
    this.context = context;
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
  }

  @Provides @Singleton public Context getContext() {
    return context;
  }

  @Provides @Singleton public SharedPreferences getSharedPreferences() {
    return sharedPreferences;
  }
}
