package com.andrewchelladurai.simplebible.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
    // apply the theme selected in the preferences
    applyThemeSelectedInPreference();

    // create the UI
    super.onCreate(savedInstanceState);
    setContentView(R.layout.simple_bible);

    // Tie the BottomNavigationBar with the NavigationUi Arch component
    NavigationUI.setupWithNavController(
        (BottomNavigationView) findViewById(R.id.main_nav_bar),
        Navigation.findNavController(this, R.id.main_nav_host_fragment));

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
    // find the view that has focus, if there is none, create the activity window
    final View view = (getCurrentFocus() != null)
                      ? getCurrentFocus()
                      : new View(this);

    final InputMethodManager imm =
        (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
    if (imm != null) {
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    } else {
      Log.e(TAG, "hideKeyboard: failed to hide keyboard", new NullPointerException());
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
