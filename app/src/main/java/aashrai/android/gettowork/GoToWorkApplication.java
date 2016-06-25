package aashrai.android.gettowork;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

import aashrai.android.gettowork.di.component.ApplicationComponent;
import aashrai.android.gettowork.di.component.DaggerApplicationComponent;
import aashrai.android.gettowork.di.module.ApplicationModule;
import aashrai.android.gettowork.utils.Constants;
import timber.log.Timber;

public class GoToWorkApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        applicationComponent =
                DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        checkAndAddCollection();
    }

    private void checkAndAddCollection() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getStringSet(Constants.ABUSE_COLLECTION_TITLE, null) == null) {
            Set<String> titles = new HashSet<>();
            titles.add(Constants.THE_FUCK_OFF_COLLECTION);
            sharedPreferences.edit().putStringSet(Constants.ABUSE_COLLECTION_TITLE, titles).apply();
        }
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
