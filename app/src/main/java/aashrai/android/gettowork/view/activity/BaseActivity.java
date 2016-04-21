package aashrai.android.gettowork.view.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutId());
    configureDagger();
    ButterKnife.bind(this);
  }

  public abstract @LayoutRes int getLayoutId();

  public abstract void configureDagger();
}
