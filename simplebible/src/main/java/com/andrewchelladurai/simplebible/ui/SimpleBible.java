package com.andrewchelladurai.simplebible.ui;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    // apply application theme
    setTheme(R.style.SbBaseTheme);

    // apply the theme selected in the preferences
    applyThemeSelectedInPreference();

    if (savedInstanceState == null) {
      setupNotificationChannel();
    }

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
  public void showErrorScreen(@Nullable final String message,
                              final boolean shareLogs,
                              final boolean exitApp) {
    Log.d(TAG, "showErrorScreen: message = [" + message + "], shareLogs = ["
               + shareLogs + "], exitApp = [" + exitApp + "]");
  }

  @Override
  public void restartApp() {
    recreate();
  }

  private void setupNotificationChannel() {
    Log.d(TAG, "setupNotificationChannel:");

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      Log.d(TAG, "setupNotificationChannel: skipping, feature not present in ["
                 + Build.VERSION.CODENAME + "]");
      return;
    }

    final NotificationManager notificationManager = getSystemService(NotificationManager.class);
    if (notificationManager == null) {
      Log.e(TAG, "setupNotificationChannel: skipping coz we have a null NotificationManager");
      return;
    }

    final NotificationChannel setupChannel = new NotificationChannel(
        getString(R.string.notify_channel_id_setup),
        getString(R.string.notify_channel_name_setup),
        NotificationManager.IMPORTANCE_HIGH);
    setupChannel.setDescription(getString(R.string.notify_channel_desc_setup));

    final NotificationChannel reminderChannel = new NotificationChannel(
        getString(R.string.notify_channel_id_reminder),
        getString(R.string.notify_channel_name_reminder),
        NotificationManager.IMPORTANCE_DEFAULT);
    reminderChannel.setDescription(getString(R.string.notify_channel_desc_reminder));

    notificationManager.createNotificationChannel(setupChannel);
    notificationManager.createNotificationChannel(reminderChannel);
  }

  @Override
  public void shareText(@NonNull final String text) {
    hideKeyboard();

    if (text.isEmpty()) {
      Log.e(TAG, "shareText: why do you want to share empty text?");
      return;
    }

    final String appName = getString(R.string.application_name);
    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, appName);
    intent.putExtra(android.content.Intent.EXTRA_TEXT, text);
    startActivity(Intent.createChooser(intent, appName));

  }

}
