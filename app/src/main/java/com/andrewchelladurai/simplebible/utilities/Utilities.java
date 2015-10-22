/*
 * This file 'Utilities.java' is part of SimpleBible :  An Android Bible application
 * with offline access, simple features and easy to use navigation.
 *
 * Copyright (c) Andrew Chelladurai - 2015.
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

package com.andrewchelladurai.simplebible.utilities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.andrewchelladurai.simplebible.ActivityWelcome;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.notification.ReminderScheduler;

import java.util.Calendar;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 04-Oct-2015 at 1:14 PM
 */
public class Utilities {

    private static final String TAG = "Utilities";
    private static SharedPreferences preferences = null;
    private static ActivityWelcome activityWelcome = null;
    private static Utilities utilities = null;
    private static ReminderScheduler reminderScheduler;
    private static int reminderHour;
    private static int reminderMinute;

    private Utilities(SharedPreferences sharedPreferences, ActivityWelcome activityWelcome1) {
        preferences = sharedPreferences;
        activityWelcome = activityWelcome1;

        if (isReminderEnabled()) {
            startReminderService();
        }
    }

    public static void createInstance(SharedPreferences sharedPreferences, ActivityWelcome
            activityWelcome) {
        if (utilities == null) {
            utilities = new Utilities(sharedPreferences, activityWelcome);
        }
    }

    public static void updateTheme(Activity activity) {
        Log.d(TAG, "updateTheme() called with: " + "activity = [" + activity + "]");
        boolean isDarkThemeSet = preferences.getBoolean("pref_app_theme", false);

        if (isDarkThemeSet) {
            activity.setTheme(R.style.DarkTheme);
        } else {
            activity.setTheme(R.style.LightTheme);
        }
    }

    public static void changeTheme(Activity activity) {
        boolean isDarkThemeSet = preferences.getBoolean("pref_app_theme", false);
        Log.d(TAG, "changeTheme() called with: " + "activity = [" + activity + "]");

        activity.finish();

        Intent i = new Intent(activity, activity.getClass());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                   Intent.FLAG_ACTIVITY_CLEAR_TASK |
                   Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i);

        if (isDarkThemeSet) {
            activity.setTheme(R.style.DarkTheme);
        } else {
            activity.setTheme(R.style.LightTheme);
        }
    }

    public static String getStringPreference(String prefName, String defaultValue) {
        return preferences.getString(prefName, defaultValue);
    }

    public static Calendar getReminderTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, reminderHour);
        c.set(Calendar.MINUTE, reminderMinute);
        return c;
    }

    public static void startReminderService() {
        if (!isReminderServiceEnabled() || reminderScheduler == null) {
                reminderScheduler = new ReminderScheduler(activityWelcome);
            reminderScheduler.doBindService();
        }
    }

    public static void stopReminderService() {
        if (reminderScheduler != null) {
            reminderScheduler.doUnbindService();
        }
    }

    public static boolean isReminderEnabled() {
        return preferences.getBoolean("notifications_new_message", true);
    }

    public static boolean isReminderServiceEnabled() {
        if (reminderScheduler == null) {
            Log.e(TAG, "isReminderServiceEnabled() Exited : reminderScheduler == null");
            return false;
        }
        return true;
    }

    public static void setReminderTimestamp(int hour, int minute) {
        Log.d(TAG, "setReminderTimestamp() called with: hour=[" + hour + "], minute=[" +
                   minute + "]");
        reminderHour = hour;
        reminderMinute = minute;

        SharedPreferences.Editor editor = preferences.edit();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, reminderHour);
        calendar.set(Calendar.MINUTE, reminderMinute);

        // To get the localized String in HH:MM AM/PM Format
        if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
            editor.putString("pref_notify_time",
                             reminderHour + " : " + reminderMinute + " AM - Hours : Minutes");
        } else if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
            editor.putString("pref_notify_time",
                             (reminderHour - 12) + " : " + reminderMinute + " PM - Hours : " +
                             "Minutes");
        } else {
            // I don't know what time was set and how
            Log.d(TAG, "setReminderTimestamp() Hit the Else part of the AM/PM condition flow." +
                       "How did this happen?!!");
        }
        editor.apply();
        reminderScheduler.setAlarmForNotification(calendar);
        Log.d(TAG, "setReminderTimestamp() Exited");

    }
}
