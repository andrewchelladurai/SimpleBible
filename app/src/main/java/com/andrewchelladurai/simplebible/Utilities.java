package com.andrewchelladurai.simplebible;

import android.content.res.Resources;
import android.util.Log;
import android.view.Surface;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 27-Feb-2016 @ 11:18 AM
 */
public class Utilities {

    private static final String TAG = "Utilities";

    private static Utilities staticInstance = new Utilities();

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
}
