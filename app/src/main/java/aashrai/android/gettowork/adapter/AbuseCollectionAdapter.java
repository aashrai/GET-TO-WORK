package aashrai.android.gettowork.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import aashrai.android.gettowork.R;

public class AbuseCollectionAdapter extends RecyclerView.Adapter<AbuseCollectionAdapter.ViewHolder> {

    private final String[] abuseCollections;

    public AbuseCollectionAdapter(String[] abuseCollections) {
        this.abuseCollections = abuseCollections;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_abuse_collection, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.collectionName.setText(abuseCollections[position]);
    }

    @Override
    public int getItemCount() {
        return abuseCollections.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button collectionName;

        ViewHolder(View itemView) {
            super(itemView);
            collectionName = (Button) itemView;
        }
    }
}
