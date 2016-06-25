package aashrai.android.gettowork.model;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.HashSet;

import javax.inject.Inject;

import aashrai.android.gettowork.di.AbuseCollectionScope;
import aashrai.android.gettowork.utils.Constants;

@AbuseCollectionScope
public class AbuseCollectionModel {

    private final Context context;

    @Inject
    public AbuseCollectionModel(Context context) {
        this.context = context;
    }

    public String[] getAbuseCollectionTitles() {
        return (String[]) PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(Constants.ABUSE_COLLECTION_TITLE, new HashSet<String>()).toArray();
    }
}
