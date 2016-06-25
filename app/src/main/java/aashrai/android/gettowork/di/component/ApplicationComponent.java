package aashrai.android.gettowork.di.component;

import javax.inject.Singleton;

import aashrai.android.gettowork.di.module.ApplicationModule;
import aashrai.android.gettowork.di.module.SettingsModule;
import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    SettingsComponent getSettingsComponent(SettingsModule settingsModule);

    MainActivityComponent getMainActivityComponent();

    AbuseCollectionComponent getAbuseCollectionComponent();
}
