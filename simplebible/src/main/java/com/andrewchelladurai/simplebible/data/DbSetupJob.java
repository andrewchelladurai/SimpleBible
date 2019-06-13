package com.andrewchelladurai.simplebible.data;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import com.andrewchelladurai.simplebible.R;

public class DbSetupJob
    extends JobIntentService {

  public static final int STARTED = -1;
  public static final int RUNNING = 0;
  public static final int FINISHED = 1;
  private static final String TAG = "DbSetupJob";
  private static final int JOB_ID = 131416;
  private static ResultReceiver RESULT_RECEIVER;

  public static void startWork(@NonNull Context context,
                               @NonNull Intent work,
                               @NonNull final ResultReceiver resultReceiver) {
    RESULT_RECEIVER = resultReceiver;
    RESULT_RECEIVER.send(STARTED, Bundle.EMPTY);
    enqueueWork(context, DbSetupJob.class, JOB_ID, work);
  }

  @Override
  protected void onHandleWork(@NonNull final Intent intent) {
    Log.d(TAG, "onHandleWork");
    startForegroundNotification();

    for (int i = 0; i < 17; i++) {
      Log.i("DbSetupJob", "Running service " + (i + 1)
                          + "/5 @ " + SystemClock.elapsedRealtime());
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        Log.e(TAG, "onHandleIntent: ", e);
      }
    }

    Log.i("DbSetupJob", "Completed service @ " + SystemClock.elapsedRealtime());
    RESULT_RECEIVER.send(FINISHED, Bundle.EMPTY);
  }

  private void startForegroundNotification() {
    Log.d(TAG, "startForegroundNotification");
    final Intent intent = new Intent(this, DbSetupJob.class);
    final PendingIntent pendingIntent =
        PendingIntent.getActivity(this, 0, intent, 0);
    startForeground(JOB_ID, new NotificationCompat.Builder(this, getPackageName())
                                .setContentTitle(getString(R.string.dbSetupNotificationTitle))
                                .setContentText(getString(R.string.dbSetupNotificationMessage))
                                .setContentIntent(pendingIntent)
                                .setOngoing(true)
                                .setSmallIcon(R.drawable.ic_logo)
                                .setOnlyAlertOnce(true)
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .build());
  }

}
