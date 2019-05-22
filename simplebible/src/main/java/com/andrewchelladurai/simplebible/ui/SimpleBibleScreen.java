package com.andrewchelladurai.simplebible.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;

public class SimpleBibleScreen
    extends AppCompatActivity
    implements SimpleBibleScreenOps,
               HomeScreen.FragmentInteractionListener {

  private static final String TAG = "SimpleBibleScreen";

  @Override
  protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    setTheme(R.style.SbTheme);
    setContentView(R.layout.simple_bible_screen);

    createNotificationChannel();
  }

  private void createNotificationChannel() {
    Log.d(TAG, "createNotificationChannel:");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel =
          new NotificationChannel(getPackageName(),
                                  getString(R.string.app_notification_channel_name),
                                  NotificationManager.IMPORTANCE_HIGH);
      channel.setDescription(getString(R.string.app_notification_channel_description));

      getSystemService(NotificationManager.class).createNotificationChannel(channel);
    }
  }

}
