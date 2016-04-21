package aashrai.android.gettowork.adapter;

import aashrai.android.gettowork.R;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.List;

public class TimingGridAdapter extends RecyclerView.Adapter<TimingGridAdapter.ViewHolder> {

  private final List<String> timings;
  private onTimingClickListener timingClickListener;

  public TimingGridAdapter(List<String> timings) {
    this.timings = timings;
  }

  public void setTimingClickListener(onTimingClickListener clickListener) {
    this.timingClickListener = clickListener;
  }

  public interface onTimingClickListener {
    void onTimingClick(String timing);
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.button_timing, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.timing.setText(timings.get(position));
  }

  @Override public int getItemCount() {
    return timings.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    Button timing;

    public ViewHolder(View itemView) {
      super(itemView);
      timing = (Button) itemView;
      timing.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
      timingClickListener.onTimingClick(timings.get(getAdapterPosition()));
    }
  }
}
