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
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class ScreenSimpleBible
    extends AppCompatActivity
    implements ScreenSimpleBibleOps {

  private static final String TAG = "ScreenSimpleBible";

  @Override
  protected void onCreate(Bundle savedState) {

    handleThemeToggle();
    setTheme(R.style.SbThemeBase);

    super.onCreate(savedState);

    setContentView(R.layout.screen_simple_bible);

    // setup bottom navigation bar with the navigation host fragment
    NavigationUI.setupWithNavController(
        (BottomNavigationView) findViewById(R.id.screen_main_bottom_nav_bar),
        Navigation.findNavController(this, R.id.screen_main_nav_host_fragment));

    if (savedState == null) {
      // Need this only once when the application launches
      createNotificationChannel();
    }

  }

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      final NotificationChannel notificationChannel = new NotificationChannel(
          getPackageName(),
          getString(R.string.screen_main_notify_channel_name),
          NotificationManager.IMPORTANCE_HIGH);
      notificationChannel.setDescription(
          getString(R.string.screen_main_notify_channel_desc));

      final NotificationManager notificationManager =
          getSystemService(NotificationManager.class);
      if (notificationManager != null) {
        notificationManager.createNotificationChannel(notificationChannel);
      }
      Log.d(TAG, "createNotificationChannel: created");
    }
    Log.d(TAG, "createNotificationChannel: skipped");
  }

  @Override
  public void hideNavigationView() {
    findViewById(R.id.screen_main_bottom_nav_bar).setVisibility(View.GONE);
  }

  @Override
  public void showNavigationView() {
    findViewById(R.id.screen_main_bottom_nav_bar).setVisibility(View.VISIBLE);
  }

  @Override
  public void shareText(@NonNull final String text) {
    hideKeyboard();
    if (text.isEmpty()) {
      Log.e(TAG, "shareText: why do you want to share empty text?");
      return;
    }
    final String appName = getString(R.string.app_name);
    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, appName);
    intent.putExtra(android.content.Intent.EXTRA_TEXT, text);
    startActivity(Intent.createChooser(intent, appName));
  }

  @Override
  public void showMessage(@NonNull final String message) {
    hideKeyboard();
    Snackbar.make(findViewById(R.id.screen_main_nav_host_fragment), message, Snackbar.LENGTH_LONG)
            .setAnchorView(R.id.screen_main_bottom_nav_bar)
            .show();
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
  public void showErrorScreen(@Nullable final String message, final boolean informDev,
                              final boolean exitApp) {
    Log.d(TAG, "showErrorScreen() called with: message = ["
               + message + "], informDev = [" + informDev + "], exitApp = [" + exitApp + "]");
    final Bundle bundle = new Bundle();
    bundle.putString(ScreenError.ARG_MESSAGE, message);
    bundle.putBoolean(ScreenError.ARG_INFORM_DEV, informDev);
    bundle.putBoolean(ScreenError.ARG_EXIT_APP, exitApp);

    Navigation.findNavController(this, R.id.screen_main_nav_host_fragment)
              .navigate(R.id.global_screen_error, bundle);
  }

  @Override
  public void handleThemeToggle() {
    final String keyName = getString(R.string.pref_theme_key);

    final String keyValueAuto = getString(R.string.pref_theme_value_system);
    final String keyValueYes = getString(R.string.pref_theme_value_yes);
    final String keyValueNo = getString(R.string.pref_theme_value_no);

    final String darkThemeMode = PreferenceManager.getDefaultSharedPreferences(this)
                                                  .getString(keyName, keyValueAuto);
    Log.d(TAG, "handleThemeToggle: darkThemeMode[" + darkThemeMode + "]");

    int nightModeValue = AppCompatDelegate.MODE_NIGHT_YES;
    if (darkThemeMode.equalsIgnoreCase(keyValueAuto)) {
      nightModeValue = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                       ? AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                       : AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;
    } else if (darkThemeMode.equalsIgnoreCase(keyValueYes)) {
      //noinspection ConstantConditions
      nightModeValue = AppCompatDelegate.MODE_NIGHT_YES;
    } else if (darkThemeMode.equalsIgnoreCase(keyValueNo)) {
      nightModeValue = AppCompatDelegate.MODE_NIGHT_NO;
    } else {
      Log.d(TAG, "handleThemeToggle: unknown match");
    }

    AppCompatDelegate.setDefaultNightMode(nightModeValue);
  }

  @Override
  public void restartApp() {
    recreate();
  }

}

