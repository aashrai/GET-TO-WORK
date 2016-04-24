package aashrai.android.gettowork.di.component;

import aashrai.android.gettowork.di.module.ApplicationModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

  SettingsComponent getSettingsComponent();

  MainActivityComponent getMainActivityComponent();
}
