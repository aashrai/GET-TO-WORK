package aashrai.android.gettowork;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by aashrai on 21/4/16.
 */
public class TimingGridDecorator extends RecyclerView.ItemDecoration {

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    int position = parent.getChildAdapterPosition(view);
    int spacing =
        parent.getContext().getResources().getDimensionPixelOffset(R.dimen.timing_grid_spacing);
    if (position != 0) outRect.top = spacing;
    switch (position) {
      case 6:
      case 1:
        outRect.right = spacing / 2;
        break;
      case 7:
      case 2:
        outRect.left = spacing / 2;
        break;
      case 3:
        outRect.right = spacing / 2;
        break;
      case 4:
        outRect.left = spacing / 2;
        outRect.right = spacing / 2;
        break;
      case 5:
        outRect.left = spacing / 2;
        break;
    }
  }
}
