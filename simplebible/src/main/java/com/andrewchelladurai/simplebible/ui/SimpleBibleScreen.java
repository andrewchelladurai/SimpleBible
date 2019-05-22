package com.andrewchelladurai.simplebible.ui;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.DbSetupService;
import com.andrewchelladurai.simplebible.model.SimpleBibleModel;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.andrewchelladurai.simplebible.utils.VerseUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class SimpleBibleScreen
    extends AppCompatActivity
    implements SimpleBibleScreenOps,
               HomeScreen.FragmentInteractionListener,
               BookListScreen.FragmentInteractionListener,
               SearchScreen.FragmentInteractionListener,
               BookmarkListScreen.OnFragmentInteractionListener,
               PreferencesScreen.FragmentInteractionListener {

  private static final String TAG = "SimpleBibleScreen";

  private static boolean flagSetupFinished = false;

  private NotificationChannel notifChannel = null;

  private SimpleBibleModel model;

  @Override
  protected void onResume() {
    model = ViewModelProviders.of(this).get(SimpleBibleModel.class);
    setupNavigationComponent();
    setupDbServiceListeners();
    super.onResume();
  }

  @Override
  protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    setTheme(R.style.SbTheme);
    setContentView(R.layout.simple_bible_screen);

    if (savedState == null) {
      // hide the bottom navigation bar because it is useless when the application is not yet setup.
      hideNavigationComponent();
      createNotificationChannel();
      startDbSetupService();
    }
  }

  private void startDbSetupService() {
    // create a listener to handle failed database setup
    LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
      @Override
      public void onReceive(final Context context, final Intent intent) {
        Log.d(TAG, "onReceive: DbSetupService setup failed");
        Bundle args = new Bundle();
        args.putString(ErrorScreen.ARG_MESSAGE, getString(R.string.db_init_error));
        args.putBoolean(ErrorScreen.ARG_EMAIL_DEV, true);
        Navigation.findNavController(SimpleBibleScreen.this, R.id.main_scr_nav_host_fragment)
                  .navigate(R.id.action_global_errorScreen, args);
      }
    }, new IntentFilter(DbSetupService.ACTION_SETUP_FAILURE));

    // start the database setup service
    ContextCompat.startForegroundService(this, new Intent(this, DbSetupService.class));
  }

  private void setupDbServiceListeners() {
    Log.d(TAG, "setupDbServiceListeners() : flagSetupFinished [" + flagSetupFinished + "]");
    if (flagSetupFinished) {
      showNavigationComponent();
      return;
    }

    hideNavigationComponent();
    model.getBookCount().observe(this, bookCount -> {
      if (bookCount == BookUtils.EXPECTED_COUNT) {
        model.getVerseCount().observe(this, verseCount -> {
          if (verseCount == VerseUtils.EXPECTED_COUNT) {
            flagSetupFinished = true;
            showNavigationComponent();
            showDailyVerse();
          }
        });
      }
    });
  }

  private void showDailyVerse() {
    Log.d(TAG, "showDailyVerse: ");
  }

  private void setupNavigationComponent() {
    Log.d(TAG, "setupNavigationComponent:");
    final NavController navController =
        Navigation.findNavController(this, R.id.main_scr_nav_host_fragment);
    final BottomNavigationView navBar = findViewById(R.id.main_scr_bottom_nav_bar);
    NavigationUI.setupWithNavController(navBar, navController);
  }

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      notifChannel = new NotificationChannel(getPackageName(),
                                             getString(R.string.app_notification_channel_name),
                                             NotificationManager.IMPORTANCE_HIGH);
      notifChannel.setDescription(getString(R.string.app_notification_channel_description));

      getSystemService(NotificationManager.class).createNotificationChannel(notifChannel);
      Log.d(TAG, "createNotificationChannel: created");
    }
    Log.d(TAG, "createNotificationChannel: skipped");
  }

  public void hideKeyboard() {
    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
    //Find the currently focused view, so we can grab the correct window token from it.
    View view = getCurrentFocus();
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
      view = new View(this);
    }
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  public void hideNavigationComponent() {
    findViewById(R.id.main_scr_bottom_nav_bar).setVisibility(View.GONE);
  }

  public void showNavigationComponent() {
    findViewById(R.id.main_scr_bottom_nav_bar).setVisibility(View.VISIBLE);
  }

  public void shareText(@NonNull final String text) {
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

  public void showErrorMessage(@NonNull final String message) {
    hideKeyboard();
    Snackbar.make(findViewById(R.id.main_scr_root), message, Snackbar.LENGTH_LONG)
            .setAnchorView(R.id.main_scr_bottom_nav_bar)
            .show();
  }

  public void showErrorScreen(@Nullable final String message) {
    Log.d(TAG, "showErrorScreen:");
    Bundle args = new Bundle();
    args.putString(ErrorScreen.ARG_MESSAGE, message);
    Navigation.findNavController(this, R.id.main_scr_nav_host_fragment)
              .navigate(R.id.action_global_errorScreen, args);
  }

}
