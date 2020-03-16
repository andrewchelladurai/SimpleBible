package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.andrewchelladurai.simplebible.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SimpleBibleScreen
    extends AppCompatActivity {

  private static final String TAG = "SimpleBibleScreen";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.simple_bible_screen);

    final TextView textView = findViewById(R.id.message);
    final BottomNavigationView navigation = findViewById(R.id.main_nav_bar);
    navigation.setOnNavigationItemSelectedListener(item -> {
      switch (item.getItemId()) {
        case R.id.main_nav_bar_home:
          textView.setText(R.string.main_nav_bar_home);
          return true;
        case R.id.main_nav_bar_book_list:
          textView.setText(R.string.main_nav_bar_book_list);
          return true;
        case R.id.main_nav_bar_search:
          textView.setText(R.string.main_nav_bar_search);
          return true;
        case R.id.main_nav_bar_bookmark_list:
          textView.setText(R.string.main_nav_bar_bookmark_list);
          return true;
        case R.id.main_nav_bar_settings:
          textView.setText(R.string.main_nav_bar_settings);
          return true;
        default:
          Log.e(TAG, "onCreate:", new IllegalArgumentException(
              "Unknown item passed [" + item.getTitle() + " - " + item.getItemId() + "]"));
      }
      return false;
    });

    // show the home screen by default on page load
    navigation.getMenu().performIdentifierAction(R.id.main_nav_bar_home, 0);

  }

}
