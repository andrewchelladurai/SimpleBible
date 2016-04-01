/*
 * This file 'Utilities.java' is part of SimpleBible :
 *
 * Copyright (c) 2016.
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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Surface;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 27-Feb-2016 @ 11:18 AM
 */
public class Utilities {

    private static final String TAG = "Utilities";

    private static final Utilities STATIC_INSTANCE = new Utilities();

    private Utilities() {
    }

    public static Utilities getInstance() {
        return STATIC_INSTANCE;
    }

    public int getChapterListColumnCount(int rotation, Resources resources) {
/*
        if (isLargeDisplay(resources)) {
            return 2;
        }
*/

        switch (rotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                Log.d(TAG, "getChapterListColumnCount() either 0 | 180");
                if (isLargeDisplay(resources)) {
                    return 2;
                } else {
                    return 1;
                }
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                Log.d(TAG, "getChapterListColumnCount() either 90 | 270");
                if (resources.getBoolean(R.bool.isLarge)) {
                    return 2;
                } else {
                    return 2;
                }
            default:
                return 1;
        }
    }

    public Typeface getPreferredStyle(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String value =
                pref.getString(context.getString(R.string.pref_ui_text_style_key_name),
                        context.getString(R.string.pref_ui_text_style_value_normal));
        if (value.equalsIgnoreCase(context.getString(
                R.string.pref_ui_text_style_value_old_english))) {
            return Typeface.SERIF;
        } else if (value.equalsIgnoreCase(context.getString(
                R.string.pref_ui_text_style_value_typewriter))) {
            return Typeface.MONOSPACE;
        }
        return Typeface.DEFAULT;
    }

    public float getPreferredSize(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String value =
                pref.getString(context.getString(R.string.pref_ui_text_size_key_name),
                        context.getString(R.string.pref_ui_text_size_value_normal));
        if (value.equalsIgnoreCase(
                context.getString(R.string.pref_ui_text_size_value_small))) {
            return 14f;
        } else if (value.equalsIgnoreCase(
                context.getString(R.string.pref_ui_text_size_value_big))) {
            return 22f;
        } else if (value.equalsIgnoreCase(
                context.getString(R.string.pref_ui_text_size_value_huge))) {
            return 26f;
        }
        return 18f;
    }

    public boolean isLargeDisplay(Resources resources) {
        if (resources.getBoolean(R.bool.isLarge)) {
            Log.d(TAG, "isLargeDisplay : true");
            return true;
        } else {
            Log.d(TAG, "isLargeDisplay : false");
            return false;
        }
    }

    public int getTheme(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        if (pref.getBoolean("pref_ui_dark_theme", false)) {
            Log.d(TAG, "changeTheme: Dark Selected");
            return R.style.ThemeLight;
        } else {
            Log.d(TAG, "changeTheme: Light Selected");
            return android.R.style.Theme_DeviceDefault;
        }
    }
}
