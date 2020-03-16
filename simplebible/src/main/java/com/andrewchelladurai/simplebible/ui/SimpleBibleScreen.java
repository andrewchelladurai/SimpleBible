package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.andrewchelladurai.simplebible.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SimpleBibleScreen
    extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.simple_bible_screen);

    TextView textView = findViewById(R.id.message);
    BottomNavigationView navigation = findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(item -> {
      switch (item.getItemId()) {
        case R.id.navigation_home:
          textView.setText(R.string.title_home);
          return true;
        case R.id.navigation_dashboard:
          textView.setText(R.string.title_dashboard);
          return true;
        case R.id.navigation_notifications:
          textView.setText(R.string.title_notifications);
          return true;
        default:
      }
      return false;
    });
  }

}
