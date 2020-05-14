package com.andrewchelladurai.simplebible.ui;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.ReminderWorker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SimpleBible
    extends AppCompatActivity
    implements SimpleBibleOps {

  private static final String TAG = "SimpleBibleScreen";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // apply application theme
    setTheme(R.style.SbBaseTheme);

    // apply the theme selected in the preferences
    updateApplicationTheme();

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
  public void updateApplicationTheme() {
    final String value = PreferenceManager.getDefaultSharedPreferences(this)
                                          .getString(getString(R.string.pref_theme_key),
                                                     getString(R.string.pref_theme_value_system));
    Log.d(TAG, "updateApplicationTheme: value[" + value + "]");
    if (value.equalsIgnoreCase(getString(R.string.pref_theme_value_yes))) {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    } else if (value.equalsIgnoreCase(getString(R.string.pref_theme_value_no))) {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    } else {
      Log.d(TAG, "updateApplicationTheme: AUTO/unknown, using default behavior");
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

  @Override
  public void showMessage(@NonNull final String string, @IdRes final int anchorViewId) {
    Snackbar.make(findViewById(anchorViewId), string, Snackbar.LENGTH_SHORT)
            .setAnchorView(anchorViewId)
            .show();
  }

  @Override
  public void updateDailyVerseReminderState() {
    if (PreferenceManager.getDefaultSharedPreferences(this)
                         .getBoolean(getString(R.string.pref_reminder_key), true)) {
      updateDailyVerseReminderTime();
    } else {
      final WorkManager workManager = WorkManager.getInstance(this);
      workManager.cancelAllWorkByTag(ReminderWorker.TAG);
      Log.d(TAG, "updateDailyVerseReminderState: cancelled All Work By Tag ["
                 + ReminderWorker.TAG + "]");
    }
  }

  @Override
  public void updateDailyVerseReminderTime() {
    final String prefKeyHour = getString(R.string.pref_reminder_time_hour_key);
    final String prefKeyMin = getString(R.string.pref_reminder_time_minute_key);

    final Resources resources = getResources();
    final int defaultHourValue = resources.getInteger(R.integer.default_reminder_time_hour);
    final int defaultMinValue = resources.getInteger(R.integer.default_reminder_time_minute);

    final SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
    final int hour = sPref.getInt(prefKeyHour, defaultHourValue);
    final int minute = sPref.getInt(prefKeyMin, defaultMinValue);

    final Calendar reminderTimeStamp = Calendar.getInstance();
    reminderTimeStamp.set(Calendar.HOUR_OF_DAY, hour);
    reminderTimeStamp.set(Calendar.MINUTE, minute);
    reminderTimeStamp.set(Calendar.SECOND, 0);

    final Calendar timeNow = Calendar.getInstance();
    if (reminderTimeStamp.before(timeNow)) {
      reminderTimeStamp.add(Calendar.HOUR_OF_DAY, 24);
      Log.d(TAG, "updateDailyVerseReminderTime: reminderTimestamp is past, adjusted by 24 Hours");
    }

    final long timeDiff = reminderTimeStamp.getTimeInMillis() - timeNow.getTimeInMillis();
    Log.d(TAG, "updateDailyVerseReminderTime: reminderTimeStamp["
               + DateFormat.format("yyyy-MMM-dd @ HH:mm:ss z", reminderTimeStamp) + "]");

    final WorkManager workManager = WorkManager.getInstance(getApplicationContext());
    workManager.cancelAllWorkByTag(ReminderWorker.TAG);
    workManager.enqueueUniqueWork(ReminderWorker.TAG,
                                  ExistingWorkPolicy.REPLACE,
                                  new OneTimeWorkRequest.Builder(ReminderWorker.class)
                                      .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                                      .addTag(ReminderWorker.TAG)
                                      .setInputData(new Data.Builder()
                                                        .putInt(ReminderWorker.ARG_HOUR, hour)
                                                        .putInt(ReminderWorker.ARG_MINUTE, minute)
                                                        .build())
                                      .build());

    final ListenableFuture<List<WorkInfo>> listWorkInfoByTag =
        workManager.getWorkInfosByTag(ReminderWorker.TAG);

    try {
      final List<WorkInfo> infoList = listWorkInfoByTag.get();
      final WorkInfo.State enqueuedState = WorkInfo.State.ENQUEUED;
      for (final WorkInfo workInfo : infoList) {
        if (workInfo.getState().equals(enqueuedState)) {
          Log.d(TAG, "doWork: [" + enqueuedState.name() + "]" + "[" + workInfo.getId() + "]");
        }
      }
    } catch (ExecutionException | InterruptedException ex) {
      Log.e(TAG, "updateDailyVerseReminderTime: ", ex);
    }

  }

}
