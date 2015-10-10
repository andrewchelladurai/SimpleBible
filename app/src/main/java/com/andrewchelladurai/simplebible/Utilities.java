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

package com.andrewchelladurai.simplebible;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 04-Oct-2015 at 1:14 PM
 */
public class Utilities {

    private static final String TAG = "Utilities";
    private static SharedPreferences preferences = null;
    private static Utilities utilities = null;

    private Utilities(SharedPreferences sharedPreferences) {
        preferences = sharedPreferences;
    }

    public static void createInstance(SharedPreferences sharedPreferences) {
        if (utilities == null) {
            utilities = new Utilities(sharedPreferences);
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
}
