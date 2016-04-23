package aashrai.android.gettowork.utils;

import aashrai.android.gettowork.R;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class AppListDecorator extends RecyclerView.ItemDecoration {

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    if (parent.getChildAdapterPosition(view) != 0) {
      outRect.top = view.getResources().getDimensionPixelOffset(R.dimen.standard_spacing);
    }
  }
}
