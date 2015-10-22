/*
 * This file 'ReminderScheduler.java' is part of SimpleBible :
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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 20-Oct-2015 @ 10:20 PM
 */
public class ReminderScheduler {

    private static final String TAG = "ReminderScheduler";
    private ServiceReminderScheduler schedulerService;
    private Context context;
    private boolean isBound;

    public ReminderScheduler(Context context) {
        this.context = context;
    }

    public void doBindService() {
        Log.d(TAG, "doBindService() Entered");
        context.bindService(new Intent(context, ServiceReminderScheduler.class),
                            mConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "doBindService() Exited : Reminder Service Begun");
        isBound = true;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            ReminderScheduler.this.schedulerService =
                    ((ServiceReminderScheduler.ServiceBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
//            schedulerService = null;
        }
    };

    public void setAlarmForNotification(Calendar c) {
        Log.d(TAG, "setAlarmForNotification() Entered Exited");
        if (schedulerService == null) {
            Toast.makeText(null, "Please toggle Reminder Preference", Toast.LENGTH_SHORT).show();
            return;
        }
        schedulerService.setAlarm(c);
    }

    public void doUnbindService() {
        Log.d(TAG, "doUnbindService() Entered Exited");
        if (isBound) {
            context.unbindService(mConnection);
            isBound = false;
            Log.d(TAG, "doUnbindService() Reminder Service Stopped");
        }
        Log.d(TAG, "doUnbindService() Exited");
    }

}
