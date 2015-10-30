/*
 * This file 'ServiceReminderScheduler.java' is part of SimpleBible :
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

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

public class ServiceReminderScheduler
        extends Service {

    private static final String TAG = "SrvcReminderScheduler";
    private final IBinder binder = new ServiceBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setAlarm(Calendar c) {
        Log.d(TAG, "setAlarm() Entered");
        new NotifierThread(this, c).run();
        Log.d(TAG, "setAlarm() Exited");
    }

    public class ServiceBinder
            extends Binder {

        ServiceReminderScheduler getService() {
            return ServiceReminderScheduler.this;
        }
    }
}
