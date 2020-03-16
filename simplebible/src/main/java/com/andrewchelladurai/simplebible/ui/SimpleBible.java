package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.andrewchelladurai.simplebible.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SimpleBible
    extends AppCompatActivity {

  private static final String TAG = "SimpleBibleScreen";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.simple_bible);

    final TextView textView = findViewById(R.id.message);
    final BottomNavigationView navigation = findViewById(R.id.main_nav_bar);
    navigation.setOnNavigationItemSelectedListener(item -> {
      switch (item.getItemId()) {
        case R.id.screen_home:
          textView.setText(R.string.main_nav_bar_home);
          return true;
        case R.id.screen_book_list:
          textView.setText(R.string.main_nav_bar_book_list);
          return true;
        case R.id.screen_search:
          textView.setText(R.string.main_nav_bar_search);
          return true;
        case R.id.screen_bookmark_list:
          textView.setText(R.string.main_nav_bar_bookmark_list);
          return true;
        case R.id.screen_settings:
          textView.setText(R.string.main_nav_bar_settings);
          return true;
        default:
          Log.e(TAG, "onCreate:", new IllegalArgumentException(
              "Unknown item passed [" + item.getTitle() + " - " + item.getItemId() + "]"));
      }
      return false;
    });

    NavigationUI.setupWithNavController(
        navigation, Navigation.findNavController(this, R.id.main_nav_host_fragment));

  }

}
