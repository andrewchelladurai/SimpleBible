package com.andrewchelladurai.simplebible.ui;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.DbSetupJob;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class ScreenSimpleBible
    extends AppCompatActivity
    implements ScreenSimpleBibleOps {

  private static final String TAG = "ScreenSimpleBible";

  private NotificationChannel notificationChannel;

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
      hideNavigationView();

      // Need this only once when the application launches
      createNotificationChannel();
      startDbSetupService();

    }

  }

  private void startDbSetupService() {
    Log.d(TAG, "startDbSetupService() called");

    final Intent intent = new Intent(this, DbSetupJob.class);

    // start the database setup service
    DbSetupJob.startWork(this, intent, new DbSetupJobResultReceiver(new Handler()));
  }

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      notificationChannel = new NotificationChannel(
          getPackageName(),
          getString(R.string.notificationChannelName),
          NotificationManager.IMPORTANCE_HIGH);
      notificationChannel.setDescription(getString(R.string.notificationChannelDescription));

      getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);
      Log.d(TAG, "createNotificationChannel: created");
    }
    Log.d(TAG, "createNotificationChannel: skipped");
  }

  @Override
  public void hideNavigationView() {
    findViewById(R.id.scrMainBottomNavView).setVisibility(View.GONE);
  }

  @Override
  public void showNavigationView() {
    findViewById(R.id.scrMainBottomNavView).setVisibility(View.VISIBLE);
  }

  @Override
  public void shareText(@NonNull final String text) {
    hideKeyboard();
    if (text.isEmpty()) {
      Log.e(TAG, "shareText: why do you want to share empty text?");
      return;
    }
    final String appName = getString(R.string.appName);
    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, appName);
    intent.putExtra(android.content.Intent.EXTRA_TEXT, text);
    startActivity(Intent.createChooser(intent, appName));
  }

  @Override
  public void showMessage(@NonNull final String message) {
    hideKeyboard();
    Snackbar.make(findViewById(R.id.scrMainNavHostFragment), message, Snackbar.LENGTH_LONG)
            .setAnchorView(R.id.scrMainBottomNavView)
            .show();
  }

  @Override
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

  public class DbSetupJobResultReceiver
      extends ResultReceiver {

    DbSetupJobResultReceiver(Handler handler) {
      super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
      switch (resultCode) {
        case DbSetupJob.STARTED:
          Log.d(TAG, "onReceiveResult: STARTED");
          break;
        case DbSetupJob.RUNNING:
          Log.d(TAG, "onReceiveResult: RUNNING");
          break;
        case DbSetupJob.FINISHED:
          Log.d(TAG, "onReceiveResult: FINISHED");
          break;
        default:
          Log.d(TAG, "onReceiveResult: unknown state");
      }
    }

  }

}

