package aashrai.android.gettowork.di.component;


import aashrai.android.gettowork.di.AbuseCollectionScope;
import aashrai.android.gettowork.view.activity.AbuseCollectionsActivity;
import dagger.Subcomponent;

@Subcomponent @AbuseCollectionScope
public interface AbuseCollectionComponent {
    void inject(AbuseCollectionsActivity abuseCollectionsActivity);
}
