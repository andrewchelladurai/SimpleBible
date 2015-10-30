/*
 * This file 'NotifierThread.java' is part of SimpleBible :
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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 20-Oct-2015 @ 10:21 PM
 */
public class NotifierThread
        implements Runnable {

    private static final String TAG = "NotifierThread";
    private final Calendar calendar;
    private final AlarmManager alarmManager;
    private final Context context;

    public NotifierThread(Context context, Calendar calendar) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.calendar = calendar;
    }

    @Override
    public void run() {
        Log.d(TAG, "run() Entered");
        Intent intent = new Intent(context, NotifierService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                (60 * 60 * 24 * 1000), pendingIntent);
        Log.d(TAG, "run() Exited");
    }
}
