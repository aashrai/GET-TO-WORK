package aashrai.android.gettowork.di.component;

import aashrai.android.gettowork.di.SettingsScope;
import aashrai.android.gettowork.di.module.SettingsModule;
import aashrai.android.gettowork.view.activity.SettingsActivity;
import dagger.Subcomponent;

@Subcomponent(modules = SettingsModule.class) @SettingsScope public interface SettingsComponent {

  void inject(SettingsActivity settingsActivity);
}
