package com.andrewchelladurai.simplebible;

import android.content.res.Resources;
import android.util.Log;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 14-May-2016 @ 9:14 PM
 */
public class Utilities {
    private static Utilities staticInstance = null;

    private static final String TAG = "Utilities";
    private Resources mResources;

    public static Utilities getInstance(final Resources pResources) {
        if (staticInstance == null) {
            staticInstance = new Utilities(pResources);
            Log.i(TAG, "getInstance: staticInstance is Initiated");
        }
        return staticInstance;
    }

    private Utilities(final Resources pResources) {
        mResources = pResources;
    }

    public int getBooksColumnCount() {
        boolean isLandscape = mResources.getBoolean(R.bool.isLandscape);
        boolean isLarge = mResources.getBoolean(R.bool.isLarge);
        int count = 2;
        if (isLarge) {
            if (isLandscape) {
                count = 4;
            } else {
                count = 3;
            }
        } else {
            if (isLandscape) {
                count = 3;
            }
        }
        Log.d(TAG, "getBooksColumnCount() returned: " + count);
        return count;
    }
}
