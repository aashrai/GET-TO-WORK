package aashrai.android.gettowork.di.module;

import aashrai.android.gettowork.Constants;
import aashrai.android.gettowork.di.MainActivityScope;
import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;
import java.util.HashSet;
import java.util.Set;

@Module public class MainActivityModule {

  @Provides @MainActivityScope Set<String> getActivatedPackages(
      SharedPreferences sharedPreferences) {
    return sharedPreferences.getStringSet(Constants.ACTIVATED_PACKAGES, new HashSet<String>());
  }
}
