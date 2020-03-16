package com.andrewchelladurai.simplebible.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SimpleBible
    extends AppCompatActivity
    implements SimpleBibleOps {

  private static final String TAG = "SimpleBibleScreen";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    applyThemeSelectedInPreference();
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

  @Override
  public void applyThemeSelectedInPreference() {
    final String keyName = getString(R.string.pref_theme_key);
    final String valueAuto = getString(R.string.pref_theme_value_system);
    final String valueYes = getString(R.string.pref_theme_value_yes);
    final String valueNo = getString(R.string.pref_theme_value_no);

    final String value = PreferenceManager.getDefaultSharedPreferences(this)
                                          .getString(keyName, valueAuto);
    Log.d(TAG, "applyThemeSelectedInPreference: value[" + value + "]");

    if (value.equalsIgnoreCase(valueYes)) {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    } else if (value.equalsIgnoreCase(valueNo)) {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    } else {
      Log.d(TAG, "applyThemeSelectedInPreference: AUTO/unknown, using default behavior");
      AppCompatDelegate.setDefaultNightMode((Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                                            ? AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                                            : AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
    }
  }

  @Override
  public void hideKeyboard() {
    InputMethodManager imm =
        (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
    //Find the currently focused view, so we can grab the correct window token from it.
    View view = getCurrentFocus();
    //If no view currently has focus, create a new one, just so we can grab a window token
    // from it
    if (view == null) {
      view = new View(this);
    }
    if (imm != null) {
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  @Override
  public void hideNavigationView() {
    findViewById(R.id.main_nav_bar).setVisibility(View.GONE);
  }

  @Override
  public void showNavigationView() {
    findViewById(R.id.main_nav_bar).setVisibility(View.VISIBLE);
  }

  @Override
  public void showErrorScreen(final String message, final boolean shareLogs,
                              final boolean exitApp) {
    throw new UnsupportedOperationException("Not yet Implemented");
  }

  @Override
  public void restartApp() {
    recreate();
  }

}
