package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ScreenSimpleBible
    extends AppCompatActivity
    implements ScreenSimpleBibleOps {

  @Override
  protected void onCreate(Bundle savedState) {
    setTheme(R.style.SbTheme);
    super.onCreate(savedState);
    setContentView(R.layout.screen_simple_bible);

    // setup bottom navigation bar with the navigation host fragment
    NavigationUI.setupWithNavController(
        (BottomNavigationView) findViewById(R.id.scrMainBottomNavView),
        Navigation.findNavController(this, R.id.scrMainNavHostFragment));

    if (savedState == null) {
      showNavigationView();
    }

  }

  @Override
  public void hideNavigationView() {
    findViewById(R.id.scrMainBottomNavView).setVisibility(View.GONE);
  }

  @Override
  public void showNavigationView() {
    findViewById(R.id.scrMainBottomNavView).setVisibility(View.VISIBLE);
  }

}
