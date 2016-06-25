package aashrai.android.gettowork.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import aashrai.android.gettowork.R;
import aashrai.android.gettowork.utils.AppIconRequestHandler;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class PackageListAdapter extends RecyclerView.Adapter<PackageListAdapter.ViewHolder> {

  private final List<ApplicationInfo> packageList;
  private final Set<String> activatedPackages;
  private final PackageManager packageManager;
  private final Picasso picasso;
  private static final String TAG = "PackageListAdapter";

  public PackageListAdapter(Set<String> activatedPackages,
      Context context) {
    this.packageList = new ArrayList<>();
    this.activatedPackages = activatedPackages;
    this.packageManager = context.getPackageManager();
    picasso =
        new Picasso.Builder(context).addRequestHandler(new AppIconRequestHandler(context)).build();

    //Log.d(TAG, "PackageListAdapter: activated packages " + activatedPackages);
  }

  public interface onPackageToggleListener {
    void onPackageToggle(String packageName, boolean activated);
  }

  private onPackageToggleListener packageToggleListener;

  public void setPackageToggleListener(onPackageToggleListener packageToggleListener) {
    this.packageToggleListener = packageToggleListener;
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    String packageName = packageList.get(position).packageName;
    holder.packageName.setText(packageManager.getApplicationLabel(packageList.get(position)));
    holder.packageName.setChecked(activatedPackages.contains(packageName));
    picasso.load(AppIconRequestHandler.getUri(packageName)).noFade().into(holder.thumbnail);
  }

  @Override public int getItemCount() {
    return packageList.size();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package, parent, false));
  }

  public void updatePackageList(List<ApplicationInfo> packageList) {
    int currentSize = this.packageList.size();
    int updatedSize = packageList.size();
    updateItems(packageList);
    configureItemAnimations(currentSize, updatedSize);
  }

  private void updateItems(List<ApplicationInfo> packageList) {
    this.packageList.clear();
    this.packageList.addAll(packageList);
  }

  private void configureItemAnimations(int currentSize, int updatedSize) {
    notifyItemRangeRemoved(0, currentSize);
    notifyItemRangeInserted(0, updatedSize);
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.sw_activate) Switch packageName;
    @BindView(R.id.iv_thumbnail) ImageView thumbnail;
    boolean defaultCheck;

    public ViewHolder(View itemView) {
      super(itemView);
      defaultCheck = true;
      ButterKnife.bind(this, itemView);
    }

    @OnCheckedChanged(R.id.sw_activate) public void activateForPackage(boolean flag) {
      String packageName = packageList.get(getAdapterPosition()).packageName;
      packageToggleListener.onPackageToggle(packageName, flag);
    }
  }
}
