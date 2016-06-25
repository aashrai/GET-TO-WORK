package aashrai.android.gettowork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import javax.inject.Inject;

import aashrai.android.gettowork.GoToWorkApplication;
import aashrai.android.gettowork.R;
import aashrai.android.gettowork.adapter.AbuseCollectionAdapter;
import aashrai.android.gettowork.presenter.AbuseCollectionPresenter;
import aashrai.android.gettowork.view.AbuseCollectionsView;
import butterknife.BindView;

public class AbuseCollectionsActivity extends BaseActivity implements AbuseCollectionsView {

    @BindView(R.id.rv_abuse_collection)
    RecyclerView abuseCollection;
    @Inject
    AbuseCollectionPresenter abuseCollectionPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        abuseCollection.setLayoutManager(new GridLayoutManager(this, 2));
        abuseCollectionPresenter.setView(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_abuse_collection;
    }

    @Override
    public void configureDagger() {
        ((GoToWorkApplication) getApplication()).getApplicationComponent()
                .getAbuseCollectionComponent().inject(this);
    }

    @Override
    public void setCollectionTitles(String[] titles) {
        abuseCollection.setAdapter(new AbuseCollectionAdapter(titles));
    }

    @Override
    public void launchActivity(Intent intent) {
        startActivity(intent);
    }
}
