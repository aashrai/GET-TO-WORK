package aashrai.android.gettowork.presenter;

import android.content.Context;

import javax.inject.Inject;

import aashrai.android.gettowork.di.AbuseCollectionScope;
import aashrai.android.gettowork.model.AbuseCollectionModel;
import aashrai.android.gettowork.view.AbuseCollectionsView;

@AbuseCollectionScope
public class AbuseCollectionPresenter {

    private AbuseCollectionsView abuseCollectionsView;
    private final Context context;
    private final AbuseCollectionModel model;

    @Inject
    public AbuseCollectionPresenter(Context context, AbuseCollectionModel model) {
        this.context = context;
        this.model = model;
    }

    public void setView(AbuseCollectionsView abuseCollectionsView) {
        this.abuseCollectionsView = abuseCollectionsView;
        this.abuseCollectionsView.setCollectionTitles(model.getAbuseCollectionTitles());
    }
}
