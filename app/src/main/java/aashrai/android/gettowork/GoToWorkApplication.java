package aashrai.android.gettowork;

import aashrai.android.gettowork.di.component.ApplicationComponent;
import aashrai.android.gettowork.di.component.DaggerApplicationComponent;
import aashrai.android.gettowork.di.module.ApplicationModule;
import android.app.Application;
import timber.log.Timber;

public class GoToWorkApplication extends Application {

  private ApplicationComponent applicationComponent;

  @Override public void onCreate() {
    super.onCreate();
    Timber.plant(new Timber.DebugTree());
    applicationComponent =
        DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
  }

  public ApplicationComponent getApplicationComponent() {
    return applicationComponent;
  }
}
