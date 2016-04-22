package aashrai.android.gettowork.adapter;

import aashrai.android.gettowork.R;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import java.util.List;
import java.util.Set;

public class PackageListAdapter extends RecyclerView.Adapter<PackageListAdapter.ViewHolder> {

  private final List<ApplicationInfo> packageList;
  private final Set<String> activatedPackages;
  private final PackageManager packageManager;
  private static final String TAG = "PackageListAdapter";

  public PackageListAdapter(List<ApplicationInfo> packageList, Set<String> activatedPackages,
      PackageManager packageManager) {
    this.packageList = packageList;
    this.activatedPackages = activatedPackages;
    this.packageManager = packageManager;

    //Log.d(TAG, "PackageListAdapter: activated packages " + activatedPackages);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.packageName.setText(packageList.get(position).loadLabel(packageManager));
    holder.packageName.setChecked(
        activatedPackages.contains(packageList.get(position).packageName));
    holder.thumbnail.setImageDrawable(packageList.get(position).loadIcon(packageManager));
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

  public class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.sw_activate) Switch packageName;
    @Bind(R.id.iv_thumbnail) ImageView thumbnail;
    boolean defaultCheck;

    public ViewHolder(View itemView) {
      super(itemView);
      defaultCheck = true;
      ButterKnife.bind(this, itemView);
    }

    @OnCheckedChanged(R.id.sw_activate) public void activateForPackage(boolean flag) {
      String packageName = packageList.get(getAdapterPosition()).packageName;
      if (flag) {
        activatedPackages.add(packageName);
      } else {
        activatedPackages.remove(packageName);
      }
    }
  }
}
