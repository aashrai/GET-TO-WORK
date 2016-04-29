package aashrai.android.gettowork.di.component;

import aashrai.android.gettowork.di.module.ApplicationModule;
import aashrai.android.gettowork.di.module.SettingsModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = ApplicationModule.class) public interface ApplicationComponent {

  SettingsComponent getSettingsComponent(SettingsModule settingsModule);

  MainActivityComponent getMainActivityComponent();
}
