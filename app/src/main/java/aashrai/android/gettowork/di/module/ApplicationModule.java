package aashrai.android.gettowork.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class ApplicationModule {

  private final Context context;
  private final SharedPreferences sharedPreferences;
  private final Resources resources;
  private final Resources.Theme theme;

  public ApplicationModule(Context context) {
    this.context = context;
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    this.resources = context.getResources();
    this.theme = context.getTheme();
  }

  @Provides @Singleton public Context getContext() {
    return context;
  }

  @Provides @Singleton public SharedPreferences getSharedPreferences() {
    return sharedPreferences;
  }

  @Provides @Singleton public Resources getResources() {
    return resources;
  }

  @Provides @Singleton public Resources.Theme getTheme() {
    return theme;
  }
}
