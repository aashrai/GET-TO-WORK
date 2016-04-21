package aashrai.android.gettowork.di.component;

import aashrai.android.gettowork.view.MainActivity;
import aashrai.android.gettowork.di.MainActivityScope;
import aashrai.android.gettowork.di.module.MainActivityModule;
import dagger.Subcomponent;

@Subcomponent(modules = MainActivityModule.class) @MainActivityScope
public interface MainActivityComponent {

  void inject(MainActivity mainActivity);
}
