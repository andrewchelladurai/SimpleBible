/*
 * This file 'NotifierService.java' is part of SimpleBible :
 *
 * Copyright (c) 2015.
 *
 * This Application is available at below locations
 * Binary : https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 * Source : https://github.com/andrewchelladurai/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * OR <http://www.gnu.org/licenses/gpl-3.0.txt>
 */

package com.andrewchelladurai.simplebible.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.andrewchelladurai.simplebible.ActivityWelcome;
import com.andrewchelladurai.simplebible.R;

public class NotifierService
        extends Service {

    private static final String TAG = "NotifierService";
    private final IBinder binder = new ServiceBinder();
    private NotificationManager nManager;

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate()");
        nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        showNotification();
        Log.d(TAG, "onStartCommand() Exited.");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void showNotification() {
        Log.d(TAG, "showNotification() Entered");
        boolean showReminder = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("notifications_new_message", true);

        if (showReminder) {
            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("Simple Bible")
                    .setContentText("Press to open and read The Bible")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_alarm_white_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon));

            Intent resultIntent = new Intent(this, ActivityWelcome.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(
                    this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            nBuilder.setContentIntent(resultPendingIntent);
            nManager.notify(1357, nBuilder.build());
        } else {
            Log.d(TAG, "showNotification() Reminder is disabled");
        }
        Log.d(TAG, "showNotification() Exited");
    }

    public class ServiceBinder
            extends Binder {

        NotifierService getService() {
            return NotifierService.this;
        }
    }
}
