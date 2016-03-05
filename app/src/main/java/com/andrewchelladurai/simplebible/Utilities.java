package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Surface;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 27-Feb-2016 @ 11:18 AM
 */
public class Utilities {

    private static final String TAG = "Utilities";

    private static final Utilities staticInstance = new Utilities();

    private Utilities() {
    }

    public static Utilities getInstance() {
        return staticInstance;
    }

    public int getChapterListColumnCount(int rotation, Resources resources) {
        if (resources.getBoolean(R.bool.isLarge)) {
            Log.d(TAG, "getChapterListColumnCount: isLarge");
            return 2;
        }
        Log.d(TAG, "getChapterListColumnCount: not isLarge");

        switch (rotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                Log.d(TAG, "getChapterListColumnCount() either 0 | 180");
                return 1;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                Log.d(TAG, "getChapterListColumnCount() either 90 | 270");
                return 2;
        }
        return 1;
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
        String str =
                pref.getString(context.getString(R.string.pref_ui_text_size_key_name),
                               context.getString(R.string.pref_ui_text_size_value_normal));
        if (str.equalsIgnoreCase(
                context.getString(R.string.pref_ui_text_size_value_small))) {
            return 14f;
        } else if (str.equalsIgnoreCase(
                context.getString(R.string.pref_ui_text_size_value_big))) {
            return 22f;
        } else if (str.equalsIgnoreCase(
                context.getString(R.string.pref_ui_text_size_value_huge))) {
            return 26f;
        }
        return 18f;
    }

}
