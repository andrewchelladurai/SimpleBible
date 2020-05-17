package com.andrewchelladurai.simplebible.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.andrewchelladurai.simplebible.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ReminderWorker
    extends Worker {

  public static final String TAG = "ReminderWorker";

  public static final String ARG_HOUR = "HOUR";

  public static final String ARG_MINUTE = "MINUTE";

  public ReminderWorker(@NonNull final Context context,
                        @NonNull final WorkerParameters params) {
    super(context, params);
    Log.d(TAG, "ReminderWorker: UUID [" + getId().toString() + "]");
  }

  @NonNull
  @Override
  public Result doWork() {
    triggerNotification();
    enqueueNextWorkRequest();
    return Result.success();
  }

  private void triggerNotification() {
    Log.d(TAG, "triggerNotification:");
    // TODO: 15/5/20 show a Pending Notification displaying the day's verse.
  }

  private void enqueueNextWorkRequest() {
    Log.d(TAG, "enqueueNextWorkRequest:");
    final Data iData = getInputData();
    final Resources resources = getApplicationContext().getResources();

    final int defaultHour = resources.getInteger(R.integer.default_reminder_time_hour);
    final int defaultMin = resources.getInteger(R.integer.default_reminder_time_minute);

    final int hour = iData.getInt(ARG_HOUR, defaultHour);
    final int minute = iData.getInt(ARG_MINUTE, defaultMin);

    final Calendar reminderTimeStamp = Calendar.getInstance();
    reminderTimeStamp.set(Calendar.HOUR_OF_DAY, hour);
    reminderTimeStamp.set(Calendar.MINUTE, minute);
    reminderTimeStamp.set(Calendar.SECOND, 0);

    final Calendar timeNow = Calendar.getInstance();
    if (reminderTimeStamp.before(timeNow)) {
      reminderTimeStamp.add(Calendar.HOUR_OF_DAY, 24);
      Log.d(TAG, "enqueueNextWorkRequest: reminderTimestamp is past, adjusted by 24 Hours");
    }

    final long timeDiff = reminderTimeStamp.getTimeInMillis() - timeNow.getTimeInMillis();
    Log.d(TAG, "enqueueNextWorkRequest: reminderTimeStamp["
               + DateFormat.format("yyyy-MMM-dd @ HH:mm:ss z", reminderTimeStamp) + "]");

    final WorkManager workManager = WorkManager.getInstance(getApplicationContext());
    workManager.cancelAllWorkByTag(ReminderWorker.TAG);
    workManager.enqueueUniqueWork(ReminderWorker.TAG,
                                  ExistingWorkPolicy.REPLACE,
                                  new OneTimeWorkRequest.Builder(ReminderWorker.class)
                                      .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                                      .addTag(ReminderWorker.TAG)
                                      .setInputData(iData)
                                      .build());
  }

}
