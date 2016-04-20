package aashrai.android.gettowork.di.component;

import aashrai.android.gettowork.SettingsActivity;
import aashrai.android.gettowork.di.SettingsScope;
import dagger.Subcomponent;

@Subcomponent/*(modules = SettingsModule.class)*/ @SettingsScope public interface SettingsComponent {

  void inject(SettingsActivity settingsActivity);
}
